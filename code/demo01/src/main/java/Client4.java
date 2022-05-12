import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author: saturn-x
 * @Date: 2022/5/10 18:45
 * @用途：
 */
public class Client4 {
    public static void main(String[] args) throws IOException {
        /*
        * 1.测试 简单的socket
        * */
        String host= "127.0.0.1";
        Socket socket = new Socket(host, 13);
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[256];
        int read = inputStream.read(bytes);
        System.out.printf("长度为%d%n", read);
        System.out.println("内容为："+new String(bytes,0,read));
        /*
        *2. 测试使用HTTP
         */
//        Socket socket = new Socket("www.oreilly.com",80);
//        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
//        outputStreamWriter.write("GET / HTTP1.1\r\n\r\n");
//        outputStreamWriter.flush();
//        socket.shutdownOutput();
//        InputStream in = socket.getInputStream();
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//        byte[] bytes = new byte[1024];
//        for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine() )
//            System.out.println(line);
//        socket.close();
    }
}
