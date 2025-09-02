package com.sigmabank.service;

import com.sigmabank.model.DepartmentData;
import com.sigmabank.model.Employee;
import com.sigmabank.model.Manager;
import com.sigmabank.state.ProcessingState;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {

    public Map<String, DepartmentData> buildDepartments(ProcessingState state) {
        Map<String, DepartmentData> departments = new LinkedHashMap<>();
        for (Map.Entry<String, Manager> e : state.departmentToManager.entrySet()) {
            String deptName = e.getKey();
            Manager manager = e.getValue();
            DepartmentData deptData = new DepartmentData(manager);
            List<Employee> emps = state.employeesByManager.getOrDefault(manager.getId(), Collections.emptyList());
            deptData.getEmployees().addAll(emps);
            departments.put(deptName, deptData);
        }
        return departments;
    }
}


