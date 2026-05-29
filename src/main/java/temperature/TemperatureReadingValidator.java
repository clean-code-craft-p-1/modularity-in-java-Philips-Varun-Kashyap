package temperature;

final class TemperatureReadingValidator {
    private static final int TIMESTAMP_PARTS = 3;
    private static final double MIN_TEMPERATURE = -100.0;
    private static final double MAX_TEMPERATURE = 200.0;

    private TemperatureReadingValidator() {
    }

    static Double validate(String line) {
        String[] parts = line.split(",");
        if (parts.length != 2) {
            return null;
        }

        if (!hasValidTimestamp(parts[0].strip())) {
            return null;
        }

        return parseTemperature(parts[1].strip());
    }

    private static boolean hasValidTimestamp(String timestamp) {
        return timestamp.split(":").length == TIMESTAMP_PARTS;
    }

    private static Double parseTemperature(String value) {
        double temperature;
        try {
            temperature = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }

        if (temperature < MIN_TEMPERATURE || temperature > MAX_TEMPERATURE) {
            return null;
        }

        return temperature;
    }
}