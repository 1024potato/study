package cn.kj120.study.net.io.entity;

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
}
