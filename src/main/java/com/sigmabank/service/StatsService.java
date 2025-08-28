package com.sigmabank.service;

import com.sigmabank.config.Config;
import com.sigmabank.enums.Output;
import com.sigmabank.model.DepartmentData;
import com.sigmabank.model.StatRow;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsService {

    public void outputStats(Map<String, DepartmentData> departments, Config config) {
        if (!config.isStat()) return;

        List<StatRow> rows = new ArrayList<>();
        List<String> deptNames = new ArrayList<>(departments.keySet());
        Collections.sort(deptNames, String::compareTo);
        for (String dept : deptNames) {
            DepartmentData dd = departments.get(dept);
            List<BigDecimal> salaries = dd.getEmployees().stream()
                    .map(e -> new BigDecimal(e.getSalary()))
                    .collect(Collectors.toList());
            BigDecimal min = salaries.stream().min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
            BigDecimal max = salaries.stream().max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
            BigDecimal mid;
            if (salaries.isEmpty()) {
                mid = BigDecimal.ZERO;
            } else {
                BigDecimal sum = salaries.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                mid = sum.divide(BigDecimal.valueOf(salaries.size()), 10, RoundingMode.HALF_UP);
            }
            rows.add(new StatRow(dept, roundUp2(min), roundUp2(max), roundUp2(mid)));
        }

        List<String> lines = new ArrayList<>();
        lines.add("department, min, max, mid");
        for (StatRow r : rows) {
            lines.add(r.getDepartment() + ", " + r.getMin() + ", " + r.getMax() + ", " + r.getMid());
        }

        if (config.getOutput() == Output.FILE) {
            Path p = Paths.get(config.getPath());
            try {
                if (p.getParent() != null) {
                    Files.createDirectories(p.getParent());
                }
                Files.write(p, lines, StandardCharsets.UTF_8);
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


