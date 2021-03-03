package cn.kj120.study.net.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

@Slf4j
public class ClientInputHandler implements Runnable {

    private SocketChannel socketChannel;

    private Charset charset;

    public ClientInputHandler(SocketChannel socketChannel, Charset charset) {
        this.socketChannel = socketChannel;
        this.charset = charset;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (scanner.hasNext()) {
                String next = scanner.next();
                ByteBuffer byteBuffer = charset.encode(next);
                try {
                    while (byteBuffer.hasRemaining()) {
                        socketChannel.write(byteBuffer);
                    }
                    log.info("发送消息：" + next);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
