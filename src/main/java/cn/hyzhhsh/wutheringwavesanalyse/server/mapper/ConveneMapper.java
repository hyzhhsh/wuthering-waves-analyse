package cn.hyzhhsh.wutheringwavesanalyse.server.mapper;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.ConveneItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ConveneMapper {
    /**
     * 根据playerId查询唤取记录
     *
     * @param playerId
     * @return
     */
    @Select("select * from convene_item where playerId=#{playerId} order by timeKey asc")
    List<ConveneItem> select(Long playerId);

    /**
     * 批量插入唤取记录
     *
     * @param targetConveneItems
     * @return
     */
    int insert(ArrayList<ConveneItem> targetConveneItems);

    /**
     * 删除唤取记录
     *
     * @param playerId
     */
    @Delete("delete from convene_item where playerId=#{playerId}")
    void delete(Long playerId);
}
