package cn.kj120.study.io.aio;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Scanner;

@Slf4j
public class ConnectCompletionHandler implements CompletionHandler<Void, Object> {

    private Charset charset = Charset.forName("utf-8");

    private AsynchronousSocketChannel socketChannel;

    public ConnectCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Void result, Object attachment) {
        log.info("客户端已经连接上了");
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer, byteBuffer, new ClientReadCompletionHandler(socketChannel));

        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNext()) {
                String next = scanner.next();
                log.info("发送消息: " + next);
                ByteBuffer encode = charset.encode(next);
                socketChannel.write(encode);
            }
        }
    }

    @Override
    public void failed(Throwable exc, Object attachment) {

    }
}
