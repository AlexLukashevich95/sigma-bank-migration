package com.sigmabank.service;

import com.sigmabank.config.Config;
import com.sigmabank.enums.Order;
import com.sigmabank.enums.SortBy;
import com.sigmabank.model.DepartmentData;
import com.sigmabank.model.Employee;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

@Service
public class SortService {

    public void sortEmployees(Map<String, DepartmentData> departments, Config config) {
        Optional.ofNullable(config.getSortBy())
                .map(this::createComparator)
                .map(comp -> config.getOrder() == Order.DESC ? comp.reversed() : comp)
                .ifPresent(comparator ->
                        departments.values().forEach(dept -> dept.getEmployees().sort(comparator))
                );
    }

    private Comparator<Employee> createComparator(SortBy sortBy) {
        return switch (sortBy) {
            case NAME -> Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER);
            case SALARY -> Comparator.comparing(e -> new BigDecimal(e.getSalary()));
        };
    }
}


