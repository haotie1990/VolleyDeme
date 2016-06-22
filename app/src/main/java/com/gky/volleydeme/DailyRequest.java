package com.gky.volleydeme;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 凯阳 on 2016/6/21.
 */
public class DailyRequest extends Request<List<ItemResponseData>> {

    private Response.Listener mListener;

    public DailyRequest(int method, String url, Response.Listener<List<ItemResponseData>> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
    }

    @Override
    protected Response<List<ItemResponseData>> parseNetworkResponse(NetworkResponse response) {
        try {
            String dataString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            JSONObject root = JSON.parseObject(dataString);
            JSONArray category = root.getJSONArray("category");
            JSONObject results = root.getJSONObject("results");

            List<ItemResponseData> itemResponseDatas = new ArrayList<>();
            for (int i = 0, len = category.size(); i < len; i++){
                String categoryItem = category.getString(i);
                if(categoryItem.equals(ItemResponseData.ITEM_TYPE_BEAUTITY)){
                    continue;
                }
                itemResponseDatas.add(new ItemResponseData("NONE"));
                String text = results.get(categoryItem).toString();
                List<ItemResponseData> itemList = JSON.parseObject(text, new TypeReference<List<ItemResponseData>>(){});
                itemResponseDatas.addAll(itemList);
            }
            itemResponseDatas.add(0, JSON.parseObject(results.get(ItemResponseData.ITEM_TYPE_BEAUTITY).toString(),
                new TypeReference<List<ItemResponseData>>(){}).get(0));
            return Response.success(itemResponseDatas, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Response.error(new VolleyError(response));
        }
        return null;
    }

    @Override
    protected void deliverResponse(List<ItemResponseData> response) {
        mListener.onResponse(response);
    }
}
