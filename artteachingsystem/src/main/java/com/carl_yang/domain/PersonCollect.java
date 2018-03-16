package com.carl_yang.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Loren Yang on 2017/10/12.
 */

public class PersonCollect implements Parcelable {

    private String p_url;
    private String v_url;
    private String iamgename;
    private String content;
    private String auth;
    private String type;//视频OR图片
    private int get_type;//获取的途径

    public int getGet_type() {
        return get_type;
    }

    public void setGet_type(int get_type) {
        this.get_type = get_type;
    }

    public String getP_url() {
        return p_url;
    }

    public void setP_url(String p_url) {
        this.p_url = p_url;
    }

    public String getV_url() {
        return v_url;
    }

    public void setV_url(String v_url) {
        this.v_url = v_url;
    }

    public String getIamgename() {
        return iamgename;
    }

    public void setIamgename(String iamgename) {
        this.iamgename = iamgename;
    }

    public String getContent() {
        return content;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PersonCollect() {

    }

    public PersonCollect(String url, String imname, String imcontent, String auth, String large_url) {
        this.p_url = url;
        this.v_url = large_url;
        this.iamgename = imname;
        this.content = imcontent;
        this.auth = auth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(p_url);
        dest.writeString(v_url);
        dest.writeString(iamgename);
        dest.writeString(content);
        dest.writeString(auth);
    }

    public static final Creator<PersonCollect> CREATOR = new Creator<PersonCollect>() {
        @Override
        public PersonCollect createFromParcel(Parcel in) {
            PersonCollect pc = new PersonCollect();
            pc.p_url = in.readString();
            pc.v_url = in.readString();
            pc.iamgename = in.readString();
            pc.content = in.readString();
            pc.auth = in.readString();
            return pc;
        }

        @Override
        public PersonCollect[] newArray(int size) {
            return new PersonCollect[size];
        }
    };
}
