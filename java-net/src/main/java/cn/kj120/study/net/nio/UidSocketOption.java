package cn.kj120.study.net.nio;


import java.net.SocketOption;

public class UidSocketOption<T> implements SocketOption<T> {

    private String name;

    private Class<T> clazz;

    public UidSocketOption(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Class<T> type() {
        return clazz;
    }
}
