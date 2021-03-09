package cn.kj120.study.io.http;

import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
public class Request {

    private String uri;

    private String url;

    private String version;

    private String method;

    private Map<String, String> headerMap = new HashMap<>();

    private Map<String, String> parameterMap = new HashMap<>();

    private InputStream inputStream = new ByteArrayInputStream(new byte[2048]);

    private String body = "";

    public Request(String message) {
        if (message == null || "".equals(message)) {
            return;
        }
        String[] split = message.split("\n");

        String[] protocol = split[0].split(" ");

        method = protocol[0];
        url = protocol[1];

        String[] urlsplit = url.split("\\?");
        uri = url;

        if (urlsplit.length > 1) {
            String[] getSplit = urlsplit[1].split("&");

            for (int i = 0; i < getSplit.length; i++) {
                String[] data = getSplit[i].split("=");
                parameterMap.put(data[0], data[1]);
            }
        }



        version = protocol[2];


        int bodyStartIndex = 0;

        for (int i = 1; i < split.length; i++) {
            if ("\r".equals(split[i]) && bodyStartIndex == 0) {
                bodyStartIndex = i + 1;
            } if (bodyStartIndex != 0) {
                body += split[i];
            } else {
                String[] header = split[i].split(":");
                headerMap.put(header[0], header[1].substring(1));
            }
        }

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

    public static void main(String[] args) {

        Charset charset = Charset.forName("utf-8");

        ByteBuffer encode = charset.encode("腾通通（深圳）科技企业");
        ByteBuffer encode1 = charset.encode("测试1");
        ByteBuffer encode2 = charset.encode("测试1a");
        System.err.println(encode);
        System.err.println(encode1);
        System.err.println(encode2);
    }

}
