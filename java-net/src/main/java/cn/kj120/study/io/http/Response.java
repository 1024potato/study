package cn.kj120.study.io.http;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Response {

    private Integer code;

    private String version;

    private Map<String, String> headerMap = new HashMap<>();

    private String body;


    public String getHeader(String name) {
        return headerMap.get(name);
    }

    public void setHeader(String name, String value) {
        headerMap.put(name, value);
    }

    public Set<String> getHeaders() {
        return headerMap.keySet();
    }

    public String httpReturn() {
        return null;
    }
}
