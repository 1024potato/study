## 自定义jsonTypeHandler

[示例代码地址](https://github.com/1024potato/study/tree/master/mybatis-json-type-handler)

1.  编写类 JsonTypeHandler 继承 BaseTypeHandler
-   json序列化使用 Spring 自带 Jackson 包的 ObjectMapper
-   实现序列化方法 setNonNullParameter
-   实现三个反序列化方法 getNullableResult
```java
    /**
     * mybatis json字符串和对象间互转
     * @param <T>
     */
    @Slf4j
    public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
    
        private ObjectMapper mapper = new ObjectMapper();
    
        private Class<T> clazz;
    
        public JsonTypeHandler(Class<T> clazz) {
            this.clazz = clazz;
            // json字符串字段比对象多不报异常
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            // 为null字段不参与序列化
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            // 对象为空不抛出异常
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        }
    
        @Override
        public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
            try {
                ps.setString(i, mapper.writeValueAsString(parameter));
            } catch (JsonProcessingException e) {
                log.error("入库时格式化成json异常 {}", e);
            }
        }
    
        @Override
        public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
            return getResult(rs.getString(columnName));
        }
    
        @Override
        public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
            return getResult(rs.getString(columnIndex));
        }
    
        @Override
        public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
            return getResult(cs.getString(columnIndex));
        }
    
        private T getResult(String str) {
            if (str == null || "".equals(str.trim())) {
                return null;
            }
            try {
                return mapper.readValue(str, clazz);
            } catch (IOException e) {
                log.error("json字符串转对象异常 {}", e);
            }
            return null;
        }
    }
```
2.  使用 JsonTypeHandler 
-   查询时使用,XML ResultMap 加上 typeHandler="cn.kj120.init.mybatis.JsonTypeHandler" 属性
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.kj120.study.mybatis.dao.UserDao">
    <!-- 手动档   -->
    <resultMap id="UserDetail" type="cn.kj120.study.mybatis.domain.User">
        <id column="id" jdbcType="INTEGER" property="id" />
        <result column="account" jdbcType="VARCHAR" property="account" />
        <result column="nickname" jdbcType="VARCHAR" property="nickname" />
        <result column="mobile" jdbcType="CHAR" property="mobile" />
        <result column="avatar" jdbcType="VARCHAR" property="avatar" />
        <result column="password" jdbcType="VARCHAR" property="password" />
        <!--  cate 字段存储的json文本 需要指定typeHandler      -->
        <result column="cate" jdbcType="VARCHAR" property="cate" typeHandler="cn.kj120.study.mybatis.handler.CateTypeHandler" />
    </resultMap>

    <select id="getUserByIdTypeHandler" resultMap="UserDetail">
        select * from user where id = #{id}
    </select>

</mapper>
```

-   插入或更新时使用, {user.cate,typeHandler="cn.kj120.init.mybatis.JsonTypeHandler"}
```xml
   <!-- 手动档   -->
    <insert id="addTypeHandler">
        insert into user(nickname, cate) values (#{user.nickname}, #{user.cate, typeHandler=cn.kj120.study.mybatis.handler.CateTypeHandler})
    </insert>
```

3.  每次都要指定typeHandler太繁琐了,‘懒人’推动社会的进步。省略typeHandler,手动挡换自动挡
-   创建一个类实现 JsonTypeHandler 并且传入需要自动Json序列化类的类对象
```java
    public class CateTypeHandler extends JsonTypeHandler<Cate>{
        public CateTypeHandler() {
            // 传入需要自动Json序列化类的类对象
            super(Cate.class);
        }
    }
```
-   注册 CateTypeHandler, spring boot加上以下配置
```properties
# cn.kj120.init.mybatis 为 CateTypeHandler 类所在包路径
mybatis.type-handlers-package: cn.kj120.study.mybatis.handler
```
-  手动挡换自动挡后sql写法
```xml
    <!-- 自动挡   -->
    <select id="getUserById" resultType="cn.kj120.study.mybatis.domain.User">
        select * from user where id = #{id}
    </select>
```
```xml
    <!-- 自动挡   -->
    <insert id="add">
        insert into user(nickname, cate) values (#{user.nickname}, #{user.cate})
    </insert>
```

