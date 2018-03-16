package com.carl_yang.domain;

/**
 * Created by carl_yang on 2017/4/13.
 */

public class findNoEndClassRecord {

    private String is_record;
    private Crmap crmap;

    public String getIs_record() {
        return is_record;
    }

    public void setIs_record(String is_record) {
        this.is_record = is_record;
    }

    public Crmap getCrmap() {
        return crmap;
    }

    public void setCrmap(Crmap crmap) {
        this.crmap = crmap;
    }

    public class Crmap{
        private String class_id;
        private String ctr_id;
        private String ctr_date;
        private String curr_date;

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

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
