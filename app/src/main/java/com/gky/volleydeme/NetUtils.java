package com.gky.volleydeme;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by 凯阳 on 2016/6/20.
 */
public class NetUtils {

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private static class NetUtilsLoader {
        public static NetUtils INSTANCE = new NetUtils();
    }

    private NetUtils(){
        mRequestQueue = Volley.newRequestQueue(MainApplication.getContext());

        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(MainApplication.getContext()));
    }

    public static NetUtils getInstance(){
        return NetUtilsLoader.INSTANCE;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> request){
        mRequestQueue.add(request);
    }

    public void cancelAll(Object tag){
        mRequestQueue.cancelAll(tag);
    }
}
