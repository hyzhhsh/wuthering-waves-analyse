package cn.hyzhhsh.wutheringwavesanalyse.common.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 自定义反序列化器，用于数据库中JSON字符串转HashMap<Integer, Integer>
 */
public class HashMapDeserializer extends JsonDeserializer<HashMap<Integer, Integer>> {
    @Override
    public HashMap<Integer, Integer> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        HashMap<Integer, Integer> map = new HashMap<>();
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);
        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            map.put(Integer.parseInt(entry.getKey()), entry.getValue().asInt());
        }
        return map;
    }
}
