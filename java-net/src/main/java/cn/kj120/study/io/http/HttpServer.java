package cn.kj120.study.io.http;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.Set;

@Slf4j
public class HttpServer {

    private int port;

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private Charset charset = Charset.forName("utf-8");

    private ByteBuffer byteBuffer = ByteBuffer.allocate(2048);

    public HttpServer() {
        this(8080);
    }

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.bind(new InetSocketAddress(port));

        selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();

            Set<SelectionKey> keys = selector.selectedKeys();

            for (SelectionKey key : keys) {

                if (key.isAcceptable()) {
                    serverSocketChannel = (ServerSocketChannel) key.channel();

                    SocketChannel accept = serverSocketChannel.accept();

                    accept.configureBlocking(false);

                    accept.register(selector, SelectionKey.OP_READ);

                } else if (key.isReadable()) {

                    SocketChannel channel = (SocketChannel) key.channel();


                    byteBuffer.clear();

                    while (channel.read(byteBuffer) > 0) {

                    }
                    byteBuffer.flip();
                    String message = charset.decode(byteBuffer).toString();

                    Request request = new Request(message);

                    key.attach(request);

                    key.interestOps(SelectionKey.OP_WRITE);
                } else if (key.isWritable()) {

                    SocketChannel channel = (SocketChannel) key.channel();

                    Request request = (Request) key.attachment();

                    Response response = new Response();

                    response.setCode(200);
                    response.setVersion("HTTP/1.1");

                    response.setHeader("Content-Type", "application/json;charset=UTF-8");

                    int i = new Random().nextInt(9999);

                    response.setBody(String.format("{\"name\":\"pcg\", \"age\": %s}", i));

                    String result = response.httpReturn();

                    ByteBuffer encode = charset.encode(result);

                    while (encode.hasRemaining()) {
                        channel.write(encode);
                    }
                    channel.close();
                }
            }

            keys.clear();
        }
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer();

        httpServer.start();
    }

}
