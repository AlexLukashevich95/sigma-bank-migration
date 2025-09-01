# Sigma Bank Migration (.sb sorter)

Консольное Java-приложение для обработки файлов `.sb` в текущей директории: группировка по департаментам, сортировка сотрудников, статистика, лог ошибок.

## Требования
- Java 17
- Maven 3.8+

## Сборка
```bash
mvn -q -DskipTests package
```
Артефакт: `target/sigma-bank-migration-1.0.0.jar`

## Запуск
```bash
java -jar target/sigma-bank-migration-1.0.0.jar --sort=name --order=asc --stat
java -jar target/sigma-bank-migration-1.0.0.jar -s=salary --order=desc --stat -o=file --path=output/statistics.txt
java -jar target/sigma-bank-migration-1.0.0.jar --stat
```

## Параметры
- `--sort`/`-s`: `name` | `salary`
- `--order`: `asc` | `desc` (только с `--sort`)
- `--stat`: включить вывод статистики
- `--output`/`-o`: `console` | `file` (по умолчанию `console`)
- `--path=<путь>`: обязателен при `--output=file`

Некорректные параметры и строки данных добавляются в `error.log`.



