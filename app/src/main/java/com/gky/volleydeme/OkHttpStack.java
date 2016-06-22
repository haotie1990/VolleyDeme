package com.gky.volleydeme;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by 凯阳 on 2016/6/21.引用自https://gist.github.com/bryanstern/4e8f1cb5a8e14c202750
 */
public class OkHttpStack implements HttpStack{

    private OkHttpClient mClient;

    public OkHttpStack(OkHttpClient client) {
        mClient = client;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
        throws IOException, AuthFailureError {

        // 克隆一个OkHttpClien
        OkHttpClient client = mClient.clone();

        // 设置超时参数
        int timeoutMs = request.getTimeoutMs();
        client.setConnectTimeout(timeoutMs, TimeUnit.MILLISECONDS);
        client.setReadTimeout(timeoutMs, TimeUnit.MILLISECONDS);
        client.setWriteTimeout(timeoutMs, TimeUnit.MILLISECONDS);

        // 创建一个Request.Builder实例
        com.squareup.okhttp.Request.Builder okHttpRequestBuilder = new com.squareup.okhttp.Request.Builder();
        okHttpRequestBuilder.url(request.getUrl());

        // 复制请求头到okhttp的请求头中
        Map<String, String> headers = request.getHeaders();
        for (final String name : headers.keySet()){
            okHttpRequestBuilder.addHeader(name, headers.get(name));
        }
        for (final String name : additionalHeaders.keySet()){
            okHttpRequestBuilder.addHeader(name, additionalHeaders.get(name));
        }

        setConnectionParametersForRequest(okHttpRequestBuilder, request);

        // 执行网络请求获取响应结果
        com.squareup.okhttp.Request okHttpRequest = okHttpRequestBuilder.build();
        Call okHttpCall = client.newCall(okHttpRequest);
        Response okHttpResponse = okHttpCall.execute();

        // 创建状态行实例
        BasicStatusLine responseStatus = new BasicStatusLine(parseProtocol(okHttpResponse.protocol()),
            okHttpResponse.code(), okHttpResponse.message());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        // 设置响应体
        response.setEntity(entityFromOkHttpResponse(okHttpResponse));

        // 构建响应头
        Headers responseHeaders = okHttpResponse.headers();
        for (int i = 0, len = responseHeaders.size(); i < len; i++){
            final String name = responseHeaders.name(i), value = responseHeaders.value(i);
            if(name != null){
                response.addHeader(new BasicHeader(name, value));
            }
        }

        return response;
    }

    private static void setConnectionParametersForRequest(com.squareup.okhttp.Request.Builder builder, Request<?> request) throws AuthFailureError {
        switch (request.getMethod()){
            case Request.Method.DEPRECATED_GET_OR_POST:
                byte[] postBody = request.getBody();
                if(postBody != null){
                    builder.post(RequestBody.create(MediaType.parse(request.getBodyContentType()), postBody));
                }
                break;
            case Request.Method.GET:
                builder.get();
                break;
            case Request.Method.DELETE:
                builder.delete();
                break;
            case Request.Method.POST:
                builder.post(createRequestBody(request));
                break;
            case Request.Method.PUT:
                builder.put(createRequestBody(request));
                break;
            case Request.Method.HEAD:
                builder.head();
                break;
            case Request.Method.OPTIONS:
                builder.method("OPTIONS", null);
                break;
            case Request.Method.TRACE:
                builder.method("TRACE", null);
                break;
            case Request.Method.PATCH:
                builder.patch(createRequestBody(request));
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static RequestBody createRequestBody(Request request) throws AuthFailureError {
        final byte[] body = request.getBody();
        if(body == null)
            return null;
        return RequestBody.create(MediaType.parse(request.getBodyContentType()), body);
    }

    private static ProtocolVersion parseProtocol(final Protocol protocol){
        switch (protocol){
            case HTTP_1_0:
                return new ProtocolVersion("HTTP", 1, 0);
            case HTTP_1_1:
                return new ProtocolVersion("HTTP", 1, 1);
            case HTTP_2:
                return new ProtocolVersion("HTTP", 2, 0);
            case SPDY_3:
                return new ProtocolVersion("SPDY", 3, 1);
            default:
                throw new IllegalAccessError("Unknown Protocol.");
        }
    }

    private static HttpEntity entityFromOkHttpResponse(Response response) throws IOException {
        BasicHttpEntity entity = new BasicHttpEntity();
        ResponseBody requestBody = response.body();

        entity.setContent(requestBody.byteStream());
        entity.setContentLength(requestBody.contentLength());
        entity.setContentEncoding(response.header("Content-Encoding"));

        if(requestBody.contentType() != null){
            entity.setContentType(requestBody.contentType().type());
        }
        return entity;
    }
}
