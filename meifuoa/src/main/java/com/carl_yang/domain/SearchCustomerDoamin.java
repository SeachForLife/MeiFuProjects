package com.carl_yang.domain;

import java.util.List;

/**
 * Created by carl_yang on 2017/3/27.
 */

public class SearchCustomerDoamin {

    private String success;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private List<CustomerList> list;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<CustomerList> getList() {
        return list;
    }

    public void setList(List<CustomerList> list) {
        this.list = list;
    }

    public class CustomerList {
        private String customer_address;
        private String customer_name;
        private String customer_id;
        private String emp_name;

        public String getEmp_name() {
            return emp_name;
        }

        public void setEmp_name(String emp_name) {
            this.emp_name = emp_name;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getCustomer_address() {
            return customer_address;
        }

        public void setCustomer_address(String customer_address) {
            this.customer_address = customer_address;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }
    }
}
