package temperature;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TemperatureBatchProcessor {
    private TemperatureBatchProcessor() {
    }

    public static void processBatch(String filename) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            System.out.println("Error: File not found.");
            return;
        }

        List<Double> temps = new ArrayList<>();
        int errors = 0;
        List<String> badLines = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length != 2) {
                errors++;
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            String timestamp = parts[0].strip();
            String value = parts[1].strip();

            if (timestamp.split(":").length != 3) {
                errors++;
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            double temp;
            try {
                temp = Double.parseDouble(value);
            } catch (NumberFormatException e) {
                errors++;
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            if (temp < -100 || temp > 200) {
                errors++;
                badLines.add("  Line " + (i + 1) + ": " + line);
                continue;
            }

            temps.add(temp);
        }

        if (temps.isEmpty()) {
            System.out.println("No valid temperature data found.");
            return;
        }

        double maxTemp = Collections.max(temps);
        double minTemp = Collections.min(temps);
        double avgTemp = temps.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        System.out.println("============================================================");
        System.out.println("Temperature Analysis Summary");
        System.out.println("============================================================");
        System.out.println("Total readings: " + lines.size());
        System.out.println("Valid readings: " + temps.size());
        System.out.println("Errors: " + errors);
        System.out.println("------------------------------------------------------------");
        System.out.printf("Max temperature: %.2f%n", maxTemp);
        System.out.printf("Min temperature: %.2f%n", minTemp);
        System.out.printf("Average temperature: %.2f%n", avgTemp);
        System.out.println("------------------------------------------------------------");

        if (errors > 0) {
            System.out.println("Invalid lines:");
            for (String badLine : badLines) {
                System.out.println(badLine);
            }
        }

        String outName = filename + "_summary.txt";
        try (PrintWriter out = new PrintWriter(new FileWriter(outName))) {
            out.println("Temperature Analysis Summary");
            out.println("==================================================");
            out.println("File analyzed: " + filename);
            out.println("Total readings: " + lines.size());
            out.println("Valid readings: " + temps.size());
            out.println("Errors: " + errors);
            out.printf("Max temperature: %.2f%n", maxTemp);
            out.printf("Min temperature: %.2f%n", minTemp);
            out.printf("Average temperature: %.2f%n", avgTemp);
            out.println("------------------------------------------------------------");
            if (errors > 0) {
                out.println();
                out.println("Invalid lines:");
                for (String badLine : badLines) {
                    out.println(badLine);
                }
            }
            System.out.println("Report saved to " + outName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
}