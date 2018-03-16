package com.carl_yang.domain;

/**
 * Created by Administrator on 2017/3/17.
 */

public class LoginReturn {

    private String success;
    private String hasUser;
    private MessageTea user;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getHasUser() {
        return hasUser;
    }

    public void setHasUser(String hasUser) {
        this.hasUser = hasUser;
    }

    public MessageTea getUser() {
        return user;
    }

    public void setUser(MessageTea user) {
        this.user = user;
    }

    public class MessageTea {
        private String cn_name;
        private String picurl;
        private String school_id;
        private String user_id;

        public String getCn_name() {
            return cn_name;
        }

        public void setCn_name(String cn_name) {
            this.cn_name = cn_name;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getSchool_id() {
            return school_id;
        }

        public void setSchool_id(String school_id) {
            this.school_id = school_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
