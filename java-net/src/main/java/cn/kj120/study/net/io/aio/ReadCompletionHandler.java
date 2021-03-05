package cn.kj120.study.net.io.aio;

import cn.kj120.study.net.io.entity.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {


    private Map<Integer, AsynchronousSocketChannel> channelMap;

    private AsynchronousSocketChannel client;

    private Integer uid;

    private Charset charset = Charset.forName("utf-8");

    public ReadCompletionHandler(Map<Integer, AsynchronousSocketChannel> channelMap, AsynchronousSocketChannel client, Integer uid) {
        this.channelMap = channelMap;
        this.client = client;
        this.uid = uid;
    }

    @Override
    public void completed(Integer integer, ByteBuffer byteBuffer) {
        byteBuffer.flip();

        String string = charset.decode(byteBuffer).toString();

        log.info("接收到客户端消息: {}", string);

        Message message = receive(string);

        log.info(message.toString());

        if (message == null) {
            return;
        }

        String sendStr = String.format("客户端[%s]: %s", uid, message.getContent());

        ByteBuffer buffer = charset.encode(sendStr);

        if (message.getType() == 0) {
            for (Map.Entry<Integer, AsynchronousSocketChannel> entry : channelMap.entrySet()) {
                entry.getValue().write(buffer);
            }
        } else {
            AsynchronousSocketChannel socketChannel = channelMap.get(message.getToUid());
            if (socketChannel == null) {
                log.warn("消息接收客户端[{}]已经下线", message.getToUid());
            } else {
                socketChannel.write(buffer);
            }
        }

        // 继续读取下一次接收到的数据
        byteBuffer.clear();

        client.read(byteBuffer, byteBuffer, this);

    }

    @Override
    public void failed(Throwable exc, ByteBuffer byteBuffer) {
        channelMap.remove(uid);
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.error("读取消息异常", exc);
    }

    private Message receive(String str) {
        if (str == null || "".equals(str)) {
            log.info("消息不能为空");
            return null;
        }

        String[] split = str.split(":");

        if (split.length != 2) {
            log.info("消息格式错误");
        }

        Message message = new Message();

        Integer toUid = Integer.valueOf(split[0]);

        message.setFromUid(uid);
        message.setToUid(toUid);
        message.setContent(split[1]);

        Integer type = toUid == 0 ? 0 : 1;

        message.setType(type);

        return message;
    }
}
