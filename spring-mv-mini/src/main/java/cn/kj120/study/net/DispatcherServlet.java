package cn.kj120.study.net;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DispatcherServlet extends HttpServlet {

    private Map<String, Object> ioc = new ConcurrentHashMap<>(64);

    private List<String> clazzs = new LinkedList<>();

    private Properties properties;

    private Set<MappingHandler> mappingHandlers = new HashSet<>();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object result = dispatcher(req, resp);

        resp.getWriter().println(result.toString());
    }

    private Object dispatcher(HttpServletRequest req, HttpServletResponse resp) {
        // 全路径
        String uri = req.getRequestURI();

        String contextPath = req.getContextPath();

        // 替换成requestMapping 路径
        String urlPath = uri.replace(contextPath, "");

        if (mappingHandlers.isEmpty()) {
            return "404 Page Not Found!";
        }

        boolean isMatch = false;

        // 匹配指定的MappingHandler并且执行
        for (MappingHandler handler : mappingHandlers) {
            if (handler.getUrlPattern().matcher(urlPath).matches()) {
               return invokeHandler(handler, req, resp);
            }
        }

        if (!isMatch) {
            return "404 Page Not Found!";
        }

        return "404 Page Not Found!";
    }

    @Override
    public void init() throws ServletException {
        // 加载配置
        loadConfig();

        // 扫描包
        scanPackage(properties.getProperty("scanPackage"));

        // 实例化并注册到ioc容器
        initInstance();
        
        // 依赖注入
        autowired();
        
        // 初始web容器
        initMappingHandler();

    }

    /**
     * 加载配置文件
     */
    private void loadConfig() {
        ServletConfig servletConfig = getServletConfig();
        String configName = servletConfig.getInitParameter("contextConfigLocation");

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configName);

        properties = new Properties();

        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("配置文件初始化异常");
        }
    }

    /**
     * 扫描知道包路径的下类全路径
     * @param basePackage
     */
    private void scanPackage(String basePackage) {
        // 未设置扫描路径时默认为classPath
        if (basePackage == null) {
            basePackage = "";
        }
        URL resource = getClass().getClassLoader().getResource("");
        String path = basePackage.replaceAll("\\.", "/");
        path = resource.getPath() + "/" + path;
        File dir = new File(path);

        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                String s = basePackage + "." + file.getName().replace(".class", "");
                //System.err.println("扫描到类： " + s);
                // 获取class完整类名
                clazzs.add(s);
            } else if (file.isDirectory()) {
                scanPackage(basePackage + "." + file.getName());
            }

        }

    }

    /**
     * 实例化扫描到的包到ioc容器
     */
    private void initInstance() {
        if (clazzs.isEmpty()) {
            return;
        }

        for (String clazz : clazzs) {

            try {
                Class<?> aClass = Class.forName(clazz);

                // 标注PCompoment注解的内需要实例化化到ioc
                PCompoment annotation = aClass.getAnnotation(PCompoment.class);
                if (annotation == null) {
                    continue;
                }
                Object instance = aClass.newInstance();

                // 获取到入ioc容器的名称
                String beanName = tolowerCase(aClass.getSimpleName());
                if (ioc.containsKey(beanName)) {
                    throw new RuntimeException("ioc容器存在重复的名称： " + beanName);
                }
                if (!"".equals(annotation.value())) {
                    beanName = annotation.value();
                }

                // 放入ioc容器
                ioc.put(beanName, instance);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DI 依赖注入
     */
    private void autowired() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Object value = entry.getValue();
            Field[] declaredFields = value.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                // 标注PAutowired注解的字段才需要注入值
                PAutowired annotation = field.getAnnotation(PAutowired.class);
                if (annotation == null) {
                    continue;
                }
                String beanName = annotation.value();
                if ("".equals(beanName)) {
                    beanName = field.getName();
                }
                Object o = ioc.get(beanName);

                if (o == null) {
                    throw new RuntimeException("容器中不存在bean: " + beanName + "  无法完成注入");
                }
                field.setAccessible(true);
                try {
                    field.set(value, o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化web容器
     */
    private void initMappingHandler() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Object value = entry.getValue();
            // 标记PController注解为web控制器
            if (value.getClass().isAnnotationPresent(PController.class)) {
                Method[] methods = value.getClass().getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(PRequestMapping.class)) {
                        continue;
                    }
                    // 生产指定的处理器
                    MappingHandler mappingHandler = new MappingHandler(method, value);
                    if (mappingHandlers.contains(mappingHandler)) {
                        throw new RuntimeException("url地址存在重复:  " + mappingHandler);
                    }
                    mappingHandlers.add(mappingHandler);
                }
            }
        }
    }

    /**
     * 执行请求
     * @param mappingHandler
     * @param req
     * @param resp
     * @return
     */
    private Object invokeHandler(MappingHandler mappingHandler, HttpServletRequest req, HttpServletResponse resp) {
        // 形参列表
        Object[] parameters = mappingHandler.getParameters();

        // 参数名称和对应的新参数组为，用户查询新参类型进行转换成实参数组
        Map<String, Integer> parameterMap = mappingHandler.getParameterMap();
        Map<String, String[]> reqMap = req.getParameterMap();

        Method method = mappingHandler.getMethod();

        // 生成实参数组
        Object[] param = null;
        if (parameters != null) {
            param = new Object[parameters.length];

            for (Map.Entry<String, Integer> entry : parameterMap.entrySet()) {
                String[] strings = reqMap.get(entry.getKey());
                Object paramVal = convert(strings[0], (Class<?>) parameters[entry.getValue()]);
                param[entry.getValue()] = paramVal;
            }
        }

        try {
            return method.invoke(mappingHandler.getController(), param);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return "500 server error";
    }

    /**
     * 转换类型
     * @param o
     * @param clazz
     * @return
     */
    private Object convert(Object o, Class<?> clazz) {
        if (o == null) {

        } else if (clazz == HttpServletRequest.class) {
            o = (HttpServletRequest) o;
        } else if (clazz == HttpServletResponse.class) {
            o = (HttpServletResponse) o;
        } else if (clazz == Integer.class) {
            o = Integer.valueOf(o.toString());
        }

        return o;
    }

    /**
     * 首字符转小写
     * @param name
     * @return
     */
    private String tolowerCase(String name) {
        char[] chars = name.toCharArray();
        char c = chars[0];
        if (c >= 'A' && c <= 'Z') {
            c += 32;
        }
        chars[0] = c;
        return String.valueOf(chars);
    }
}
