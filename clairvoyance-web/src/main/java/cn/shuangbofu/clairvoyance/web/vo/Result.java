package cn.shuangbofu.clairvoyance.web.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/7/30 下午8:48
 */
@Data
public class Result<T> {

    @JsonProperty("errorMsg")
    private String message;

    @JsonProperty("result")
    private T data;
    private boolean success;

    public Result() {
    }

    private Result(boolean success) {
        this.success = success;
    }

    public Result(String message, T data, boolean success) {
        this.message = message;
        this.data = data;
        this.success = success;
    }

    public static <T> Result<T> success(T data) {
        return new Result<T>(true).message("success").data(data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<T>(false).message(message);
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public T data() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }
}

