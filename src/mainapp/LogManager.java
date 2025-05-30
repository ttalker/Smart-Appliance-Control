package mainapp;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

public class LogManager {
    private static final List<String> logs = new ArrayList<>();
    private static JTextArea logArea;

    // Attach the JTextArea to update
    public static void attachLogArea(JTextArea area) {
        logArea = area;
        refreshLogArea();
    }

    // Add a new log entry
    public static void log(String message) {
        String entry = "[" + java.time.LocalTime.now().withNano(0) + "] " + message;
        logs.add(entry);
        if (logArea != null) {
            logArea.append(entry + "\n");
        }
    }

    // Optionally refresh the whole area (e.g. when switching panels)
    public static void refreshLogArea() {
        if (logArea != null) {
            logArea.setText(""); // clear
            for (String log : logs) {
                logArea.append(log + "\n");
            }
        }
    }

    // For looping access
    public static List<String> getLogs() {
        return logs;
    }
}
