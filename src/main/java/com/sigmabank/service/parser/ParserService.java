package com.sigmabank.service.parser;

import com.sigmabank.service.parser.impl.EmployeeParserService;
import com.sigmabank.service.parser.impl.ManagerParserService;
import com.sigmabank.state.ProcessingState;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sigmabank.utils.Utility.splitCsv;

@Service
public class ParserService {
    private final List<EntityParserService> parsers;

    public ParserService(List<EntityParserService> parsers) {
        this.parsers = new ArrayList<>(parsers);
    }

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
            processFile(file, state);
        }

        return state;
    }

    private void processFile(Path file, ProcessingState state) {
        List<String> lines;
        try {
            lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            state.errorLines.add("FILE_READ_ERROR:" + file.getFileName());
            return;
        }

        for (String line : lines) {
            processLine(line, state);
        }
    }

    private void processLine(String raw, ProcessingState state) {
        String line = raw == null ? "" : raw.trim();
        if (line.isEmpty()) return;

        String[] inputData = splitCsv(line);
        if (inputData.length != 5) {
            state.errorLines.add(raw);
            return;
        }

        String role = inputData[0].trim();
        boolean parsed = false;

        for (EntityParserService parser : parsers) {
            if (parser.canParse(role)) {
                parser.parse(inputData, raw, state, state.globalIds);
                parsed = true;
                break;
            }
        }

        if (!parsed) {
            state.errorLines.add(raw);
        }
    }
}