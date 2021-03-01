package cn.kj120.study.net;

public class DemoService {

    private UserConfig userConfig;

    public DemoService(UserConfig userConfig) {
        this.userConfig = userConfig;
    }

    public String hello(String name) {
        return "hello " + name;
    }
}
