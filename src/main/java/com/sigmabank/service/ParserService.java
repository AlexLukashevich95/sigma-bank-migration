package com.sigmabank.service;

import com.sigmabank.model.Employee;
import com.sigmabank.model.Manager;
import com.sigmabank.state.ProcessingState;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParserService {

    public List<Path> findInputFiles(Path directory) throws IOException {
        try (Stream<Path> stream = Files.list(directory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".sb"))
                    .collect(Collectors.toList());
        }
    }

    public ProcessingState parseFiles(List<Path> inputFiles) {
        ProcessingState state = new ProcessingState();
        for (Path file : inputFiles) {
            List<String> lines;
            try {
                lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                state.errorLines.add("FILE_READ_ERROR:" + file.getFileName());
                continue;
            }

            for (String raw : lines) {
                String line = raw == null ? "" : raw.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = splitCsv(line);
                if (parts.length != 5) {
                    state.errorLines.add(raw);
                    continue;
                }
                String role = parts[0].trim();
                String idStr = parts[1].trim();
                String name = parts[2].trim();
                String salaryStr = parts[3].trim();
                String last = parts[4].trim();

                if (role.equals("Manager")) {
                    if (!isPositiveDecimal(salaryStr) || name.isEmpty() || last.isEmpty() || !isInteger(idStr)) {
                        state.errorLines.add(raw);
                        continue;
                    }
                    int id = Integer.parseInt(idStr);
                    if (state.globalIds.contains(id)) {
                        state.errorLines.add(raw);
                        continue;
                    }
                    state.globalIds.add(id);
                    Manager m = new Manager(id, name, salaryStr, last);
                    Manager existing = state.departmentToManager.putIfAbsent(m.getDepartmentName(), m);
                    if (existing != null && existing.getId() != m.getId()) {
                        state.errorLines.add(raw);
                    } else {
                        state.managerIdToDepartment.put(m.getId(), m.getDepartmentName());
                    }
                } else if (role.equals("Employee")) {
                    if (!isPositiveDecimal(salaryStr) || name.isEmpty() || !isInteger(idStr) || !isInteger(last)) {
                        state.errorLines.add(raw);
                        continue;
                    }
                    int id = Integer.parseInt(idStr);
                    if (state.globalIds.contains(id)) {
                        state.errorLines.add(raw);
                        continue;
                    }
                    state.globalIds.add(id);
                    int managerId = Integer.parseInt(last);
                    Employee emp = new Employee(id, name, salaryStr, managerId, raw);
                    state.employeesByManager.computeIfAbsent(managerId, k -> new java.util.LinkedList<>()).add(emp);
                } else {
                    state.errorLines.add(raw);
                }
            }
        }
        return state;
    }

    private String[] splitCsv(String line) {
        String[] rawParts = line.split(",");
        String[] parts = new String[rawParts.length];
        for (int i = 0; i < rawParts.length; i++) {
            parts[i] = rawParts[i] == null ? "" : rawParts[i].trim();
        }
        return parts;
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isPositiveDecimal(String s) {
        if (s == null || s.isEmpty()) return false;
        try {
            BigDecimal v = new BigDecimal(s);
            return v.compareTo(java.math.BigDecimal.ZERO) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}


