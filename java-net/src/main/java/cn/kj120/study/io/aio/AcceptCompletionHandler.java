package cn.kj120.study.io.aio;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

    private AsynchronousServerSocketChannel serverSocketChannel;

    private AtomicInteger atomicInteger;

    private Map<Integer, AsynchronousSocketChannel> channelMap;

    public AcceptCompletionHandler(AsynchronousServerSocketChannel serverSocketChannel, AtomicInteger atomicInteger, Map<Integer, AsynchronousSocketChannel> channelMap) {
        this.serverSocketChannel = serverSocketChannel;
        this.atomicInteger = atomicInteger;
        this.channelMap = channelMap;
    }

    @Override
    public void completed(AsynchronousSocketChannel client, Object attachment) {

        Integer uid = atomicInteger.incrementAndGet();

        channelMap.put(uid, client);

        log.info("客户端[{}]已上线, 当前在线客户端数量[{}]", uid, channelMap.size());

        if (serverSocketChannel.isOpen()) {
            serverSocketChannel.accept(null, new AcceptCompletionHandler(serverSocketChannel, atomicInteger, channelMap));
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        client.read(byteBuffer, byteBuffer, new ReadCompletionHandler(channelMap, client, uid));
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        log.error("连接异常", exc);
    }
}
