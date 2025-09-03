package com.sigmabank.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatData {
    private String department;
    private String min;
    private String max;
    private String mid;
}
