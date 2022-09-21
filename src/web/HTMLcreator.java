package web;

import file.AG_LogObject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import static constants.NumericConstants.LOG_MAX_RECORDS_ON_PAGE;
import static constants.URL.LOGHTML_DIR_URL;

public class HTMLcreator {
    public static void createLogTable(LinkedList<AG_LogObject> logList) {
        int totalRecords = logList.size();
        int totalPages = totalRecords / LOG_MAX_RECORDS_ON_PAGE;
        int writtenRecords = 0;

        for(int pageNumber = 0; pageNumber <= totalPages; pageNumber++) {
            int recordsOnPage = 0;
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(LOGHTML_DIR_URL + "logTable" + pageNumber + ".html"), StandardCharsets.UTF_8))) {
                writer.write("<!DOCTYPE html>\n");
                writer.write("<head>\n");
                writer.write("\t<meta charset=\"utf-8\">\n");
                writer.write("\t<style>\n");
                writer.write("\t\ttable {\n");
                writer.write("\t\t\tborder-collapse: collapse;\n");
                writer.write("\t\t\tborder: 1px solid #333;\n");
                writer.write("\t\t}\n");
                writer.write("\t\ttd {\n");
                writer.write("\t\t\tborder: 1px solid #333;\n");
                writer.write("\t\t}\n");
                writer.write("\t\ttr:nth-child(even) {\n");
                writer.write("\t\t\tbackground: #f0f0f0;\n");
                writer.write("\t\t}\n");
                writer.write("\t</style>\n");
                writer.write("</head>\n");
                writer.write("<body>\n");
                writer.write("\t<h1>Log reader</h1>\n");
                if (pageNumber - 1 < 0) {
                    writer.write("\t<a href=" + LOGHTML_DIR_URL + "logTable" + totalPages + ".html>to the last</a>\n");
                } else {
                    writer.write("\t<a href=" + LOGHTML_DIR_URL + "logTable" + (pageNumber - 1) + ".html>to the previous</a>\n");
                }

                if (pageNumber + 1 > totalPages) {
                    writer.write("\t<a href=" + LOGHTML_DIR_URL + "logTable" + 0 + ".html>to the beginning</a>\n");
                } else {
                    writer.write("\t<a href=" + LOGHTML_DIR_URL + "logTable" + (pageNumber + 1) + ".html>to the next</a>\n");
                }

                //writer.write("\t<a href=\"http://" + hostURL + ":" + port + "/getAll\">getAll</a>\n");
                //writer.write("\t<a href=\"http://" + hostURL + ":" + port + "/getDistinct\">getDistinct</a>\n");
                writer.write("\t<table>\n");
                while (recordsOnPage < LOG_MAX_RECORDS_ON_PAGE && writtenRecords < totalRecords) {
                       writer.write("\t\t<tr>\n");
                        writer.write("\t\t\t<td>" + logList.get(writtenRecords).getDate() + "</td>\n");
                        writer.write("\t\t\t<td>" + logList.get(writtenRecords).getLevel() + "</td>\n");
                        writer.write("\t\t\t<td>" + logList.get(writtenRecords).getSource() + "</td>\n");
                        writer.write("\t\t\t<td>" + logList.get(writtenRecords).getMessage() + "</td>\n");
                        writer.write("\t\t</tr>\n");

                    recordsOnPage++;
                    writtenRecords++;
                }
                writer.write("\t</table>\n");
                writer.write("</body>\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
