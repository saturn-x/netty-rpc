import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author: saturn-x
 * @Date: 2022/5/9 15:26
 * @用途：
 */
public class Client2 {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        SocketChannel out = socket.getChannel();
        while (true) {
            String s = "hello world";
            out.write(StandardCharsets.UTF_8.encode(s));
        }

    }
}
