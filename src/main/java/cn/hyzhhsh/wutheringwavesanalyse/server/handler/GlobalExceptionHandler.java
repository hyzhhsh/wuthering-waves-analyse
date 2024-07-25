package cn.hyzhhsh.wutheringwavesanalyse.server.handler;

import cn.hyzhhsh.wutheringwavesanalyse.common.exception.BaseException;
import cn.hyzhhsh.wutheringwavesanalyse.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获业务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> exceptionHandler(BaseException ex) {
        log.error("捕获到业务异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 捕获其他异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception ex) {
        String message = ex.getMessage();
        if (message.startsWith("Maximum upload size exceeded")) {
            log.error("捕获到业务异常信息：上传的文件大小超出限制");
            return Result.error("上传的文件大小超出限制");
        }
        if (message.contains("Duplicate entry")) {
            log.info("捕获到业务异常信息：重复添加");
            return Result.error("重复添加");
        }
        log.error("捕获到异常信息：{}", message);
        log.error("异常堆栈：{}", ex.getStackTrace());
        return Result.error("内部错误");
    }
}
