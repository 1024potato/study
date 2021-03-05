package cn.kj120.study.net.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServer {

    private int port = 8320;

    private EventLoopGroup boss = new NioEventLoopGroup();

    private EventLoopGroup work = new NioEventLoopGroup();

    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.channel(NioServerSocketChannel.class)
                    .group(boss, work)
                    .childHandler(new CustomChannelHandler())
                    .localAddress(port)
                    .bind()
                    .sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer();

        nettyServer.start();
    }
}
