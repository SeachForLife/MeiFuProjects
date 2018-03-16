package com.carl_yang.httpintent.domain;

import java.util.List;

/**
 * Created by carl_yang on 2017/3/27.
 */

public class StuDoamin {

    private String success;
    private String is_submit_works;
    private StuMessage stu;
    private String isLike;
    private LastComment lastComment;
    //纹样、博物馆入口选择
    private String show_page;
    private String page_name;

    public String getShow_page() {
        return show_page;
    }

    public void setShow_page(String show_page) {
        this.show_page = show_page;
    }

    public String getPage_name() {
        return page_name;
    }

    public void setPage_name(String page_name) {
        this.page_name = page_name;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public StuMessage getStu() {
        return stu;
    }

    public void setStu(StuMessage stu) {
        this.stu = stu;
    }

    public String getIs_submit_works() {
        return is_submit_works;
    }

    public void setIs_submit_works(String is_submit_works) {
        this.is_submit_works = is_submit_works;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public LastComment getLastComment() {
        return lastComment;
    }

    public void setLastComment(LastComment lastComment) {
        this.lastComment = lastComment;
    }

    public class StuMessage {
        private String stu_name;
        private String stu_no;
        private String stu_id;
        private String ctr_id;
        private String is_login;
        private String status;

        public String getStu_name() {
            return stu_name;
        }

        public void setStu_name(String stu_name) {
            this.stu_name = stu_name;
        }

        public String getIs_login() {
            return is_login;
        }

        public void setIs_login(String is_login) {
            this.is_login = is_login;
        }

        public String getCtr_id() {
            return ctr_id;
        }

        public void setCtr_id(String ctr_id) {
            this.ctr_id = ctr_id;
        }

        public String getStu_id() {
            return stu_id;
        }

        public void setStu_id(String stu_id) {
            this.stu_id = stu_id;
        }

        public String getStu_no() {
            return stu_no;
        }

        public void setStu_no(String stu_no) {
            this.stu_no = stu_no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public class LastComment{
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
