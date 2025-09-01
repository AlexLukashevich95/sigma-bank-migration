package com.sigmabank.model;

public class StatData {
    private String department;
    private String min;
    private String max;
    private String mid;

    public StatData(String department, String min, String max, String mid) {
        this.department = department;
        this.min = min;
        this.max = max;
        this.mid = mid;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}
