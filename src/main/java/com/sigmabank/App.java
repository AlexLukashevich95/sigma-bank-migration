package com.sigmabank;

import com.sigmabank.config.Config;
import com.sigmabank.model.DepartmentData;
import com.sigmabank.service.*;
import com.sigmabank.service.parser.ParserService;
import com.sigmabank.service.parser.impl.EmployeeParserService;
import com.sigmabank.service.parser.impl.ManagerParserService;
import com.sigmabank.state.ProcessingState;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        Config config;
        try {
            config = Config.parse(args);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
            return;
        }

        ParserService parserService = new ParserService();
        DepartmentService departmentService = new DepartmentService();
        SortService sortService = new SortService();
        StatsService statsService = new StatsService();
        OutputService outputService = new OutputService();

        List<Path> inputFiles;
        try {
            inputFiles = parserService.findInputFiles(Paths.get("."));
        } catch (Exception e) {
            System.err.println("Не удалось прочитать текущую директорию: " + e.getMessage());
            System.exit(1);
            return;
        }

        if (inputFiles.isEmpty()) {
            System.out.println("Нет входных файлов с расширением .sb в текущей директории.");
        }

        ProcessingState state = parserService.parseFiles(inputFiles);

        Map<String, DepartmentData> departments = departmentService.buildDepartments(state);

        sortService.sortEmployees(departments, config);

        state.employeesByManager.entrySet().stream()
                .filter(e -> !state.managerIdToDepartment.containsKey(e.getKey()))
                .flatMap(e -> e.getValue().stream())
                .map(emp -> emp.getRawLine())
                .forEach(state.errorLines::add);

        outputService.writeDepartments(departments);

        statsService.outputStats(departments, config);

        outputService.writeErrorLog(state.errorLines);
    }
}


