package cn.kj120.study.net.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyClient {

    private int port = 8320;

    private String host = "localhost";

    public void start() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();

        ChannelFuture connect = bootstrap.connect(host, port);

        ChannelFuture sync = connect.sync();


    }
}
