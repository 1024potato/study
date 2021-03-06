package cn.kj120.study.io.http;

import lombok.Data;

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

    private String body = "";

    public Request(String message) {
        if (message == null) {
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
        String msg = "POST http://app-test.qizhidao.com/qzd-bff-pcweb/project/findShopResult HTTP/1.1\n" +
                "User-Agent: X/21233222\n" +
                "accessToken: eyJhbGciOiJIUzUxMiJ9.ZXlKNmFYQWlPaUpFUlVZaUxDSmhiR2NpT2lKa2FYSWlMQ0psYm1NaU9pSkJNVEk0UTBKRExVaFRNalUySW4wLi5tdGVXUVFodGxFNi1Na05YVFpfTHJRLnZJZ2t4cDZIYVNXa1RLdGkxZUtxcXd1aG9mNWVZQmZMcC1Vc05ZSG9jMHRZVWhtWVkzZGx6QTBIMjJmOE1mdVlsTHg5RzB3RmNJYkh6SF9ST3d2cWprWHJ6RHA5dmxRR3FNa2VJZmpiY1l3eFNjQ1ltZ2c3Yk1pVE1XY0c3SVBzNEJ2X3E5MkVFN2VYemxTN0NQR0hrNWRoYkx1TklFMmRfRjlta2xRRGtYekU2d3doMzF2VFc1S3J4Q1ZsYjU3VG9QN2hURjlPQXkzTHBwRGNybVVnaHh3MUxYTHltaHBiT2N4anNaeEhsSno0c29rMVBQcEhOQzdGQktBNGJVaHoxdGUxNTQxQXM3bkE1MTdRdVZ1SnhTcUNPemM2YVNUVGZBZHB3NDZnR214dXdaVVJ1RV82eXM1R1ZneEpUS0tXLmM0RVJia19DYmN4aFZ4QjY4TDJDWVE.q3X5Zr8hem2MtEkLZ82XiDUviL1LiJCS2jsLktacA7dltGwRRepVUXGEPbI5ZTPW7Bbj7rBHRDwpHsJj9OybZw\n" +
                "signature: admin.123456\n" +
                "Content-Type: application/json\n" +
                "Accept: */*\n" +
                "Cache-Control: no-cache\n" +
                "Postman-Token: bd2eb018-2beb-4b42-af55-5279312b468d\n" +
                "Host: app-test.qizhidao.com\n" +
                "Accept-Encoding: gzip, deflate, br\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 1506\n" +
                "\n" +
                "{\n" +
                "    \"project_lists\": [\n" +
                "        {\n" +
                "            \"prob\": 0.5,\n" +
                "            \"project_id\": \"336546603777130496\",\n" +
                "            \"subsidy\": [\n" +
                "                27.483801246488778,\n" +
                "                32.5\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"prob\": 0.5,\n" +
                "            \"project_id\": \"332863668130680832\",\n" +
                "            \"subsidy\": [\n" +
                "                0.0,\n" +
                "                0.0\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"prob\": 0.5,\n" +
                "            \"project_id\": \"333561265820536832\",\n" +
                "            \"subsidy\": [\n" +
                "                0.0,\n" +
                "                0.0\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"prob\": 0.5,\n" +
                "            \"project_id\": \"333564720807153664\",\n" +
                "            \"subsidy\": [\n" +
                "                0.0,\n" +
                "                0.0\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"prob\": 0.5,\n" +
                "            \"project_id\": \"332848417955909632\",\n" +
                "            \"subsidy\": [\n" +
                "                0.0,\n" +
                "                0.0\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"prob\": 0.5,\n" +
                "            \"project_id\": \"331130754107969536\",\n" +
                "            \"subsidy\": [\n" +
                "                0.0,\n" +
                "                0.0\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"prob\": 0.5,\n" +
                "            \"project_id\": \"336546603617746944\",\n" +
                "            \"subsidy\": [\n" +
                "                0.0,\n" +
                "                0.0\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"prob\": 0.5,\n" +
                "            \"project_id\": \"310838019807514624\",\n" +
                "            \"subsidy\": [\n" +
                "                0.0,\n" +
                "                0.0\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        Request request = new Request(msg);

        System.err.println(request);
    }

}
