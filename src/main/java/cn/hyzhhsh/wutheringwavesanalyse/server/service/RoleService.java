package cn.hyzhhsh.wutheringwavesanalyse.server.service;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.Role;

import java.util.List;

public interface RoleService {
    /**
     * 查询所有角色
     *
     * @return
     */
    List<Role> selectAll();

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
    void delete(Long playerId);

    /**
     * 查询角色
     *
     * @param playerId
     * @return
     */
    Role select(Long playerId);
}
