package cn.hyzhhsh.wutheringwavesanalyse.server.service.impl;

import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.Role;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.ConveneMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.ConveneSummaryMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.RareConveneMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.mapper.RoleMapper;
import cn.hyzhhsh.wutheringwavesanalyse.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    ConveneMapper conveneMapper;

    @Autowired
    RareConveneMapper rareConveneMapper;

    @Autowired
    ConveneSummaryMapper conveneSummaryMapper;

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public List<Role> selectAll() {
        return roleMapper.selectAll();
    }

    /**
     * 新增角色
     *
     * @param role
     */
    @Override
    public void insert(Role role) {
        roleMapper.insert(role);
    }

    /**
     * 删除角色
     *
     * @param playerId
     */
    @Override
    @Transactional
    public void delete(Long playerId) {
        //删除角色时角色数据和分析数据需要一并删除
        roleMapper.delete(playerId);
        conveneMapper.delete(playerId);
        rareConveneMapper.delete(playerId);
        conveneSummaryMapper.delete(playerId);
    }

    /**
     * 查询角色
     *
     * @param playerId
     * @return
     */
    @Override
    public Role select(Long playerId) {
        return roleMapper.select(playerId);
    }
}
