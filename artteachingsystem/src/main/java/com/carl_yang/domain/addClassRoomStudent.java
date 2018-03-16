package com.carl_yang.domain;

/**
 * Created by Administrator on 2017/3/17.
 */

public class addClassRoomStudent {

    private String success;
    private CrMap crmap;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public CrMap getCrmap() {
        return crmap;
    }

    public void setCrmap(CrMap crmap) {
        this.crmap = crmap;
    }

    public class CrMap{
        private String ctr_id;
        private String ctr_date;
        private String curr_date;

        public String getCtr_id() {
            return ctr_id;
        }

        public void setCtr_id(String ctr_id) {
            this.ctr_id = ctr_id;
        }

        public String getCtr_date() {
            return ctr_date;
        }

        public void setCtr_date(String ctr_date) {
            this.ctr_date = ctr_date;
        }

        public String getCurr_date() {
            return curr_date;
        }

        public void setCurr_date(String curr_date) {
            this.curr_date = curr_date;
        }
    }

}
