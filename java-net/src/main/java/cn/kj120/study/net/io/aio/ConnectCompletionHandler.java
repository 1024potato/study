package cn.kj120.study.net.io.aio;

import lombok.extern.slf4j.Slf4j;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

@Slf4j
public class ConnectCompletionHandler implements CompletionHandler<Void, Object> {

    private AsynchronousSocketChannel socketChannel;

    public ConnectCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void completed(Void result, Object attachment) {
        log.info("客户端已经连接上了");
    }

    @Override
    public void failed(Throwable exc, Object attachment) {

    }
}
