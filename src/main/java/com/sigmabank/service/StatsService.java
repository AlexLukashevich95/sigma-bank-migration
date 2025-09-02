package com.sigmabank.service;

import com.sigmabank.config.Config;
import com.sigmabank.enums.Output;
import com.sigmabank.model.DepartmentData;
import com.sigmabank.model.StatData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatsService {

    public void outputStats(Map<String, DepartmentData> departments, Config config) {
        if (!config.isStat()) return;

        List<StatData> stats = new ArrayList<>();
        List<String> deptNames = new ArrayList<>(departments.keySet());
        Collections.sort(deptNames, String::compareTo);
        for (String dept : deptNames) {
            DepartmentData deptData = departments.get(dept);
            List<BigDecimal> salaries = deptData.getEmployees().stream()
                    .map(e -> new BigDecimal(e.getSalary()))
                    .collect(Collectors.toList());
            BigDecimal salaryMin = salaries.stream().min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
            BigDecimal salaryMax = salaries.stream().max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
            BigDecimal salaryMid;
            if (salaries.isEmpty()) {
                salaryMid = BigDecimal.ZERO;
            } else {
                BigDecimal sum = salaries.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                salaryMid = sum.divide(BigDecimal.valueOf(salaries.size()), 10, RoundingMode.HALF_UP);
            }
            stats.add(new StatData(dept, roundUp2(salaryMin), roundUp2(salaryMax), roundUp2(salaryMid)));
        }

        List<String> lines = new ArrayList<>();
        lines.add("department, min, max, mid");
        for (StatData statData : stats) {
            lines.add(statData.getDepartment() + ", " + statData.getMin() + ", " + statData.getMax() + ", " + statData.getMid());
        }

        if (config.getOutput() == Output.FILE) {
            Path path = Paths.get(config.getPath());
            try {
                if (path.getParent() != null) {
                    Files.createDirectories(path.getParent());
                }
                Files.write(path, lines, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException("Не удалось записать файл статистики: " + e.getMessage(), e);
            }
        } else {
            for (String s : lines) {
                System.out.println(s);
            }
        }
    }

    private String roundUp2(BigDecimal v) {
        return v.setScale(2, RoundingMode.CEILING).toPlainString();
    }
}


