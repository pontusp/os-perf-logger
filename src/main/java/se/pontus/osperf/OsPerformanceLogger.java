package se.pontus.osperf;

import static java.util.Map.entry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.sun.management.OperatingSystemMXBean;


public class OsPerformanceLogger implements Runnable {
    private static final String CSV_DEFAULT_FILE = "osPerformanceLogger.csv";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.####");
    private final PrintWriter csvWriter;
    private final Map<String, Supplier<String>> csvMetrics;


    public OsPerformanceLogger(String logFile) throws FileNotFoundException {
        File csvLogFile = new File(logFile != null ? logFile : CSV_DEFAULT_FILE);
        csvWriter = new PrintWriter(csvLogFile);

        final OperatingSystemMXBean osMxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();

        csvMetrics = orderedMapOf(
                entry("vmUptime", () -> Long.toString(runtimeMxBean.getUptime())),
                entry("cpuLoad", () -> DECIMAL_FORMAT.format(osMxBean.getCpuLoad())),
                entry("processCpuLoad", () -> DECIMAL_FORMAT.format(osMxBean.getProcessCpuLoad())),
                entry("osName", osMxBean::getName),
                entry("vmName", runtimeMxBean::getVmName),
                entry("vmVendor", runtimeMxBean::getVmVendor),
                entry("vmVersion", runtimeMxBean::getVmVersion));

        csvWriter.println(csvMetrics.keySet().stream().collect(Collectors.joining(",")));
    }

    @SafeVarargs
    private static <K, V> Map<K, V> orderedMapOf(Entry<? extends K, ? extends V>... entries) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        for (Entry<? extends K, ? extends V> entry : entries ) {
            map.put(entry.getKey(), entry.getValue());
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void run() {
        csvWriter.println(csvMetrics.values().stream().map(Supplier::get).collect(Collectors.joining(",")));
        csvWriter.flush();
    }

    public static void main(String[] args) {
        try {
            String logFile = args.length > 0 ? args[0] : CSV_DEFAULT_FILE;
            System.out.println("Starting OS Performance Logger to file: " + logFile);
            OsPerformanceLogger perfLogger = new OsPerformanceLogger(logFile);
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(perfLogger, 0, 1, TimeUnit.SECONDS);

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
