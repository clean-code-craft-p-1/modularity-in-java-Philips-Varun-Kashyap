package temperature;

import java.util.List;

public final class TemperatureBatchProcessor {
    private TemperatureBatchProcessor() {
    }

    public static void processBatch(String filename) {
        List<String> lines = TemperatureBatchAnalyzer.readLines(filename);
        if (lines == null) {
            return;
        }

        TemperatureBatchResult result = TemperatureBatchAnalyzer.collectValidTemperatures(lines);
        if (!result.hasValidTemperatures()) {
            System.out.println("No valid temperature data found.");
            return;
        }

        TemperatureBatchReporter.printConsoleSummary(lines.size(), result);
        TemperatureBatchReporter.writeSummaryFile(filename, lines.size(), result);
    }
}