package temperature;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

final class TemperatureBatchProcessorTestHelper {
    private TemperatureBatchProcessorTestHelper() {
    }

    static Path writeInputFile(Path tempDir, String fileName, List<String> lines) throws IOException {
        Path inputFile = tempDir.resolve(fileName);
        Files.write(inputFile, lines);
        return inputFile;
    }

    static Path summaryFileFor(Path inputFile) {
        return inputFile.resolveSibling(inputFile.getFileName() + "_summary.txt");
    }

    static Path writeAndProcess(Path tempDir, String fileName, String... lines) throws IOException {
        Path inputFile = writeInputFile(tempDir, fileName, List.of(lines));
        TemperatureBatchProcessor.processBatch(inputFile.toString());
        return inputFile;
    }

    static String writeProcessAndReadSummary(Path tempDir, String fileName, String... lines) throws IOException {
        return Files.readString(summaryFileFor(writeAndProcess(tempDir, fileName, lines)));
    }

    static void assertContainsAll(String content, String... expectedValues) {
        for (String expectedValue : expectedValues) {
            assertTrue(content.contains(expectedValue));
        }
    }

    static void assertSummaryNotCreated(Path inputFile, String message) {
        assertFalse(Files.exists(summaryFileFor(inputFile)), message);
    }
}