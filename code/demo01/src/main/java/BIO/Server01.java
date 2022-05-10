package BIO;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @Author: saturn-x
 * @Date: 2022/5/8 20:57
 * @用途： 简单实现一个BIO的服务器
 */
@Slf4j
public class Server01 {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(8080);

        while(true) {
            log.debug("服务器开始监听：{}",serverSocket);
            Socket client = serverSocket.accept();
            // 获取流
            new Thread(new MyRunnable(client)).start();
        }
    }

}


// 做为任务传入线程
@Slf4j
class MyRunnable implements Runnable{
    Socket socket;
    public MyRunnable(Socket client) {
        this.socket = client;
        log.debug("接入新的客户端{}",this.socket);
    }

    @SneakyThrows
    @Override
    public void run() {
        try (InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            while (true) {
                int maxLen = 1024;
                byte[] contextBytes = new byte[maxLen];
                int realLen = in.read(contextBytes, 0, maxLen);
                String message = new String(contextBytes , 0 , realLen);
                log.debug("从客户端{}:{}",socket,message);
                if(message.equals("quit")) {
                    break;
                }
                out.write(("复读机："+message+"\n").getBytes());
                out.flush();

            }
        } catch (IOException e ) {
            e.printStackTrace();
        }finally {
            if (socket != null)
                socket.close();
        }
        // 获取socket的流 然后不断返回

    }
}

