package cn.hyzhhsh.wutheringwavesanalyse.server.controller;

import cn.hyzhhsh.wutheringwavesanalyse.common.exception.BaseException;
import cn.hyzhhsh.wutheringwavesanalyse.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("/file")
@Api(tags = "文件相关接口")
@Slf4j
public class FileController {
    @SneakyThrows
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file, HttpServletRequest request) {
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        log.info("文件上传：{}", originalFilename);

        //获取原始文件的后缀
        int index = originalFilename.lastIndexOf(".");
        //若文件名不含后缀
        if (index == -1) throw new BaseException("文件名需包含后缀");
        String extension = originalFilename.substring(index);

        //构造新的文件名
        String objectName = UUID.randomUUID() + extension;

        //获取项目在本机运行的真实地址
        String realPath = request.getServletContext().getRealPath("/upload");

        File savedPath = new File(realPath);
        if (!savedPath.exists()) savedPath.mkdirs();

        String destPath = savedPath + "/" + objectName;

        file.transferTo(new File(destPath));
        log.info("文件已保存到：{}", destPath);

        return Result.success(destPath);
    }
}
