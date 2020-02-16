import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

/**
 * The Log class.
 */
public class Log {
    /**
     * logger Log class variable.
     */
    public Logger logger = Logger.getLogger(Log.class.getName());
    private ConsoleHandler ch;
    private FileHandler fh;

    /**
     * constructor.
     *
     * @param filename the file that stores logging details.
     */
    public Log(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists()) {
                f.createNewFile();
            }
            ch = new ConsoleHandler();
            ch.setLevel(Level.ALL);
            logger.addHandler(ch);
            fh = new FileHandler(filename);
            fh.setLevel(Level.ALL);
            logger.setLevel(Level.ALL);
            logger.config("Configuration done.");
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.addHandler(fh);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
