package com.gky.volleydeme;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by 凯阳 on 2016/6/21.
 */
public class ResponseData<T> {

    @JSONField(name = "category")
    private List<String> category;

    @JSONField(name = "error")
    private boolean error;

    @JSONField(name = "results")
    private T results;

    public T getResults() {
        return results;
    }
}
