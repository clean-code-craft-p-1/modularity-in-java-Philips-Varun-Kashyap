package temperature;

import java.util.ArrayList;
import java.util.List;

final class TemperatureBatchResult {
    private final List<Double> temperatures = new ArrayList<>();
    private final List<String> badLines = new ArrayList<>();
    private int errors;

    void addTemperature(double temperature) {
        temperatures.add(temperature);
    }

    void addBadLine(int lineNumber, String line) {
        errors = TemperatureBatchProcessorUtils.recordBadLine(badLines, errors, lineNumber, line);
    }

    boolean hasValidTemperatures() {
        return !temperatures.isEmpty();
    }

    List<Double> temperatures() {
        return temperatures;
    }

    List<String> badLines() {
        return badLines;
    }

    int errors() {
        return errors;
    }
}