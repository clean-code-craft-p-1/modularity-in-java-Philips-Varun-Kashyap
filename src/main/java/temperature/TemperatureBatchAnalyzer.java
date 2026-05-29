package temperature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

final class TemperatureBatchAnalyzer {
    private TemperatureBatchAnalyzer() {
    }

    static List<String> readLines(String filename) {
        try {
            return Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Error: File not found.");
            return null;
        }
    }

    static TemperatureBatchResult collectValidTemperatures(List<String> lines) {
        TemperatureBatchResult result = new TemperatureBatchResult();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            Double temperature = TemperatureReadingValidator.validate(line);
            if (temperature == null) {
                result.addBadLine(i + 1, line);
                continue;
            }

            result.addTemperature(temperature);
        }
        return result;
    }
}