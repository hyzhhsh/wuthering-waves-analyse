package cn.hyzhhsh.wutheringwavesanalyse.server.mapper;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.ConveneSummary;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConveneSummaryMapper {

    /**
     * 删除唤取总结
     *
     * @param playerId
     */
    @Delete("delete from convene_summary where playerId=#{playerId}")
    void delete(Long playerId);

    /**
     * 插入唤取总结
     *
     * @param conveneSummary
     */
    void insert(ConveneSummary conveneSummary);

    /**
     * 根据playerId查询唤取总结
     *
     * @param playerId
     * @return
     */
    ConveneSummary select(Long playerId);
}
