package cn.kj120.study.net.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Set;

@Slf4j
public class NioClient {

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 退出命令
     */
    private static final String EXIT_CONTENT = "exit";

    private Selector selector;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private SocketChannel socketChannel;

    private Charset charset = Charset.forName("utf-8");

    public NioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            SocketChannel socketChannel = SocketChannel.open();

            socketChannel.configureBlocking(false);

            socketChannel.connect(new InetSocketAddress(host, port));

            selector = Selector.open();

            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();

                for (SelectionKey key : keys) {

                    if (key.isConnectable()) {

                        socketChannel = (SocketChannel) key.channel();
                        if (socketChannel.isConnectionPending()) {
                            socketChannel.finishConnect();

                            log.info("客户端已经连接");

                            new Thread(new ClientInputHandler(socketChannel, charset)).start();
                        }

                        // 注册读事件到selector
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        // 获取触发事件的socket
                        socketChannel = (SocketChannel) key.channel();

                        byteBuffer.clear();

                        socketChannel.write(byteBuffer);

                        byteBuffer.flip();

                        String msg = charset.decode(byteBuffer).toString();


                        System.out.println(msg);

                    }
                }


                keys.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        NioClient nioClient = new NioClient("localhost", 8001);

        nioClient.start();
    }
}
