package com.sigmabank.model;

import java.util.ArrayList;
import java.util.List;

public class DepartmentData {
    private Manager manager;
    private List<Employee> employees = new ArrayList<>();

    public DepartmentData(Manager manager) {
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}


