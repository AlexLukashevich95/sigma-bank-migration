package com.sigmabank.service;

import com.sigmabank.model.DepartmentData;
import com.sigmabank.model.Employee;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class OutputService {

    private static final String ERROR_LOG = "error.log";

    public void writeDepartments(Map<String, DepartmentData> departments) {
        for (Map.Entry<String, DepartmentData> e : departments.entrySet()) {
            String deptName = e.getKey();
            DepartmentData dd = e.getValue();
            Path out = Paths.get(deptName + ".sb");
            try (BufferedWriter w = Files.newBufferedWriter(out, StandardCharsets.UTF_8)) {
                w.write("Manager," + dd.getManager().getId() + "," + dd.getManager().getName() + "," + dd.getManager().getSalary());
                w.newLine();
                for (Employee emp : dd.getEmployees()) {
                    w.write("Employee," + emp.getId() + "," + emp.getName() + "," + emp.getSalary() + "," + emp.getManagerId());
                    w.newLine();
                }
            } catch (IOException ex) {
                System.err.println("Ошибка записи файла департамента " + out.getFileName() + ": " + ex.getMessage());
            }
        }
    }

    public void writeErrorLog(List<String> errorLines) {
        if (errorLines == null || errorLines.isEmpty()) return;
        try {
            Files.write(Paths.get(ERROR_LOG), errorLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Не удалось записать error.log: " + e.getMessage());
        }
    }
}


