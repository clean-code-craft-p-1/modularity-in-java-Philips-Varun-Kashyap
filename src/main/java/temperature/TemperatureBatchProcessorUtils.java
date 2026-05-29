package temperature;

import java.io.PrintWriter;
import java.util.List;

final class TemperatureBatchProcessorUtils {
    private TemperatureBatchProcessorUtils() {
    }

    static int recordBadLine(List<String> badLines, int errors, int lineNumber, String line) {
        badLines.add("  Line " + lineNumber + ": " + line);
        return errors + 1;
    }

    static void writeSummary(PrintWriter out, String filename, int totalReadings,
            int validReadings, int errors, double maxTemp, double minTemp, double avgTemp) {
        if (filename != null) {
            out.println("File analyzed: " + filename);
        }
        out.println("Total readings: " + totalReadings);
        out.println("Valid readings: " + validReadings);
        out.println("Errors: " + errors);
        out.printf("Max temperature: %.2f%n", maxTemp);
        out.printf("Min temperature: %.2f%n", minTemp);
        out.printf("Average temperature: %.2f%n", avgTemp);
    }

    static void writeInvalidLines(PrintWriter out, List<String> badLines, boolean addLeadingBlankLine) {
        if (badLines.isEmpty()) {
            return;
        }
        if (addLeadingBlankLine) {
            out.println();
        }
        out.println("Invalid lines:");
        for (String badLine : badLines) {
            out.println(badLine);
        }
    }
}