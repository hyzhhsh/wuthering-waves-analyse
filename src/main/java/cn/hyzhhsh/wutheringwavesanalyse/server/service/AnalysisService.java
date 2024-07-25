package cn.hyzhhsh.wutheringwavesanalyse.server.service;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.vo.AnalysisVO;

public interface AnalysisService {
    /**
     * 更新分析数据
     *
     * @param playerId
     */
    void update(Long playerId);

    /**
     * 查询分析数据
     *
     * @param playerId
     * @return
     */
    AnalysisVO select(Long playerId);
}
