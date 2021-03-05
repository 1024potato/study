package cn.kj120.study.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;

public class AioClient {

    private int port = 8320;

    private String host = "localhost";

    public void start() {
        try {
            AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();

            socketChannel.connect(
                    new InetSocketAddress(host, port),
                    null,
                    new ConnectCompletionHandler(socketChannel)
            );

            while (true) {
                Thread.sleep(5);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        AioClient aioClient = new AioClient();

        aioClient.start();
    }
}
