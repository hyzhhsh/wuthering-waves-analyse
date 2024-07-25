package cn.hyzhhsh.wutheringwavesanalyse.server.controller;

import cn.hyzhhsh.wutheringwavesanalyse.common.result.Result;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.dto.GetByApiDto;
import cn.hyzhhsh.wutheringwavesanalyse.pojo.dto.GetByFileDto;
import cn.hyzhhsh.wutheringwavesanalyse.server.service.ConveneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/convene")
@Api(tags = "唤取相关接口")
@Slf4j
public class ConveneController {
    @Autowired
    ConveneService conveneService;

    /**
     * 从接口获取唤取记录
     *
     * @param getByApiDto
     * @return
     */
    @PutMapping("/get-by-api")
    @ApiOperation("从接口获取唤取记录")
    public Result<String> getByApi(@RequestBody GetByApiDto getByApiDto) {
        log.info("从接口获取唤取记录：{}", getByApiDto);
        int insertedRows = conveneService.getByApi(getByApiDto);
        return Result.success(insertedRows > 0 ? "新增" + insertedRows + "条唤取记录" : "无新增唤取记录");
    }

    /**
     * 从文件获取唤取记录
     *
     * @param getByFileDto
     * @return
     */
    @PutMapping("/get-by-file")
    @ApiOperation("从文件获取唤取记录")
    public Result<String> getByFile(@RequestBody GetByFileDto getByFileDto) {
        log.info("从文件获取唤取记录：{}", getByFileDto);
        int insertedRows = conveneService.getByFile(getByFileDto);
        return Result.success(insertedRows > 0 ? "新增" + insertedRows + "条唤取记录" : "无新增唤取记录");
    }

    /**
     * 删除唤取记录
     *
     * @param playerId
     * @return
     */
    @DeleteMapping("/{playerId}")
    @ApiOperation("删除唤取记录")
    public Result delete(@PathVariable Long playerId) {
        log.info("删除唤取记录：{}", playerId);
        conveneService.delete(playerId);
        return Result.success();
    }

    /**
     * 导出唤取记录
     *
     * @param playerId
     * @return
     */
    @GetMapping("/export/{playerId}")
    @ApiOperation("导出唤取记录")
    public ResponseEntity<InputStreamResource> export(@PathVariable Long playerId) {
        log.info("导出唤取记录：{}", playerId);
        String jsonString = conveneService.export(playerId);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());

        String fileName = playerId + "-" + DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileName + ".json");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(inputStream));
    }
}
