package com.gky.volleydeme;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by 凯阳 on 2016/6/21.
 */
public class DailyResponseData<T> {

    @JSONField(name = "Android")
    private List<T> mAndroids;

    @JSONField(name = "iOS")
    private List<T> mIOS;

    @JSONField(name = "瞎推荐")
    private List<T> mRecommands;

    @JSONField(name = "拓展资源")
    private List<T> mExtentions;

    @JSONField(name = "福利")
    private List<T> mBeautiys;

    @JSONField(name = "休息视频")
    private List<T> mVedio;

    public List<T> getAndroids() {
        return mAndroids;
    }

    public List<T> getBeautiys() {
        return mBeautiys;
    }

    public List<T> getExtentions() {
        return mExtentions;
    }

    public List<T> getIOS() {
        return mIOS;
    }

    public List<T> getRecommands() {
        return mRecommands;
    }

    public List<T> getVedio() {
        return mVedio;
    }
}
