package cn.kj120.study.io.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class BioClient {
    /**
     * 服务器地址
     */
    private String host;

    /**
     * 服务器端口
     */
    private int port;

    /**
     * 退出命令
     */
    private static final String EXIT_CONTENT = "exit";

    public BioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 客户端启动
     * @throws IOException
     */
    public void start() throws IOException {

        Socket socket = new Socket();

        socket.connect(new InetSocketAddress(host, port), 5000);

        // 判断客户端是否连接成功
        while (!socket.isConnected()) {

        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // 接收消息
        receive(reader);



        // 从控制台获取消息发送内容
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (scanner.hasNext()) {
                String next = scanner.next();
                String[] msg = next.split(":");
                if (msg.length != 2) {
                    System.out.println("消息格式错误");
                } else {
                    writer.write(next + "\n");
                    writer.flush();
                }
            }
        }
    }


    /**
     * 接收消息
     * @param reader
     */
    public void receive(BufferedReader reader) {
        new Thread(() -> {

            while (true) {
                try {
                    String msg = "";
                    if (((msg = reader.readLine()) != null)) {
                        System.err.println(msg);
                    };
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        BioClient bioClient = new BioClient("localhost", 8320);

        bioClient.start();
    }

}
