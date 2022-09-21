package file;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class LogFileReader {
    public static LinkedList<AG_LogObject> readLog(String logURL) {
        LinkedList<AG_LogObject> agLogObjects = new LinkedList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(logURL), StandardCharsets.UTF_8))) {
            String line;
            String logRecord = "";
            while((line = br.readLine()) != null) {
                if(line.matches("^\\d{2}\\.\\d{2}\\.\\d{4}.*")) {
                    if(!logRecord.isEmpty()) {
                        agLogObjects.add(parser(logRecord));
                    }
                    logRecord = line;
                } else {
                    logRecord = logRecord.concat(line);
                }
            }
            agLogObjects.add(parser(logRecord)); //to add last record, that'll not be caught by if clause.
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agLogObjects;
    }

    public static AG_LogObject parser(String logRecord) {
        String date = logRecord.substring(0, logRecord.indexOf(" "));
        logRecord = logRecord.substring(logRecord.indexOf(" ")).trim();
        date = date.concat(" ");
        date = date.concat(logRecord.substring(0, logRecord.indexOf(" ")));
        logRecord = (logRecord.substring(logRecord.indexOf(" ")).trim());
        String level = logRecord.substring(0, logRecord.indexOf(" "));
        logRecord = logRecord.substring(logRecord.indexOf(" ")).trim();
        String source = logRecord.substring(0, logRecord.indexOf(" "));
        String message = logRecord.substring(logRecord.indexOf(" ")).trim();
        return new AG_LogObject(date, level, source, message);
    }
}
