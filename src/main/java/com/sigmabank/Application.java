package com.sigmabank;

import com.sigmabank.config.Config;
import com.sigmabank.model.DepartmentData;
import com.sigmabank.service.DepartmentService;
import com.sigmabank.service.OutputService;
import com.sigmabank.service.SortService;
import com.sigmabank.service.StatsService;
import com.sigmabank.service.parser.ParserService;
import com.sigmabank.state.ProcessingState;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final ParserService parserService;
    private final DepartmentService departmentService;
    private final SortService sortService;
    private final StatsService statsService;
    private final OutputService outputService;

    public Application(ParserService parserService,
                       DepartmentService departmentService,
                       SortService sortService,
                       StatsService statsService,
                       OutputService outputService) {
        this.parserService = parserService;
        this.departmentService = departmentService;
        this.sortService = sortService;
        this.statsService = statsService;
        this.outputService = outputService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        Config config;
        try {
            config = Config.parse(args);
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        List<Path> inputFiles;
        try {
            inputFiles = parserService.findInputFiles(Paths.get("."));
        } catch (Exception e) {
            System.err.println("Не удалось прочитать текущую директорию: " + e.getMessage());
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


