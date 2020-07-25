package cn.kj120.study.mybatis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
