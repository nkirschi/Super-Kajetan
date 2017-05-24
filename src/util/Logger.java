package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Nützliche Klasse für das Logging in eine Datei
 */
public class Logger {
    private static BufferedWriter writer;

    // Konstanten für die Angabe eines Logging-Levels
    public static final String INFO = "INFO: ";
    public static final String WARNING = "WARNUNG: ";
    public static final String ERROR = "FEHLER: ";

    /**
     * Statischer Block, der beim Laden der Klasse ausgeführt wird (konstruktorähnlich)
     */
    static {
        try {
            writer = new BufferedWriter(new FileWriter("log.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allgemeine Methode für das Loggen eines Strings
     *
     * @param level Das Level des zu loggenden Ereignisses
     * @param msg   Die Nachricht zum entsprechenden Ereignis
     */
    public static void log(String level, String msg) {
        try {
            String timestamp = new SimpleDateFormat("[E, d.MM.yyyy hh:mm:ss.SSS] ").format(new Date());
            writer.write(timestamp);
            writer.write(level);
            writer.write(msg);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Spezialisierung der allgemeinen log-Methode für Exceptions
     *
     * @param level Das Level der zu loggenden Exception
     * @param e     Die zu loggende Exception
     */
    public static void log(String level, Exception e) {
        for (StackTraceElement i : e.getStackTrace()) {
            String s = e.toString().concat(" in ").concat(i.getClassName())
                    .concat(" in Methode ").concat(i.getMethodName()).concat("()")
                    .concat(" in Zeile ").concat(Integer.toString(i.getLineNumber()));
            log(level, s);
        }
    }

    /**
     * Methode zur Ressourcenfreigabe nach korrekter Beendigung des Programms
     */
    public static void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
