package com.sigmabank.model;

public class Manager {
    private int id;
    private String name;
    private String salary;
    private String departmentName;

    public Manager(int id, String name, String salary, String departmentName) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
