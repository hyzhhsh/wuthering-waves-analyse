package cn.hyzhhsh.wutheringwavesanalyse.server.handler;

import cn.hyzhhsh.wutheringwavesanalyse.common.json.JacksonObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * 自定义TypeHandler，用于数据库中JSON字符串和HashMap<Integer, Integer>的序列化和反序列化
 */

//@MappedJdbcTypes(JdbcType.VARCHAR)
//@MappedTypes(HashMap.class)
@Component
public class JsonTypeHandler extends BaseTypeHandler<HashMap<Integer, Integer>> {

    @Autowired
    JacksonObjectMapper jacksonObjectMapper;

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, HashMap<Integer, Integer> integerIntegerHashMap, JdbcType jdbcType) throws SQLException {
        try {
            preparedStatement.setString(i, jacksonObjectMapper.writeValueAsString(integerIntegerHashMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<Integer, Integer> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return parseJson(resultSet.getString(s));
    }

    @Override
    public HashMap<Integer, Integer> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return parseJson(resultSet.getString(i));
    }

    @Override
    public HashMap<Integer, Integer> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return parseJson(callableStatement.getString(i));
    }

    private HashMap<Integer, Integer> parseJson(String json) {
        try {
            if (json != null) {
                return jacksonObjectMapper.readValue(json, HashMap.class);
            }
            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
