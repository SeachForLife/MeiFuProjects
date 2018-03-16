package com.carl_yang.domain;

import java.util.List;

/**
 * Created by carl_yang on 2017/3/31.
 */

public class TrajectoryStartAndEndDomain {

    private String success;
    private List<TrajcectoryStartAndEndMessage> list;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<TrajcectoryStartAndEndMessage> getList() {
        return list;
    }

    public void setList(List<TrajcectoryStartAndEndMessage> list) {
        this.list = list;
    }
}
