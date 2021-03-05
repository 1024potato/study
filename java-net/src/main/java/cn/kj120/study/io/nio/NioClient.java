package cn.kj120.study.io.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

@Slf4j
public class NioClient {

    private int port = 8319;

    private String host = "localhost";

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private Selector selector;

    private Charset charset = Charset.forName("utf-8");

    public void start() {
        try {
            SocketChannel socketChannel = SocketChannel.open();

            socketChannel.configureBlocking(false);

            socketChannel.connect(new InetSocketAddress(host, port));

            selector = Selector.open();

            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                for (SelectionKey key : selectionKeys) {

                    if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                            channel.configureBlocking(false);
                            log.info("已经连接上服务器");

                            new Thread(new UserInputHandler(socketChannel)).start();
                        }
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();

                        byteBuffer.clear();
                        while (channel.read(byteBuffer) > 0) {

                        }
                        byteBuffer.flip();

                        String string = charset.decode(byteBuffer).toString();

                        log.info(string);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NioClient nioClient = new NioClient();

        nioClient.start();
    }
}
