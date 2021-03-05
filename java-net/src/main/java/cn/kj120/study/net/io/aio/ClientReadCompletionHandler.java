package cn.kj120.study.net.io.aio;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;


@Slf4j
public class ClientReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private Charset charset = Charset.forName("utf-8");

    private AsynchronousSocketChannel socketChannel;

    public ClientReadCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer byteBuffer) {
        byteBuffer.flip();

        String string = charset.decode(byteBuffer).toString();

        log.info(string);

        byteBuffer.clear();
        socketChannel.read(byteBuffer, byteBuffer, new ClientReadCompletionHandler(socketChannel));
    }

    @Override
    public void failed(Throwable exc, ByteBuffer byteBuffer) {
        log.warn("读取信息错误", exc);
    }
}
