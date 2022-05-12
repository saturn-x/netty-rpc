import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Author: saturn-x
 * @Date: 2022/5/8 22:10
 * @用途： 做为用户端进行测试
 */
@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8080);
        InputStream in = socket.getInputStream();
        OutputStream out = null;
        log.debug("客户端建立连接：{}",socket);
        while (true) {
            out = socket.getOutputStream();
            // 从窗口获取输入传递out
            log.debug("开始输入···");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferedReader.readLine();
            if(s.equals("quit"))
                break;
            byte[] buf = new byte[1024];
            log.debug("屏幕打印：{}",s);
            out.write(s.getBytes());
            out.close();
//            out.flush();
//            int len = in.read(buf,0,1024);
//            String s1 = new String(buf,0, len);
//            log.debug("{}",s1);
        }
    }
}
