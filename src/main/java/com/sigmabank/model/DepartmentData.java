package com.sigmabank.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DepartmentData {
    private Manager manager;
    private List<Employee> employees = new ArrayList<>();

    public DepartmentData(Manager manager) {
        this.manager = manager;
    }
}


