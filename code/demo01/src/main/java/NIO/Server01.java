package NIO;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: saturn-x
 * @Date: 2022/5/9 13:52
 * @用途： 使用 nio
 */
@Slf4j
public class Server01 {
    public static void main(String[] args) throws IOException {
        // 1.创建select 选择器
        Selector selector = Selector.open();
        // 2.创建服务器channel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false); // 设置channel  非阻塞
        ssc.register(selector, SelectionKey.OP_ACCEPT);// 注册到select 表示对连接事件感兴趣

        // 3. 创建socket
        ServerSocket serverSocket = ssc.socket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1",8080));
        log.debug("NIO服务器开始监听：{}",serverSocket);
        // 4. 创建一个线程池 用来对读取的数据进行返回
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 5. 获取连接
        while (true) {
            int n = selector.select(); // 阻塞 等待感兴趣的事件到达
            log.debug("select 获取到事件的个数：{}",n);
            // 5.事件到达 获取事件集合
            Set<SelectionKey> set = selector.selectedKeys();
            Iterator<SelectionKey> iterator = set.iterator();
            while(iterator.hasNext()) {
                // 6. 遍历集合
                SelectionKey cur = iterator.next();
                iterator.remove();
                // 7.判断事件类型
                if(cur.isAcceptable()) {
                    // 该事件为连接事件 将获取到的channel注册到selector上
                    ServerSocketChannel ssc1 = (ServerSocketChannel)cur.channel();
                    // 从连接队列取出并创建一个SocketChannel
                    SocketChannel accept = ssc1.accept();
                    accept.configureBlocking(false);
                    // 对后续的注册读事件
                    accept.register(selector,SelectionKey.OP_READ);
                    log.debug("客户端{}连接···",accept);
                }else if(cur.isReadable()) {
                    SocketChannel sc = (SocketChannel)cur.channel();
                    log.debug("客户端{}有可读取事件",sc);
                    // 该事件为读事件 读完数据打印并返回
                    readHandle(cur,selector);
                }

            }
        }

    }
    private static void readHandle(SelectionKey selectionKey, Selector selector) throws IOException {
        // 1. 获取channel
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        /**
         * 循环读取客户端请求信息
         */
        StringBuilder request = new StringBuilder();
        while (socketChannel.read(byteBuffer) > 0) {
            /**
             * 切换buffer为读模式
             */
            byteBuffer.flip();

            /**
             * 读取buffer中的内容
             */
            request.append(StandardCharsets.UTF_8.decode(byteBuffer));
        }

        /**
         * 将channel再次注册到selector上，监听他的可读事件
         */
        socketChannel.register(selector, SelectionKey.OP_READ);

        log.debug("从{}：获取消息：{}", socketChannel.socket(),request.toString());
    }
    private static String readDataFromSocketChannel(SocketChannel sChannel) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuilder data = new StringBuilder();

        while (true) {

            buffer.clear();
            log.debug("服务器使用read读取···");
            int n = sChannel.read(buffer);
            log.debug("服务器使用read读取完毕，长度:{}···",n);
            if (n == -1) {
                break;
            }
            buffer.flip();
            int limit = buffer.limit();
            char[] dst = new char[limit];
            for (int i = 0; i < limit; i++) {
                dst[i] = (char) buffer.get(i);
            }
            data.append(dst);
            buffer.clear();
        }
        return data.toString();
    }


}

@Slf4j
class WorkerTask implements Runnable {
    SocketChannel socketChannel;

    public WorkerTask(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @SneakyThrows
    @Override
    public void run() {
        // 对数据读写并且返回
        ByteBuffer buf = ByteBuffer.allocate(512);

        StringBuilder data = new StringBuilder();

        while (true) {
            buf.clear();
            int n = socketChannel.read(buf);
            if (n == -1) {
                break;
            }
            buf.flip();
            int limit = buf.limit();
            char[] dst = new char[limit];
            for (int i = 0; i < limit; i++) {
                dst[i] = (char) buf.get(i);
            }
            data.append(dst);
            buf.clear();
        }
        String res = data.toString();
        log.debug("从{}读取：{}",socketChannel,res);
        socketChannel.write(ByteBuffer.wrap(res.getBytes(StandardCharsets.UTF_8)));
    }
}