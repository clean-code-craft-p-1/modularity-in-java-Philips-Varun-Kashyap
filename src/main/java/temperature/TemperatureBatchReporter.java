package temperature;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

final class TemperatureBatchReporter {
    private TemperatureBatchReporter() {
    }

    static void printConsoleSummary(int totalReadings, TemperatureBatchResult result) {
        double[] stats = calculateStats(result.temperatures());
        PrintWriter out = new PrintWriter(System.out, true);

        out.println("============================================================");
        out.println("Temperature Analysis Summary");
        out.println("============================================================");
        TemperatureBatchProcessorUtils.writeSummary(out, null, totalReadings,
                result.temperatures().size(), result.errors(), stats[0], stats[1], stats[2]);
        out.println("------------------------------------------------------------");
        TemperatureBatchProcessorUtils.writeInvalidLines(out, result.badLines(), false);
    }

    static void writeSummaryFile(String filename, int totalReadings, TemperatureBatchResult result) {
        double[] stats = calculateStats(result.temperatures());
        String outName = filename + "_summary.txt";

        try (PrintWriter out = new PrintWriter(new FileWriter(outName))) {
            out.println("Temperature Analysis Summary");
            out.println("==================================================");
            TemperatureBatchProcessorUtils.writeSummary(out, filename, totalReadings,
                    result.temperatures().size(), result.errors(), stats[0], stats[1], stats[2]);
            out.println("------------------------------------------------------------");
            TemperatureBatchProcessorUtils.writeInvalidLines(out, result.badLines(), true);
            System.out.println("Report saved to " + outName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static double[] calculateStats(List<Double> temperatures) {
        double maxTemp = Collections.max(temperatures);
        double minTemp = Collections.min(temperatures);
        double avgTemp = temperatures.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        return new double[] {maxTemp, minTemp, avgTemp};
    }
}