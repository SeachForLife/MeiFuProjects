package com.carl_yang.domain;

import java.util.List;

/**
 * Created by Administrator on 2017/3/17.
 */

public class GradeCombo {

    private List<Gradelist> list;

    public List<Gradelist> getList() {
        return list;
    }

    public void setList(List<Gradelist> list) {
        this.list = list;
    }

    public class Gradelist {
        private String grade_type;
        private String grade_code;
        private String grade_name;
        private String grade_id;

        public String getGrade_type() {
            return grade_type;
        }

        public void setGrade_type(String grade_type) {
            this.grade_type = grade_type;
        }

        public String getGrade_code() {
            return grade_code;
        }

        public void setGrade_code(String grade_code) {
            this.grade_code = grade_code;
        }

        public String getGrade_name() {
            return grade_name;
        }

        public void setGrade_name(String grade_name) {
            this.grade_name = grade_name;
        }

        public String getGrade_id() {
            return grade_id;
        }

        public void setGrade_id(String grade_id) {
            this.grade_id = grade_id;
        }
    }
}
