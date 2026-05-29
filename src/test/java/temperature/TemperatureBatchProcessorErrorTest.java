package temperature;

import java.io.IOException;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TemperatureBatchProcessorErrorTest {
    @TempDir
    Path tempDir;

    @Test
    void writesInvalidLineDetailsForMixedInput() throws IOException {
        String content = TemperatureBatchProcessorTestHelper.writeProcessAndReadSummary(tempDir, "mixed_temps.csv",
                "09:15:30,23.5",
                "bad line",
                "09:16:00,not-a-number",
                "09:17:00,250",
            "09:18:00,24.0");

        TemperatureBatchProcessorTestHelper.assertContainsAll(content,
            "Total readings: 5",
            "Valid readings: 2",
            "Errors: 3",
            "Invalid lines:",
            "Line 2: bad line",
            "Line 3: 09:16:00,not-a-number",
            "Line 4: 09:17:00,250");
    }

    @Test
    void doesNotCreateSummaryWhenNoValidDataExists() throws IOException {
        Path inputFile = TemperatureBatchProcessorTestHelper.writeAndProcess(tempDir, "invalid_temps.csv",
                "bad line",
                "10:00,22.0",
            "10:00:00,500");

        TemperatureBatchProcessorTestHelper.assertSummaryNotCreated(inputFile,
                "Summary file should not be created when all data is invalid");
    }

    @Test
    void doesNotCreateSummaryWhenInputFileIsMissing() {
        Path missingFile = tempDir.resolve("missing.csv");
        TemperatureBatchProcessor.processBatch(missingFile.toString());

        TemperatureBatchProcessorTestHelper.assertSummaryNotCreated(missingFile,
                "Summary file should not be created for a missing input file");
    }
}