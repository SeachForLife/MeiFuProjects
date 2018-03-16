package com.carl_yang.domain;


/**
 * Created by carl_yang on 2017/3/27.
 */

public class LoginDoamin {

    private String success;
    private String msg;
    private UserMessage user;
    private String version_status;
    private String version_code;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserMessage getUser() {
        return user;
    }

    public void setUser(UserMessage user) {
        this.user = user;
    }

    public String getVersion_status() {
        return version_status;
    }

    public void setVersion_status(String version_status) {
        this.version_status = version_status;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public class UserMessage {
        private String create_time;
        private String department_id;
        private String emp_id;
        private String emp_name;
        private String is_login;
        private String pemp_id;
        private String phone;
        private String role_id;
        private String status;
        private String add_customer;

        public String getAdd_customer() {
            return add_customer;
        }

        public void setAdd_customer(String add_customer) {
            this.add_customer = add_customer;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public void setDepartment_id(String department_id) {
            this.department_id = department_id;
        }

        public String getEmp_name() {
            return emp_name;
        }

        public void setEmp_name(String emp_name) {
            this.emp_name = emp_name;
        }

        public String getIs_login() {
            return is_login;
        }

        public void setIs_login(String is_login) {
            this.is_login = is_login;
        }

        public String getPemp_id() {
            return pemp_id;
        }

        public void setPemp_id(String pemp_id) {
            this.pemp_id = pemp_id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRole_id() {
            return role_id;
        }

        public void setRole_id(String role_id) {
            this.role_id = role_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getEmp_id() {
            return emp_id;
        }

        public void setEmp_id(String emp_id) {
            this.emp_id = emp_id;
        }
    }
}
