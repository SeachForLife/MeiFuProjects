package com.carl_yang.domain;

import java.util.List;

/**
 * Created by Loren Yang on 2018/1/11.
 */

public class CollectClassDomain {

    private List<Collect_List> list;

    public List<Collect_List> getList() {
        return list;
    }

    public CollectClassDomain setList(List<Collect_List> list) {
        this.list = list;
        return this;
    }

    public class Collect_List {

        private String tcr_pic;//图片地址
        private String tcr_url;//video address
        private String tcr_name;//image name
        private String tcr_desc;//image desc
        private String type;//video OR picture

        public String getTcr_pic() {
            return tcr_pic;
        }

        public Collect_List setTcr_pic(String tcr_pic) {
            this.tcr_pic = tcr_pic;
            return this;
        }

        public String getTcr_url() {
            return tcr_url;
        }

        public Collect_List setTcr_url(String tcr_url) {
            this.tcr_url = tcr_url;
            return this;
        }

        public String getTcr_name() {
            return tcr_name;
        }

        public Collect_List setTcr_name(String tcr_name) {
            this.tcr_name = tcr_name;
            return this;
        }

        public String getTcr_desc() {
            return tcr_desc;
        }

        public Collect_List setTcr_desc(String tcr_desc) {
            this.tcr_desc = tcr_desc;
            return this;
        }

        public String getType() {
            return type;
        }

        public Collect_List setType(String type) {
            this.type = type;
            return this;
        }
    }
}
