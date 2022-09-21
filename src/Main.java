import web.WEBServer;

import java.io.IOException;
import java.net.ServerSocket;

import static constants.NumericConstants.WEB_SOCKET_PORT;

public class Main {
    public static void main(String[] args) {

        try {
            ServerSocket webSocket = new ServerSocket(WEB_SOCKET_PORT);
            while(true) {
                new WEBServer(webSocket.accept());
            }
        } catch (IOException e) {
            System.out.println("SOCKET ERROR" + e);
            e.printStackTrace();
        }

        /*

        if(agList != null) {
            for (AG_LogObject agObj : agList) {
                System.out.println(agObj.getDate());
                System.out.println(agObj.getLevel());
                System.out.println(agObj.getSource());
                System.out.println(agObj.getMessage());
            }
        }

 */

    }
}
