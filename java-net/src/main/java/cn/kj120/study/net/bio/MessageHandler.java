package cn.kj120.study.net.bio;

import cn.kj120.study.net.entity.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Map;

@Slf4j
public class MessageHandler implements Runnable {

    private String uid;

    private Map<String, BufferedWriter> writerMap;

    private Socket socket;

    private static final String EXIT_CONTENT = "exit";

    private static final String ALL_CONTENT = "all";

    public MessageHandler(String uid, Map<String, BufferedWriter> writerMap, Socket socket) {
        this.uid = uid;
        this.writerMap = writerMap;
        this.socket = socket;
    }


    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
        ) {
            // 存放在线的uid和客户端对应的BufferedWriter
            writerMap.put(uid, writer);

            log.info("客户端[{}]已经连接, 当前客户端总数[{}]", uid, writerMap.size());

            // 循环查看输入流是否有新的消息
            while (true) {
                String msg = null;
                if ((msg = reader.readLine()) != null) {

                    // 解析接收到的消息
                    Message message = receive(msg, uid);
                    if (message == null) {
                        continue;
                    }

                    if (isAllMessage(message)) {
                        sendAll(message);
                    } else {
                        send(message);
                    }

                    // 检查是否为退出消息
                    if (isExit(message)) {
                        // 客户端退出从集合中删除
                        writerMap.remove(uid);
                        socket.close();
                        log.info("客户端[{}]已经退出", uid);
                        break;
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析接收到的消息
     * @param msg
     * @param formUid
     * @return
     */
    public Message receive(String msg, String formUid) {
        String[] s = msg.split(":");
        if (s.length != 2) {
            log.info("消息格式错误 {}", msg);
            return null;
        }
        Message message = new Message(formUid, s[0], s[1]);
        log.info("接收到的消息: {}", message);
        return message;
    }

    /**
     * 发送给所有的客户端
     * @param message
     * @throws IOException
     */
    public void sendAll(Message message) throws IOException {
        for (Map.Entry<String, BufferedWriter> entry : writerMap.entrySet()) {
            if (!message.getFormUid().equals(entry.getKey())) {
                BufferedWriter writer = entry.getValue();
                writerAndFlush(writer, message.getWriteMessage());
            }
        }
    }

    /**
     * 发送给指定客户端
     * @param message
     * @throws IOException
     */
    public void send(Message message) throws IOException {
        BufferedWriter writer = writerMap.get(message.getToUid());
        if (writer == null) {
            String noticeMsg = String.format("客户端[%s]已经断开连接", message.getToUid());
            log.warn(noticeMsg);
            writerAndFlush(writerMap.get(message.getFormUid()), noticeMsg);

        } else {
            writerAndFlush(writer, message.getWriteMessage());
        }

    }

    /**
     * 是否为发送给全部客户端的消息
     * @param message
     * @return
     */
    public boolean isAllMessage(Message message) {
        if (ALL_CONTENT.equals(message.getToUid())) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为退出命令
     * @param message
     * @return
     */
    public boolean isExit(Message message) {
        if (EXIT_CONTENT.equals(message.getContent())) {
            return true;
        }
        return false;
    }

    /**
     * 写入消息
     * @param writer
     * @param msg
     * @throws IOException
     */
    public void writerAndFlush(BufferedWriter writer, String msg) throws IOException {
        writer.write(msg + "\n");
        writer.flush();
    }
}
