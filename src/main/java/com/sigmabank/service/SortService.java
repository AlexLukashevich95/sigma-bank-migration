package com.sigmabank.service;

import com.sigmabank.config.Config;
import com.sigmabank.enums.Order;
import com.sigmabank.enums.SortBy;
import com.sigmabank.model.DepartmentData;
import com.sigmabank.model.Employee;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

public class SortService {

    public void sortEmployees(Map<String, DepartmentData> departments, Config config) {
        if (config.getSortBy() == null) return;

        Comparator<Employee> cmp;
        if (config.getSortBy() == SortBy.NAME) {
            cmp = Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER);
        } else {
            cmp = Comparator.comparing((Employee e) -> new BigDecimal(e.getSalary()));
        }
        if (config.getOrder() == Order.DESC) {
            cmp = cmp.reversed();
        }
        for (DepartmentData dd : departments.values()) {
            dd.getEmployees().sort(cmp);
        }
    }
}


