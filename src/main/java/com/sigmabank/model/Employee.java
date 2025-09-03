package com.sigmabank.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    private int id;
    private String name;
    private String salary;
    private int managerId;
    private String rawLine;
}
