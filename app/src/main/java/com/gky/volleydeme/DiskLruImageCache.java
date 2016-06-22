package com.gky.volleydeme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 凯阳 on 2016/6/20.
 */
public class DiskLruImageCache implements ImageLoader.ImageCache{

    private static int VERSION = 1;

    private static int VALUE_COUNT = 1;

    private Context mContext;

    private DiskLruCache mDiskLruCache;

    public DiskLruImageCache(Context context) {
        mContext = context;
        try {
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir("image"), VERSION, VALUE_COUNT,
                20 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCacheDir(String name){
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
            || !Environment.isExternalStorageRemovable()){
            cachePath = mContext.getExternalCacheDir().getPath();
        }else{
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + name);
    }

    private String bytes2HexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++){
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1){
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    private String MD5(String url){
        String md5Key;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            md5Key = bytes2HexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            md5Key = String.valueOf(url.hashCode());
        }
        return md5Key;
    }

    @Override
    public Bitmap getBitmap(String url) {
        String md5Key = MD5(url);
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(md5Key);
            InputStream is = snapshot.getInputStream(0);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String md5Key = MD5(url);
        try {
            DiskLruCache.Editor editor = mDiskLruCache.edit(md5Key);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, editor.newOutputStream(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
