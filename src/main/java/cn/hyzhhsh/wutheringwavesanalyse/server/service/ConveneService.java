package cn.hyzhhsh.wutheringwavesanalyse.server.service;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.dto.GetByApiDto;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.dto.GetByFileDto;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.ConveneItem;

import java.util.List;

public interface ConveneService {

    /**
     * 从接口获取唤取记录
     *
     * @param getByApiDto
     * @return
     */
    int getByApi(GetByApiDto getByApiDto);

    /**
     * 从文件获取唤取记录
     *
     * @param getByFileDto
     * @return
     */
    int getByFile(GetByFileDto getByFileDto);

    /**
     * 更新唤取记录
     *
     * @param conveneItems
     * @param playerId
     * @return
     */
    int update(List<ConveneItem> conveneItems, Long playerId);

    /**
     * 删除唤取记录
     *
     * @param playerId
     */
    void delete(Long playerId);

    /**
     * 导出唤取记录
     *
     * @param playerId
     * @return
     */
    String export(Long playerId);
}
