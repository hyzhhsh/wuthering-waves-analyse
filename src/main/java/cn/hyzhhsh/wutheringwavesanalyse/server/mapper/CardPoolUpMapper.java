package cn.hyzhhsh.wutheringwavesanalyse.server.mapper;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.CardPoolUpItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CardPoolUpMapper {
    /**
     * 查询角色up记录
     *
     * @param resourceType
     * @return
     */
    @Select("select * from card_pool_up_item where resourceType=#{resourceType}")
    List<CardPoolUpItem> selectByResourceType(String resourceType);
}
