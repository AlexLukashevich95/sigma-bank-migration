package com.sigmabank.service.parser.impl;

import com.sigmabank.model.Employee;
import com.sigmabank.service.parser.EntityParserService;
import com.sigmabank.state.ProcessingState;

import java.util.LinkedList;
import java.util.Set;

import static com.sigmabank.utils.Utility.*;

public class EmployeeParserService implements EntityParserService {

    public EmployeeParserService() {
    }

    @Override
    public boolean canParse(String role) {
        return "Employee".equals(role);
    }

    @Override
    public void parse(String[] inputData, String rawLine, ProcessingState state, Set<Integer> globalIds) {
        String idStr = inputData[1].trim();
        String name = inputData[2].trim();
        String salaryStr = inputData[3].trim();
        String managerIdStr = inputData[4].trim();

        if (!isValidData(idStr, name, salaryStr, managerIdStr)) {
            state.errorLines.add(rawLine);
            return;
        }

        int id = Integer.parseInt(idStr);
        int managerId = Integer.parseInt(managerIdStr);

        if (globalIds.contains(id)) {
            state.errorLines.add(rawLine);
            return;
        }

        globalIds.add(id);
        Employee emp = new Employee(id, name, salaryStr, managerId, rawLine);
        state.employeesByManager.computeIfAbsent(managerId, k -> new LinkedList<>()).add(emp);
    }

}
