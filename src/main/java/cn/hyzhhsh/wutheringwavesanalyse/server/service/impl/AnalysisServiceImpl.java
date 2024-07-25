package cn.hyzhhsh.wutheringwavesanalyse.server.service.impl;

import cn.hyzhhsh.wutheringwavesanalyse.common.constant.CardPoolConstant;
import cn.hyzhhsh.wutheringwavesanalyse.common.exception.BaseException;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.CardPoolUpItem;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.ConveneItem;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.ConveneSummary;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.RareConveneItem;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.vo.AnalysisVO;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.CardPoolUpMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.ConveneMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.ConveneSummaryMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.RareConveneMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.service.AnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    ConveneMapper conveneMapper;

    @Autowired
    RareConveneMapper rareConveneMapper;

    @Autowired
    ConveneSummaryMapper conveneSummaryMapper;

    @Autowired
    CardPoolUpMapper cardPoolUpMapper;

    /**
     * 更新分析数据
     *
     * @param playerId
     */
    @Override
    @Transactional
    public void update(Long playerId) {
        log.info("唤取分析：{}", playerId);

        //先清空已有的分析数据（各卡池出五星记录和总结）
        log.info("清除已有数据...");
        rareConveneMapper.delete(playerId);
        conveneSummaryMapper.delete(playerId);

        //从数据库中获取所有唤取数据，此数据已经按照timeKey从小到大排序
        log.info("读取唤取数据并分类处理...");
        List<ConveneItem> conveneItems = conveneMapper.select(playerId);
        if (conveneItems.isEmpty()) throw new BaseException(playerId + "无唤取记录");

        //对唤取记录按照卡池分类
        HashMap<Integer, List<ConveneItem>> classifiedConveneItems = new HashMap<>();
        for (ConveneItem conveneItem : conveneItems) {
            int cardPoolIndex = CardPoolConstant.nameToIndex.get(conveneItem.getCardPoolType());
            if (!classifiedConveneItems.containsKey(cardPoolIndex))
                classifiedConveneItems.put(cardPoolIndex, new ArrayList<>());
            classifiedConveneItems.get(cardPoolIndex).add(conveneItem);
        }

        //存储分析结果的对象
        HashMap<Integer, List<RareConveneItem>> classifiedRareConveneItems = new HashMap<>();
        ConveneSummary conveneSummary = ConveneSummary.builder().playerId(playerId).cardPoolCost(new HashMap<>()).cardPoolGet(new HashMap<>()).cardPoolTarget(new HashMap<>()).cardPoolPrepared(new HashMap<>()).build();

        log.info("读取角色up记录");
        List<CardPoolUpItem> cardPoolUpItems = cardPoolUpMapper.selectByResourceType("角色");

        //处理活动卡池和常驻卡池
        for (int cardPoolIndex = 1; cardPoolIndex <= 4; cardPoolIndex++) {
            log.info("正在处理第{}个卡池：{}...", cardPoolIndex, CardPoolConstant.indexToName.get(cardPoolIndex));
            classifiedRareConveneItems.put(cardPoolIndex, new ArrayList<>());

            //如果卡池为空（未唤取过该卡池）则跳过
            if (!classifiedConveneItems.containsKey(cardPoolIndex)) {
                conveneSummary.getCardPoolCost().put(cardPoolIndex, 0);
                conveneSummary.getCardPoolGet().put(cardPoolIndex, 0);
                conveneSummary.getCardPoolTarget().put(cardPoolIndex, 0);
                conveneSummary.getCardPoolPrepared().put(cardPoolIndex, 0);
                log.info("此卡池无唤取记录，跳过...");
                continue;
            }

            int prepared = 0, targetNum = 0;
            for (ConveneItem conveneItem : classifiedConveneItems.get(cardPoolIndex)) {
                prepared++;
                if (conveneItem.getQualityLevel() != 5) continue;

                //出五星的处理逻辑
                targetNum++;
                RareConveneItem rareConveneItem = new RareConveneItem();
                BeanUtils.copyProperties(conveneItem, rareConveneItem);
                rareConveneItem.setCost(prepared);

                //抽取角色精准调谐池子时判断是否歪卡
                if (cardPoolIndex == 1) {
                    //可能会有up池在时间上重合，此处逻辑为默认视为歪卡，若能匹配上up角色则去除歪卡标识
                    rareConveneItem.setTarget(false);
                    targetNum--;

                    for (CardPoolUpItem cardPoolUpItem : cardPoolUpItems) {
                        if (conveneItem.getTime().isAfter(cardPoolUpItem.getEndTime())) continue;
                        if ((conveneItem.getTime().isEqual(cardPoolUpItem.getStartTime())
                                || conveneItem.getTime().isAfter(cardPoolUpItem.getStartTime()))
                                && (conveneItem.getTime().isBefore(cardPoolUpItem.getEndTime())
                                || conveneItem.getTime().isEqual(cardPoolUpItem.getEndTime())))
                            if (conveneItem.getName().equals(cardPoolUpItem.getName())) {
                                rareConveneItem.setTarget(true);
                                targetNum++;
                                break;
                            }
                    }
                }

                classifiedRareConveneItems.get(cardPoolIndex).add(rareConveneItem);
                prepared = 0;
            }

            conveneSummary.getCardPoolCost().put(cardPoolIndex, classifiedConveneItems.get(cardPoolIndex).size());
            conveneSummary.getCardPoolGet().put(cardPoolIndex, classifiedRareConveneItems.get(cardPoolIndex).size());
            conveneSummary.getCardPoolTarget().put(cardPoolIndex, targetNum);
            conveneSummary.getCardPoolPrepared().put(cardPoolIndex, prepared);
            log.info("第{}个卡池：{}处理完毕", cardPoolIndex, CardPoolConstant.indexToName.get(cardPoolIndex));
        }

        //处理一次性卡池
        for (int cardPoolIndex = 5; cardPoolIndex <= 7; cardPoolIndex++) {
            log.info("正在处理第{}个卡池：{}...", cardPoolIndex, CardPoolConstant.indexToName.get(cardPoolIndex));
            classifiedRareConveneItems.put(cardPoolIndex, new ArrayList<>());

            //如果卡池为空（未唤取过该卡池）则跳过
            if (!classifiedConveneItems.containsKey(cardPoolIndex)) {
                conveneSummary.getCardPoolCost().put(cardPoolIndex, 0);
                conveneSummary.getCardPoolGet().put(cardPoolIndex, 0);
                conveneSummary.getCardPoolTarget().put(cardPoolIndex, 0);
                conveneSummary.getCardPoolPrepared().put(cardPoolIndex, 0);
                log.info("此卡池无唤取记录，跳过...");
                continue;
            }

            int cost = 0;
            for (ConveneItem conveneItem : classifiedConveneItems.get(cardPoolIndex)) {
                cost++;
                if (conveneItem.getQualityLevel() != 5) continue;

                //出五星的处理逻辑
                RareConveneItem rareConveneItem = new RareConveneItem();
                BeanUtils.copyProperties(conveneItem, rareConveneItem);
                rareConveneItem.setCost(cost);
                classifiedRareConveneItems.get(cardPoolIndex).add(rareConveneItem);
            }

            conveneSummary.getCardPoolCost().put(cardPoolIndex, classifiedConveneItems.get(cardPoolIndex).size());
            conveneSummary.getCardPoolGet().put(cardPoolIndex, classifiedRareConveneItems.get(cardPoolIndex).size());
            conveneSummary.getCardPoolTarget().put(cardPoolIndex, classifiedRareConveneItems.get(cardPoolIndex).size());
            conveneSummary.getCardPoolPrepared().put(cardPoolIndex, 0);
            log.info("第{}个卡池：{}处理完毕", cardPoolIndex, CardPoolConstant.indexToName.get(cardPoolIndex));
        }

        //所有卡池处理完后准备写入数据库
        log.info("所有卡池处理完毕，写入数据...");
        //存储出五星的记录
        List<RareConveneItem> rareConveneItems = classifiedRareConveneItems.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        rareConveneMapper.insertBatch(rareConveneItems);
        //存储唤取总结
        conveneSummaryMapper.insert(conveneSummary);
    }

    /**
     * 查询分析数据
     *
     * @param playerId
     * @return
     */
    @Override
    public AnalysisVO select(Long playerId) {
        //查询出五星的记录，此数据已经按照timeKey从大到小排序
        List<RareConveneItem> rareConveneItemList = rareConveneMapper.select(playerId);
        Map<Integer, List<RareConveneItem>> rareConveneItems = new HashMap<>();
        for (RareConveneItem rareConveneItem : rareConveneItemList) {
            Integer cardPoolIndex = CardPoolConstant.nameToIndex.get(rareConveneItem.getCardPoolType());
            if (!rareConveneItems.containsKey(cardPoolIndex)) rareConveneItems.put(cardPoolIndex, new ArrayList<>());
            rareConveneItems.get(cardPoolIndex).add(rareConveneItem);
        }

        //查询唤取总结
        ConveneSummary conveneSummary = conveneSummaryMapper.select(playerId);

        return AnalysisVO.builder().playerId(playerId).rareConveneItems(rareConveneItems).conveneSummary(conveneSummary).build();
    }
}
