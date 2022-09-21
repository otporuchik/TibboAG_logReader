package file;

public class AG_LogObject {
    private String date;
    private String level;
    private String source;
    private String message;

    public AG_LogObject(String date, String level, String source, String message) {
        this.date = date;
        this.level = level;
        this.source = source;
        this.message = message;
    }

    public String getDate() { return this.date; }
    public String getLevel() { return this.level; }
    public String getSource() { return this.source; }
    public String getMessage() { return this.message; }
}
