package temperature;

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

    @Test
    void writesSummaryForValidTemperatureReadings() throws IOException {
        Path inputFile = tempDir.resolve("test_temps.csv");
        Files.write(inputFile, List.of(
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

        Path summaryFile = tempDir.resolve("test_temps.csv_summary.txt");
        assertTrue(Files.exists(summaryFile), "Summary file should be created");

        String content = Files.readString(summaryFile);
        assertTrue(content.contains("Total readings: 10"));
        assertTrue(content.contains("Valid readings: 10"));
        assertTrue(content.contains("Errors: 0"));
    }
}