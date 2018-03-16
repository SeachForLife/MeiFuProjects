package com.carl_yang.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public class ClassCombo {

    private List<Gradelist> list;

    public List<Gradelist> getList() {
        return list;
    }

    public void setList(List<Gradelist> list) {
        this.list = list;
    }

    public class Gradelist {
        private String class_id;
        private String class_name;

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getClass_name() {
            return class_name;
        }

        public void setClass_name(String class_name) {
            this.class_name = class_name;
        }
    }
}
