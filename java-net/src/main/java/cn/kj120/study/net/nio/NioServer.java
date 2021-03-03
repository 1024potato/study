package cn.kj120.study.net.nio;

import cn.kj120.study.net.bio.BioServer;
import cn.kj120.study.net.bio.MessageHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NioServer {

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 存放在线客户端集合
     */
    private Map<String, SocketChannel> writerMap = new ConcurrentHashMap<>();

    /**
     * 生成uid
     */
    private AtomicInteger atomicInteger = new AtomicInteger();

    /**
     *
     */
    private Selector selector;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private Charset charset = Charset.forName("utf-8");

    public NioServer() {
        this(8001);
    }

    public NioServer(int port) {
        this.port = port;
    }

    /**
     * 服务器启动
     */
    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.configureBlocking(false);

            // 绑定指定端口
            serverSocketChannel.bind(new InetSocketAddress(port));


            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


            while (true) {

                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();

                for (SelectionKey key : keys) {

                    if (key.isAcceptable()) {
                        // 生成连接客户端唯一id
                        String uid = String.valueOf(atomicInteger.incrementAndGet());

                        serverSocketChannel = (ServerSocketChannel) key.channel();

                        // 接收连接的socket
                        SocketChannel socketChannel = serverSocketChannel.accept();

                        socketChannel.configureBlocking(false);

                        // 注册读事件到selector
                        socketChannel.register(selector, SelectionKey.OP_READ);

                        writerMap.put(uid, socketChannel);
                        log.info("客户端[{}]已经连接, 当前客户端总数[{}]", uid, writerMap.size());
                    } else if (key.isReadable()) {
                        // 获取触发事件的socket
                        SocketChannel socketChannel = (SocketChannel) key.channel();

                        byteBuffer.clear();

                        while (socketChannel.read(byteBuffer) > 0) {
                            socketChannel.read(byteBuffer);
                        }

                        byteBuffer.flip();



                        String msg = charset.decode(byteBuffer).toString();

                        String[] split = msg.split(":");

                        String toUid = split[0];

                        SocketChannel channel = writerMap.get(toUid);

                        ByteBuffer byteBuffer = charset.encode("客户端[]: " + split[1]);

                        while (byteBuffer.hasRemaining()) {
                            channel.write(byteBuffer);
                        }

                        log.info("客户端[{}]: {}", toUid, msg);

                    }
                }


                keys.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioServer nioServer = new NioServer();

        nioServer.start();
    }
}
