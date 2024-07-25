package cn.hyzhhsh.wutheringwavesanalyse.server.controller;

import cn.hyzhhsh.wutheringwavesanalyse.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @GetMapping()
    @ApiOperation("连通性测试")
    public Result select() {
        log.info("连通性测试");
        return Result.success("Hello");
    }
}
