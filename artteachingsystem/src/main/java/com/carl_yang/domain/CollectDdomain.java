package com.carl_yang.domain;

import java.util.List;

/**
 * Created by Loren Yang on 2017/10/15.
 */

public class CollectDdomain {


    private List<Collect_Msg_List> map;

    public List<Collect_Msg_List> getMap() {
        return map;
    }

    public void setMap(List<Collect_Msg_List> map) {
        this.map = map;
    }

    public class Collect_Msg_List {
        private String pUrl;//图片地址
        private String vUrl;//video address
        private String wName;//image name
        private String wDesc;//image desc
        private String type;//video OR picture

        public String getvUrl() {
            return vUrl;
        }

        public Collect_Msg_List setvUrl(String vUrl) {
            this.vUrl = vUrl;
            return this;
        }

        public String getpUrl() {
            return pUrl;
        }

        public Collect_Msg_List setpUrl(String pUrl) {
            this.pUrl = pUrl;
            return this;
        }

        public String getwName() {
            return wName;
        }

        public Collect_Msg_List setwName(String wName) {
            this.wName = wName;
            return this;
        }

        public String getwDesc() {
            return wDesc;
        }

        public Collect_Msg_List setwDesc(String wDesc) {
            this.wDesc = wDesc;
            return this;
        }

        public String getType() {
            return type;
        }

        public Collect_Msg_List setType(String type) {
            this.type = type;
            return this;
        }
    }

}
