package com.gky.volleydeme;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by 凯阳 on 2016/6/21.
 */
public class ItemResponseData {

    public static final String ITEM_TYPE_ANDROID = "Android";
    public static final String ITEM_TYPE_IOS = "iOS";
    public static final String ITEM_TYPE_RECOMMAND = "瞎推荐";
    public static final String ITEM_TYPE_EXTENTION = "拓展资源";
    public static final String ITEM_TYPE_BEAUTITY = "福利";
    public static final String ITEM_TYPE_VIDEO = "休息视频";
    public static final String ITEM_TYPE_NONE = "NONE";

    @JSONField(name = "_id")
    public String _id;

    @JSONField(name = "createdAt")
    public String createAt;

    @JSONField(name = "desc")
    public String desc;

    @JSONField(name = "publishedAt")
    public String publishAt;

    @JSONField(name = "source")
    public String source;

    @JSONField(name = "type")
    public String type;

    @JSONField(name = "url")
    public String url;

    @JSONField(name = "used")
    public boolean used;

    @JSONField(name = "who")
    public String who;

    public ItemResponseData(){

    }

    public ItemResponseData(String type) {
        this.type = type;
    }

    public String getDescription(){
        return desc;
    }

    public String getUrl(){
        return url;
    }

    public String getWho(){
        return who;
    }

    public String getType(){
        return type;
    }
}
