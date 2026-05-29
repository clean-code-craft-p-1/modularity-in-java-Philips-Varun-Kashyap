package temperature;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java temperature.Main <input-file>");
            return;
        }

        TemperatureBatchProcessor.processBatch(args[0]);
    }
}