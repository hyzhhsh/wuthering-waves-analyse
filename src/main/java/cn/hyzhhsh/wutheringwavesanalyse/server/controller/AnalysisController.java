package cn.hyzhhsh.wutheringwavesanalyse.server.controller;

import cn.hyzhhsh.wutheringwavesanalyse.common.result.Result;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.vo.AnalysisVO;
import cn.hyzhhsh.wutheringwavesanalyse.server.service.AnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analysis")
@Api(tags = "分析相关接口")
@Slf4j
public class AnalysisController {
    @Autowired
    AnalysisService analysisService;

    /**
     * 更新分析数据
     *
     * @param playerId
     * @return
     */
    @PutMapping("/{playerId}")
    @ApiOperation("更新分析数据")
    public Result update(@PathVariable Long playerId) {
        log.info("更新分析数据：{}", playerId);
        analysisService.update(playerId);
        return Result.success();
    }

    /**
     * 查询分析数据
     *
     * @param playerId
     * @return
     */
    @GetMapping("/{playerId}")
    @ApiOperation("查询分析数据")
    public Result<AnalysisVO> select(@PathVariable Long playerId) {
        log.info("查询分析数据：{}", playerId);
        return Result.success(analysisService.select(playerId));
    }

}
