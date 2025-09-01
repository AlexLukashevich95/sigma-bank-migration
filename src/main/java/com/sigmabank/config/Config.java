package com.sigmabank.config;

import com.sigmabank.enums.Order;
import com.sigmabank.enums.Output;
import com.sigmabank.enums.SortBy;

public class Config {

    private final SortBy sortBy;
    private final Order order;
    private final boolean stat;
    private final Output output;
    private final String path;

    Config(SortBy sortBy, Order order, boolean stat, Output output, String path) {
        this.sortBy = sortBy;
        this.order = order;
        this.stat = stat;
        this.output = output;
        this.path = path;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public Order getOrder() {
        return order;
    }

    public boolean isStat() {
        return stat;
    }

    public Output getOutput() {
        return output;
    }

    public String getPath() {
        return path;
    }

    public static Config parse(String[] args) {
        SortBy sortBy = null;
        Order order = null;
        boolean stat = false;
        Output output = Output.CONSOLE;
        String path = null;

        for (String arg : args) {
            if (arg == null) continue;
            String a = arg.trim();
            if (a.isEmpty()) continue;

            if (a.startsWith("--sort=") || a.startsWith("-s=")) {
                String val = a.substring(a.indexOf('=') + 1).trim();
                sortBy = switch (val) {
                    case "name" -> SortBy.NAME;
                    case "salary" -> SortBy.SALARY;
                    default -> throw new IllegalArgumentException("Неизвестный тип сортировки: " + val);
                };
                continue;
            }
            if (a.startsWith("--order=")) {
                String val = a.substring("--order=".length()).trim();
                order = switch (val) {
                    case "asc" -> Order.ASC;
                    case "desc" -> Order.DESC;
                    default -> throw new IllegalArgumentException("Неизвестный порядок сортировки: " + val);
                };
                continue;
            }
            if (a.equals("--stat")) {
                stat = true;
                continue;
            }
            if (a.startsWith("--output=") || a.startsWith("-o=")) {
                String val = a.substring(a.indexOf('=') + 1).trim();
                output = switch (val) {
                    case "console" -> Output.CONSOLE;
                    case "file" -> Output.FILE;
                    default -> throw new IllegalArgumentException("Неизвестный способ вывода: " + val);
                };
                continue;
            }
            if (a.startsWith("--path=")) {
                path = a.substring("--path=".length()).trim();
                continue;
            }

            throw new IllegalArgumentException("Неизвестный параметр: " + a);
        }

        validateConfig(sortBy, order, stat, output, path);

        return new Config(sortBy, order, stat, output, path);
    }

    private static void validateConfig(SortBy sortBy, Order order, boolean stat,
                                       Output output, String path) {
        if (sortBy == null && order != null) {
            throw new IllegalArgumentException("Параметр --order допустим только вместе с --sort.");
        }
        if (sortBy != null && order == null) {
            throw new IllegalArgumentException("Для сортировки необходимо указать --order=asc|desc.");
        }

        if (!stat) {
            if (output == Output.FILE || path != null) {
                throw new IllegalArgumentException("Параметры статистики допустимы только при указании --stat.");
            }
        } else {
            if (output == Output.FILE && (path == null || path.isEmpty())) {
                throw new IllegalArgumentException("--output=file требует указания --path=<путь к файлу>.");
            }
            if (path != null && output != Output.FILE) {
                throw new IllegalArgumentException("--path задан без --output=file.");
            }
        }
    }
}
