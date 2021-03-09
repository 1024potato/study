package cn.kj120.study.io.http;

import lombok.Data;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Response {

    private Integer code;

    private String version;

    private Map<String, String> headerMap = new HashMap<>();

    private String body;

    private final String ENTER = "\n";

    private OutputStream outputStream;


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
        StringBuilder sb = new StringBuilder();
        sb.append(version)
                .append(" ")
                .append(code)
                .append(" OK")
                .append(ENTER);
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            sb.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append(ENTER);
        }

        sb.append(ENTER);
        sb.append(body);

        return sb.toString();
    }
}
