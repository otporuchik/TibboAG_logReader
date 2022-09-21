package web;

import constants.LogFileStatus;
import file.AG_LogObject;
import file.LogFileChecker;
import file.LogFileReader;

import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static constants.NumericConstants.BUFFER_SIZE;
import static constants.URL.*;

public class WEBServer extends Thread {
    private final Socket socket;
    private File file = null;

    public WEBServer(Socket socket) {
        this.socket = socket;
        setDaemon(true);
        start();
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytes = inputStream.read(buffer);
            String rawRequest = new String(buffer, 0, buffer.length);
            System.out.println("Raw request is: " + rawRequest);
            String request = getRequest(rawRequest);
            System.out.println("Request is: " + request);
            String path = createPath(request);
            file = new File(path);
            String response = "HTTP/1.1 200 OK\n" +
                    "Last-Modified: " + new Date(file.lastModified()) + "\n" +
                    "Content-Length: " + file.length() + "\n" +
                    "Content-Type: " + getMIMEtype(request) + "\n" +
                    "Connection: close \n" +
                    "Server: Server\n" +
                    "Pragma: no-cache\n\n";
            FileInputStream fileInputStream = new FileInputStream(path);
            outputStream.write(response.getBytes());
            int writer = 1;
            while(writer > 0) {
                writer = fileInputStream.read(buffer);
                if(writer > 0) {
                    outputStream.write(buffer, 0, writer);
                }
            }
            inputStream.close();
            outputStream.close();
            fileInputStream.close();
        } catch(IOException e) {
            System.out.println("ERROR IN WEBServer: + e");
            e.printStackTrace();
        }
    }

    private  String getRequest(String rawRequest) {
        String request = "no request";
        String REG_REQUEST = "(?<=GET\\s).*(?=\\sHTTP)";
        Pattern patternRequest = Pattern.compile(REG_REQUEST);
        Matcher matcherRequest = patternRequest.matcher(rawRequest);
        while(matcherRequest.find()) {
            request = rawRequest.substring(matcherRequest.start(), matcherRequest.end());
        }
        return request;
    }

    private String createPath(String request) {
        String path = DEFAULT_HTML_URL;;
        switch (request) {
            case "/":
                path = INDEX_HTML_URL;
                break;
            case "/logTable":
                path = getLogHTML();
                break;
            case "/style.css":
                path = STYLE_CSS_URL;
                break;
            case "/favicon.ico":
                path = FAVICON_URL;
                break;
            default:
                path = DEFAULT_HTML_URL;
                break;
        }
        return path;
    }

    public String getMIMEtype(String request) {
        String type = "plain/text";
        switch (request) {
            case "/style.css":
                type = "text/css";
                break;
            default:
                type = "text/html";
                break;
        }
        return type;
    }

    public String getLogHTML() {
        if(LogFileChecker.checkLogFileStatus(LOG_FILE_URL).equals(LogFileStatus.FIRST_TOUCH)  ||
                LogFileChecker.checkLogFileStatus(LOG_FILE_URL).equals(LogFileStatus.MODIFIED) ) {
            LinkedList<AG_LogObject> agList = new LinkedList<>(LogFileReader.readLog(LOG_FILE_URL));
            System.out.println("reading log completed");
            HTMLcreator.createLogTable(agList);
        }
        return LOG_HTML_URL;
    }
}
