package cn.kj120.study.net.io.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

@Slf4j
public class UserInputHandler implements Runnable {

    private SocketChannel socketChannel;

    private Charset charset = Charset.forName("utf-8");


    public UserInputHandler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNext()) {
                String next = scanner.next();
                String[] split = next.split(":");
                if (split.length != 2) {
                    log.warn("消息格式错误: " + next);
                    continue;
                }

                ByteBuffer byteBuffer = charset.encode(next);
                while (byteBuffer.hasRemaining()) {
                    try {
                        socketChannel.write(byteBuffer);
                        log.info("正在发生消息: {}", byteBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
