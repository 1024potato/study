package cn.kj120.study.net.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

    private String formUid;

    private String toUid;

    private String content;

    public String getWriteMessage() {
        return String.format("客户端[%s]: %s", formUid, content);
    }
}
