package com.sigmabank.service.parser;

import com.sigmabank.state.ProcessingState;

import java.util.Set;

public interface EntityParserService {
    boolean canParse(String role);
    void parse(String[] inputData, String rawLine, ProcessingState state, Set<Integer> globalIds);
}
