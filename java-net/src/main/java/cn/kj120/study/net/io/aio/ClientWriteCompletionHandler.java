package cn.kj120.study.net.io.aio;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

@Slf4j
public class ClientWriteCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel client;

    private Charset charset = Charset.forName("utf-8");

    public ClientWriteCompletionHandler(AsynchronousSocketChannel client) {
        this.client = client;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
