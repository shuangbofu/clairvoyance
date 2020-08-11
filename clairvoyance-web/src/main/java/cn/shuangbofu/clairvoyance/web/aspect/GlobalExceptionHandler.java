package cn.shuangbofu.clairvoyance.web.aspect;

import cn.shuangbofu.clairvoyance.web.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;

/**
 * Created by shuangbofu on 2020/8/5 15:20
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ResponseStatus(OK)
    @ExceptionHandler(RuntimeException.class)
    public Result<Object> runtime(RuntimeException e) {
        log.warn(e.getMessage(), e);
        return Result.error(e.getMessage());
    }
}
