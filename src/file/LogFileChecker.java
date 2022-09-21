package file;

import constants.LogFileStatus;

import java.io.File;

import static constants.LogFileStatus.*;
import static constants.URL.LOG_FILE_URL;

public class LogFileChecker {
    /** Check if log file exists and modified.
     * If not exists or exists and was modified return TRUE
     * If exists and not modified return FALSE
     */
    public static Long lastModified = null;

    public static LogFileStatus checkLogFileStatus(String logURL) {
        File logFile = new File(logURL);
        if (logFile.isFile()) {
            if (lastModified == null) {
                lastModified = logFile.lastModified();
                return FIRST_TOUCH;
            } else if (logFile.lastModified() > lastModified) {
                return MODIFIED;
            }
        } else if (logFile.lastModified() == lastModified) {
            return NOT_MODIFIED;
        }
        return NOT_FOUND;
    }
}
