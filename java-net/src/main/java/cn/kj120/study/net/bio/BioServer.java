package cn.kj120.study.net.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class BioServer {

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 存放在线客户端集合
     */
    private Map<String, BufferedWriter> writerMap = new ConcurrentHashMap<>();

    /**
     * 生成uid
     */
    private AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 处理消息线程池
     */
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 5L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(50));

    public BioServer() {
        this(8001);
    }

    public BioServer(int port) {
        this.port = port;
    }

    /**
     * 服务器启动
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket();

            // 绑定指定端口
            serverSocket.bind(new InetSocketAddress(port));

            while (true) {

                Socket socket = serverSocket.accept();

                // 生产连接客户端唯一id
                String uid = String.valueOf(atomicInteger.incrementAndGet());

                // 处理消息的收发
                executor.execute(new MessageHandler(uid, writerMap, socket));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BioServer bioServer = new BioServer();

        bioServer.start();
    }
}
