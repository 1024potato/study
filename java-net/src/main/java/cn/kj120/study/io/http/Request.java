package cn.kj120.study.io.http;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Request {

    private String path;

    private String version;

    private String method;

    private Map<String, String> headerMap = new HashMap<>();

    private Map<String, String> parameterMap = new HashMap<>();

    private String body;

    public Request(String message) {

    }

    public String getHeader(String name) {
        return headerMap.get(name);
    }

    public void setHeader(String name, String value) {
        headerMap.put(name, value);
    }

    public Set<String> getHeaders() {
        return headerMap.keySet();
    }

    public String getParameter(String name) {
        return parameterMap.get(name);
    }

    public void setParameter(String name, String value) {
        parameterMap.put(name, value);
    }

    public Set<String> getParameters() {
        return parameterMap.keySet();
    }

}
