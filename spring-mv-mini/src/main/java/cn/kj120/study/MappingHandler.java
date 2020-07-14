package cn.kj120.study;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class MappingHandler {

    private Pattern urlPattern;

    private String url;

    private Method method;

    private Object controller;

    private Object[] parameters;

    private Map<String,Integer> parameterMap;

    public MappingHandler() {
    }

    public MappingHandler(Method method, Object controller) {
        this.method = method;
        this.controller = controller;
        initUrl();
        initParameter();
    }

    private void initParameter() {
        Parameter[] param = method.getParameters();
        if (param.length == 0) {
            return;
        }
        parameters = new Object[param.length];
        for (int i = 0; i < param.length; i++) {
            parameters[i] = param[i].getType();
        }

        // 设置键值对应的顺序
        parameterMap = new ConcurrentHashMap<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int j = 0; j < parameterAnnotations[i].length; j++) {
                Annotation annotation = parameterAnnotations[i][j];
                if (annotation.annotationType().equals(PRequestParam.class)) {
                    PRequestParam pRequestParam = (PRequestParam) parameterAnnotations[i][j];
                    parameterMap.put(pRequestParam.value(), i);
                }
            }
        }

    }

    private void initUrl() {
        StringBuffer sb = new StringBuffer();
        PRequestMapping annotation = controller.getClass().getAnnotation(PRequestMapping.class);


        if (annotation != null && !"".equals(annotation.value())) {
            if (!annotation.value().startsWith("/")) {
                sb.append("/");
            }
            sb.append(annotation.value());
        }

        PRequestMapping annotation1 = method.getAnnotation(PRequestMapping.class);
        if (annotation1 != null && !"".equals(annotation1.value())) {
            if (!annotation1.value().startsWith("/")) {
                sb.append("/");
            }
            sb.append(annotation1.value());
        }

        url = sb.toString();
        System.err.println("url 地址:" + url);
        urlPattern = Pattern.compile(url);
    }

    public Pattern getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(Pattern urlPattern) {
        this.urlPattern = urlPattern;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Map<String, Integer> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, Integer> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public boolean equals(Object o) {
       return url.equals(o);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public String toString() {
        return "MappingHandler{" +
                "urlPattern=" + urlPattern +
                ", url='" + url + '\'' +
                ", method=" + method +
                ", controller=" + controller +
                ", parameters=" + Arrays.toString(parameters) +
                ", parameterMap=" + parameterMap +
                '}';
    }
}
