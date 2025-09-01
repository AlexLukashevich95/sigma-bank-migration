package com.sigmabank.service.parser.impl;

import com.sigmabank.model.Manager;
import com.sigmabank.service.parser.EntityParserService;
import com.sigmabank.state.ProcessingState;

import java.util.Set;

import static com.sigmabank.utils.Utility.isValidData;

public class ManagerParserService implements EntityParserService {
    public ManagerParserService() {
    }

    @Override
    public boolean canParse(String role) {
        return "Manager".equals(role);
    }

    @Override
    public void parse(String[] inputData, String rawLine, ProcessingState state, Set<Integer> globalIds) {
        String idStr = inputData[1].trim();
        String name = inputData[2].trim();
        String salaryStr = inputData[3].trim();
        String department = inputData[4].trim();

        if (!isValidData(idStr, name, salaryStr, department)) {
            state.errorLines.add(rawLine);
            return;
        }

        int id = Integer.parseInt(idStr);
        if (globalIds.contains(id)) {
            state.errorLines.add(rawLine);
            return;
        }

        Manager manager = new Manager(id, name, salaryStr, department);
        Manager existing = state.departmentToManager.putIfAbsent(department, manager);
        if (existing != null && existing.getId() != manager.getId()) {
            state.errorLines.add(rawLine);
        } else {
            globalIds.add(id);
            state.managerIdToDepartment.put(manager.getId(), department);
        }
    }
}
