package cn.kj120.study.io.aio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AioServer {

    private int port = 8318;

    private AtomicInteger atomicInteger = new AtomicInteger();

    private Map<Integer, AsynchronousSocketChannel> channelMap = new ConcurrentHashMap<>();

    public void start() {
        try {
            AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));

            while (true) {
                serverSocketChannel.accept(null, new AcceptCompletionHandler(serverSocketChannel, atomicInteger, channelMap));

                System.in.read();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AioServer aioServer = new AioServer();

        aioServer.start();
    }
}
