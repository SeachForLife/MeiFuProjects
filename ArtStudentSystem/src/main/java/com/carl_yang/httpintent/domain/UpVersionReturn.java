package com.carl_yang.httpintent.domain;

/**
 * Created by Administrator on 2017/3/17.
 */

public class UpVersionReturn {

    private String success;
    private String name;
    private String url;
    private String desc;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
