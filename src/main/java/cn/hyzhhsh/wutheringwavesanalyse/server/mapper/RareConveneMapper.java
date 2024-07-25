package cn.hyzhhsh.wutheringwavesanalyse.server.mapper;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.RareConveneItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RareConveneMapper {

    /**
     * 插入出五星的记录
     *
     * @param rareConveneItems
     */
    void insertBatch(List<RareConveneItem> rareConveneItems);

    /**
     * 删除出五星的记录
     *
     * @param playerId
     */
    @Delete("delete from rare_convene_item where playerId=#{playerId}")
    void delete(Long playerId);

    /**
     * 根据playerId查询出五星的记录
     *
     * @param playerId
     * @return
     */
    @Select("select * from rare_convene_item where playerId=#{playerId} order by timeKey desc")
    List<RareConveneItem> select(Long playerId);
}
