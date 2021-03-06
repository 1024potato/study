package cn.kj120.study.io.entity;

import lombok.Data;

@Data
public class Message {

    private Integer fromUid;

    private Integer toUid;

    /**
     * 0 单聊 1 群聊
     */
    private Integer type;

    private String content;

    public Message() {
    }

    public Message(Integer fromUid, Integer toUid, String content) {
        this.fromUid = fromUid;
        this.toUid = toUid;
        this.content = content;
    }
}
