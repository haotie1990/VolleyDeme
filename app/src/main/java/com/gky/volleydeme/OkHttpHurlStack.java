package com.gky.volleydeme;

import com.android.volley.toolbox.HurlStack;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 凯阳 on 2016/6/20.
 */
public class OkHttpHurlStack extends HurlStack{

    private final OkUrlFactory mFactory;

    public OkHttpHurlStack() {
        this(new OkHttpClient());
    }

    public OkHttpHurlStack(OkHttpClient client){
        if(client == null){
            throw new NullPointerException("Client must not be null.");
        }
        mFactory = new OkUrlFactory(client);
    }

    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException {
        return mFactory.open(url);
    }
}
