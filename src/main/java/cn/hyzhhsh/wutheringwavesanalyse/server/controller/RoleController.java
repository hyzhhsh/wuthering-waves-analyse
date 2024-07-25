package cn.hyzhhsh.wutheringwavesanalyse.server.controller;

import cn.hyzhhsh.wutheringwavesanalyse.common.result.Result;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.entiy.Role;
import cn.hyzhhsh.wutheringwavesanalyse.server.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@Api(tags = "角色相关接口")
@Slf4j
public class RoleController {

    @Autowired
    RoleService roleService;

    /**
     * 查询角色
     *
     * @param playerId
     * @return
     */
    @GetMapping("/{playerId}")
    @ApiOperation("查询角色")
    public Result<Role> select(@PathVariable Long playerId) {
        log.info("查询角色：{}", playerId);
        return Result.success(roleService.select(playerId));
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @GetMapping
    @ApiOperation("查询所有角色")
    public Result<List<Role>> selectAll() {
        log.info("查询所有角色...");
        return Result.success(roleService.selectAll());
    }

    /**
     * 新增角色
     *
     * @param role
     * @return
     */
    @PostMapping
    @ApiOperation("新增角色")
    public Result insert(@RequestBody Role role) {
        log.info("新增角色：{}", role);
        roleService.insert(role);
        return Result.success();
    }

    /**
     * 删除角色
     *
     * @param playerId
     * @return
     */
    @DeleteMapping("/{playerId}")
    @ApiOperation("删除角色")
    public Result delete(@PathVariable Long playerId) {
        log.info("删除角色：{}", playerId);
        roleService.delete(playerId);
        return Result.success();
    }
}
