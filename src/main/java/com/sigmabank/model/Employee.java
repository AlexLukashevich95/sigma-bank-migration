package com.sigmabank.model;

public class Employee {
    private int id;
    private String name;
    private String salary;
    private int managerId;
    private String rawLine;

    public Employee(int id, String name, String salary, int managerId, String rawLine) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.managerId = managerId;
        this.rawLine = rawLine;
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

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getRawLine() {
        return rawLine;
    }

    public void setRawLine(String rawLine) {
        this.rawLine = rawLine;
    }
}
