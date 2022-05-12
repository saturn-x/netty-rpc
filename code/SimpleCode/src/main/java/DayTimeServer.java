import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @Author: saturn-x
 * @Date: 2022/5/11 11:17
 * @用途：
 */
public class DayTimeServer {
    public static void main(String[] args) throws IOException {
        /*
        * 1.创建ServerScoket
        * */
        int port = 13;
        ServerSocket serverSocket = new ServerSocket(13);
        while (true) {
            Socket accept = serverSocket.accept();
            OutputStream outputStream = accept.getOutputStream();
            outputStream.write((new Date().toString()+"\r\n").getBytes());
            outputStream.flush();
        }



    }
}
