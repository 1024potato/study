package cn.kj120.study.net.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

public class AioClient {

    private int port = 8318;

    private String host = "localhost";

    public void start() throws IOException {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();

        socketChannel.connect(
                new InetSocketAddress(host, port),
                null,
                new ConnectCompletionHandler(socketChannel)
        );


    }
}
