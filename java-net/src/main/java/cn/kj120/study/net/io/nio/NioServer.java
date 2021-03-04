package cn.kj120.study.net.io.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class NioServer {

    private int port = 8319;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private Selector selector;

    private AtomicInteger atomicInteger = new AtomicInteger();

    private Map<Integer, SocketChannel> channelMap = new ConcurrentHashMap<>();

    private Charset charset = Charset.forName("utf-8");

    public void start() {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.configureBlocking(false);

            serverSocketChannel.bind(new InetSocketAddress(port));

            selector = Selector.open();

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {

                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();

                        SocketChannel accept = channel.accept();

                        Integer uid = atomicInteger.incrementAndGet();

                        channelMap.put(uid, accept);

                        log.info("客户端[{}]已经连接到服务器，当前在线客户端数[{}]", uid, channelMap.size());

                        accept.configureBlocking(false);
                        
                        accept.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();

                        byteBuffer.clear();
                        while (channel.read(byteBuffer) > 0) {

                        }
                        byteBuffer.flip();

                        String msg = charset.decode(byteBuffer).toString();

                        if (msg == null || "".equals(msg)) {
                            log.warn("消息不能为空");
                            continue;
                        }

                        // 消息格式 接收用户id + : + 消息内容
                        String[] split = msg.split(":");
                        if (split.length != 2) {
                            log.warn("消息格式错误：{}", msg);
                            continue;
                        }

                        // 用户id
                        Integer uid = Integer.valueOf(split[0]);

                        SocketChannel socketChannel = channelMap.get(uid);

                        if (socketChannel == null) {
                            log.info("客户端[{}]已经下线, 无法接收信息", uid);
                        } else {
                            socketChannel.write(charset.encode("客户端[]: " + split[1]));
                        }

                    }
                }


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
