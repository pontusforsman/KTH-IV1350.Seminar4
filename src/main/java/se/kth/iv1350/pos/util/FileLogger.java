package se.kth.iv1350.pos.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple file logger for error reporting in the POS system.
 */
public class FileLogger {
    private static final String LOG_FILE = "error.log";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Logs an error message and exception stack trace to a file.
     *
     * @param message The error message to log.
     * @param e The exception to log.
     */
    public static void log(String message, Exception e) {
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            out.println("[" + LocalDateTime.now().format(FORMATTER) + "] ERROR: " + message);
            if (e != null) {
                e.printStackTrace(out);
            }
            out.println();
        } catch (IOException io) {
            System.err.println("Failed to write to log file: " + io.getMessage());
        }
    }

    public static void log(Exception e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'log'");
    }
}
