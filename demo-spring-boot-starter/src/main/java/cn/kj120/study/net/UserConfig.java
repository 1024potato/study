package cn.kj120.study.net;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ConfigurationProperties(prefix = "potato")
public class UserConfig {

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 列表
     */
    private List<Integer> list;

    /**
     * 数组
     */
    private Integer[] array;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public Integer[] getArray() {
        return array;
    }

    public void setArray(Integer[] array) {
        this.array = array;
    }
}
