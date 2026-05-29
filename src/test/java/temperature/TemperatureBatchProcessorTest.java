package temperature;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TemperatureBatchProcessorTest {
    @TempDir
    Path tempDir;

    private Path writeInputFile(String fileName, List<String> lines) throws IOException {
        Path inputFile = tempDir.resolve(fileName);
        Files.write(inputFile, lines);
        return inputFile;
    }

    private Path summaryFileFor(Path inputFile) {
        return inputFile.resolveSibling(inputFile.getFileName() + "_summary.txt");
    }

    @Test
    void writesSummaryForValidTemperatureReadings() throws IOException {
        Path inputFile = writeInputFile("test_temps.csv", List.of(
                "09:15:30,23.5",
                "09:16:00,24.1",
                "09:16:30,22.8",
                "09:17:00,25.3",
                "09:17:30,23.9",
                "09:18:00,24.7",
                "09:18:30,22.4",
                "09:19:00,26.1",
                "09:19:30,23.2",
                "09:20:00,25.0"));

        TemperatureBatchProcessor.processBatch(inputFile.toString());

        Path summaryFile = summaryFileFor(inputFile);
        assertTrue(Files.exists(summaryFile), "Summary file should be created");

        String content = Files.readString(summaryFile);
        assertTrue(content.contains("Total readings: 10"));
        assertTrue(content.contains("Valid readings: 10"));
        assertTrue(content.contains("Errors: 0"));
    }

    @Test
    void writesInvalidLineDetailsForMixedInput() throws IOException {
        Path inputFile = writeInputFile("mixed_temps.csv", List.of(
                "09:15:30,23.5",
                "bad line",
                "09:16:00,not-a-number",
                "09:17:00,250",
                "09:18:00,24.0"));

        TemperatureBatchProcessor.processBatch(inputFile.toString());

        String content = Files.readString(summaryFileFor(inputFile));
        assertTrue(content.contains("Total readings: 5"));
        assertTrue(content.contains("Valid readings: 2"));
        assertTrue(content.contains("Errors: 3"));
        assertTrue(content.contains("Invalid lines:"));
        assertTrue(content.contains("Line 2: bad line"));
        assertTrue(content.contains("Line 3: 09:16:00,not-a-number"));
        assertTrue(content.contains("Line 4: 09:17:00,250"));
    }

    @Test
    void doesNotCreateSummaryWhenNoValidDataExists() throws IOException {
        Path inputFile = writeInputFile("invalid_temps.csv", List.of(
                "bad line",
                "10:00,22.0",
                "10:00:00,500"));

        TemperatureBatchProcessor.processBatch(inputFile.toString());

        assertFalse(Files.exists(summaryFileFor(inputFile)),
                "Summary file should not be created when all data is invalid");
    }

    @Test
    void doesNotCreateSummaryWhenInputFileIsMissing() {
        Path missingFile = tempDir.resolve("missing.csv");

        TemperatureBatchProcessor.processBatch(missingFile.toString());

        assertFalse(Files.exists(summaryFileFor(missingFile)),
                "Summary file should not be created for a missing input file");
    }

    @Test
    void writesExactTemperatureStatistics() throws IOException {
        Path inputFile = writeInputFile("stats_temps.csv", List.of(
                "09:15:30,10.0",
                "09:16:00,20.0",
                "09:16:30,30.0",
                "",
                "09:17:00,40.0"));

        TemperatureBatchProcessor.processBatch(inputFile.toString());

        String content = Files.readString(summaryFileFor(inputFile));
        assertTrue(content.contains("Total readings: 5"));
        assertTrue(content.contains("Valid readings: 4"));
        assertTrue(content.contains("Errors: 0"));
        assertTrue(content.contains("Max temperature: 40.00"));
        assertTrue(content.contains("Min temperature: 10.00"));
        assertTrue(content.contains("Average temperature: 25.00"));
    }
}