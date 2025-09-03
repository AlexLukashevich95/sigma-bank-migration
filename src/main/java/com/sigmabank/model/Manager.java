package com.sigmabank.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Manager {
    private int id;
    private String name;
    private String salary;
    private String departmentName;
}
