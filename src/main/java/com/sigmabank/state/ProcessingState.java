package com.sigmabank.state;

import com.sigmabank.model.Employee;
import com.sigmabank.model.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProcessingState {
    public final Map<String, Manager> departmentToManager = new LinkedHashMap<>();
    public final Map<Integer, String> managerIdToDepartment = new HashMap<>();
    public final Map<Integer, List<Employee>> employeesByManager = new HashMap<>();
    public final Set<Integer> globalIds = new HashSet<>();
    public final List<String> errorLines = new ArrayList<>();
}



