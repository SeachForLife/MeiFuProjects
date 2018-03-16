package com.carl_yang.domain;

/**
 * Created by carl_yang on 2017/3/31.
 */

public class TrajcectoryStartAndEndMessage {

    private String t_date;
    private String trajectory_id;
    private String t_time;
    private String type;

    public String getT_date() {
        return t_date;
    }

    public void setT_date(String t_date) {
        this.t_date = t_date;
    }

    public String getTrajectory_id() {
        return trajectory_id;
    }

    public void setTrajectory_id(String trajectory_id) {
        this.trajectory_id = trajectory_id;
    }

    public String getT_time() {
        return t_time;
    }

    public void setT_time(String t_time) {
        this.t_time = t_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
