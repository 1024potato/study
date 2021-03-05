package cn.kj120.study.io.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ImSimpleChannelInboundHandler extends SimpleChannelInboundHandler<String> {

    private Map<String, ChannelHandlerContext> ctxMap = new ConcurrentHashMap<>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端注册事件 {}", ctx);
        String clientId = ctx.channel().id().asShortText();
        ctxMap.put(clientId, ctx);
        log.info("客户端[{}]上线，当前在线客户端数[{}]", clientId, ctxMap.size());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端未注册事件 {}", ctx);
        String clientId = ctx.channel().id().asShortText();
        ctxMap.remove(clientId, ctx);
        log.info("客户端[{}]下线，当前在线客户端数[{}]", clientId, ctxMap.size());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端活跃事件 {}", ctx);
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端不活跃事件 {}", ctx);
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("客户端读取完成事件 {}", ctx);
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("客户端事件 {}", evt);
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端异常事件 {}", ctx);
        //super.exceptionCaught(ctx, cause);
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        log.info(s);

        ctx.writeAndFlush( "服务器响应: " + new Random().nextInt(10) + "\n");
    }
}
