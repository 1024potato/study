package cn.kj120.study.io.netty;

import io.netty.channel.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomChannelHandler extends ChannelInitializer {

    @Override
    protected void initChannel(Channel channel) throws Exception {
        channel.pipeline()
                .addLast(new StringEncoder())
                .addLast(new StringDecoder())
                .addLast(new ImSimpleChannelInboundHandler());
    }
}
