package cn.hyzhhsh.wutheringwavesanalyse.server.service.impl;

import cn.hyzhhsh.wutheringwavesanalyse.common.constant.CardPoolConstant;
import cn.hyzhhsh.wutheringwavesanalyse.common.exception.BaseException;
import cn.hyzhhsh.wutheringwavesanalyse.common.json.JacksonObjectMapper;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.dto.ApiResponse;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.dto.GetByApiDto;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.dto.GetByFileDto;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.ConveneItem;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.Role;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.ConveneMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.ConveneSummaryMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.RareConveneMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.RoleMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.service.AnalysisService;
import cn.hyzhhsh.wutheringwavesanalyse.server.service.ConveneService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ConveneServiceImpl implements ConveneService {
    @Autowired
    JacksonObjectMapper jacksonObjectMapper;

    @Autowired
    ConveneMapper conveneMapper;

    @Autowired
    RareConveneMapper rareConveneMapper;

    @Autowired
    ConveneSummaryMapper conveneSummaryMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    AnalysisService analysisService;

    /**
     * 从接口获取唤取记录
     *
     * @param getByApiDto
     * @return
     */
    @SneakyThrows
    @Override
    public int getByApi(GetByApiDto getByApiDto) {
        //获取角色信息并存储
        Role role = new Role();
        BeanUtils.copyProperties(getByApiDto, role);

        String url = "https://gmserver-api.aki-game2.com/gacha/record/query";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<ConveneItem> allConveneItems = new ArrayList<>();

        for (int cardPoolIndex = 1; cardPoolIndex <= 7; cardPoolIndex++) {
            log.info("正在处理第{}个卡池：{}...", cardPoolIndex, CardPoolConstant.indexToName.get(cardPoolIndex));

            //构造请求体
            ObjectNode jsonObject = jacksonObjectMapper.createObjectNode()
                    .put("playerId", role.getPlayerId())
                    .put("cardPoolId", role.getCardPoolId())
                    .put("serverId", role.getServerId())
                    .put("languageCode", "zh-Hans")
                    .put("recordId", role.getRecordId())
                    .put("cardPoolType", cardPoolIndex);
            String json = jacksonObjectMapper.writeValueAsString(jsonObject);
            log.info("{}", json);
            //设置请求头和请求体
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            //发送请求
            CloseableHttpResponse response = httpClient.execute(httpPost);

            //解析请求体
            String body = EntityUtils.toString(response.getEntity());
            ApiResponse apiResponse = jacksonObjectMapper.readValue(body, ApiResponse.class);

            int statusCode = apiResponse.getCode();
            if (statusCode != 0)
                throw new BaseException("接口获取角色信息失败。参考消息：" + apiResponse.getMessage());
            List<ConveneItem> conveneItems = apiResponse.getData();
//        if (conveneItems.isEmpty())
//            throw new BaseException("接口获取信息为空，可能是此卡池无唤取记录或recordId填写有误");

            //合并获取到的唤取信息
            allConveneItems.addAll(conveneItems);
            response.close();
            log.info("第{}个卡池：{}处理完毕，此卡池获取到{}条唤取记录", cardPoolIndex, CardPoolConstant.indexToName.get(cardPoolIndex), conveneItems.size());
        }

        httpClient.close();

        //更新角色信息
        log.info("更新角色信息");
        roleMapper.update(role);

        //更新唤取信息
        return update(allConveneItems, role.getPlayerId());
    }

    /**
     * 从文件获取唤取记录
     *
     * @param getByFileDto
     * @return
     */
    @SneakyThrows
    @Override
    public int getByFile(GetByFileDto getByFileDto) {
        String jsonString = new String(Files.readAllBytes(Paths.get(getByFileDto.getUrl())));
        ApiResponse apiResponse = jacksonObjectMapper.readValue(jsonString, ApiResponse.class);
        List<ConveneItem> conveneItems = apiResponse.getData();
        //更新唤取信息
        return update(conveneItems, getByFileDto.getPlayerId());
    }

    /**
     * 更新唤取记录
     *
     * @param conveneItems
     * @param playerId
     * @return
     */
    @Override
    @Transactional
    public int update(List<ConveneItem> conveneItems, Long playerId) {
        log.info("更新唤取记录：{}", playerId);

        //如果角色不在数据库中则不更新
        Role role = roleMapper.select(playerId);
        if (role == null) throw new BaseException("角色" + playerId + "尚未添加");

        //获取到的唤取记录默认为从新到旧，需要倒序
        Collections.reverse(conveneItems);

        //若唤取记录不包含timeKey则为其设置并校验唤取记录合法性
        long preTime = 0, cnt = 1;
        for (ConveneItem conveneItem : conveneItems) {
            if (conveneItem.getPlayerId() != 0 && conveneItem.getPlayerId() != playerId)
                throw new BaseException("唤取记录与导入的目标角色不符");
            conveneItem.setPlayerId(playerId);
            long time = Long.parseLong(conveneItem.getTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            if (time == preTime) cnt++;
            else {
                if (cnt != 1 && cnt != 10) throw new BaseException("时间点" + time + "处有唤取记录缺失");
                preTime = time;
                cnt = 1;
            }
            if (cnt > 10) throw new BaseException("时间点" + time + "处存在超过10条唤取记录");
            long timeKey = time * 100 + cnt;
            if (conveneItem.getTimeKey() != 0 && conveneItem.getTimeKey() != timeKey)
                throw new BaseException("timeKey为" + conveneItem.getTimeKey() + "的唤取记录与系统计算的timeKey不符");
            conveneItem.setTimeKey(timeKey);
        }

        //查询该角色在数据库中已存在的唤取记录
        List<ConveneItem> conveneItemsFromDataBase = conveneMapper.select(playerId);
        Set<Long> timeKeys = conveneItemsFromDataBase.stream().map(ConveneItem::getTimeKey).collect(Collectors.toSet());

        //过滤已存在的唤取记录
        ArrayList<ConveneItem> targetConveneItems = new ArrayList<>();
        for (ConveneItem conveneItem : conveneItems)
            if (!timeKeys.contains(conveneItem.getTimeKey())) targetConveneItems.add(conveneItem);
        log.info("过滤后剩余{}条待插入的唤取记录", targetConveneItems.size());

        int insertedRows = targetConveneItems.isEmpty() ? 0 : conveneMapper.insert(targetConveneItems);
        if (insertedRows > 0) analysisService.update(playerId);
        return insertedRows;
    }

    /**
     * 删除唤取记录
     *
     * @param playerId
     */
    @Override
    @Transactional
    public void delete(Long playerId) {
        //删除唤取记录时，出五星的记录和唤取总结也要一并删除
        conveneMapper.delete(playerId);
        rareConveneMapper.delete(playerId);
        conveneSummaryMapper.delete(playerId);
    }

    /**
     * 导出唤取记录
     *
     * @param playerId
     * @return
     */
    @SneakyThrows
    @Override
    public String export(Long playerId) {
        //查询唤取记录，由于查询出的记录是按照timeKey从小到大（从早到晚）排序，需要手动反序
        List<ConveneItem> conveneItems = conveneMapper.select(playerId);
        Collections.reverse(conveneItems);

        ApiResponse apiResponse = ApiResponse.builder().code(0).message("success").data(conveneItems).build();
        return jacksonObjectMapper.writeValueAsString(apiResponse);
    }
}
