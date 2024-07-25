package cn.hyzhhsh.wutheringwavesanalyse.server.mapper;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    /**
     * 查询所有角色
     *
     * @return
     */
    @Select("select * from role")
    List<Role> selectAll();

    /**
     * 更新角色
     *
     * @param role
     */
    void update(Role role);

    /**
     * 新增角色
     *
     * @param role
     */
    void insert(Role role);

    /**
     * 删除角色
     *
     * @param playerId
     */
    @Delete("delete from role where playerId=#{playerId}")
    void delete(Long playerId);

    /**
     * 查询角色
     *
     * @param playerId
     * @return
     */
    @Select("select * from role where playerId=#{playerId}")
    Role select(Long playerId);
}
