package temperature;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TemperatureBatchProcessorSummaryTest {
    @TempDir
    Path tempDir;

    @Test
    void writesSummaryForValidTemperatureReadings() throws IOException {
        String content = TemperatureBatchProcessorTestHelper.writeProcessAndReadSummary(tempDir, "test_temps.csv",
                "09:15:30,23.5",
                "09:16:00,24.1",
                "09:16:30,22.8",
                "09:17:00,25.3",
                "09:17:30,23.9",
                "09:18:00,24.7",
                "09:18:30,22.4",
                "09:19:00,26.1",
                "09:19:30,23.2",
            "09:20:00,25.0");

        TemperatureBatchProcessorTestHelper.assertContainsAll(content,
            "Total readings: 10",
            "Valid readings: 10",
            "Errors: 0");
    }

    @Test
    void writesExactTemperatureStatistics() throws IOException {
        String content = TemperatureBatchProcessorTestHelper.writeProcessAndReadSummary(tempDir, "stats_temps.csv",
                "09:15:30,10.0",
                "09:16:00,20.0",
                "09:16:30,30.0",
                "",
            "09:17:00,40.0");

        TemperatureBatchProcessorTestHelper.assertContainsAll(content,
            "Total readings: 5",
            "Valid readings: 4",
            "Errors: 0",
            "Max temperature: 40.00",
            "Min temperature: 10.00",
            "Average temperature: 25.00");
    }
}