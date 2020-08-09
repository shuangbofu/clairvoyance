package cn.shuangbofu.clairvoyance.web.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by shuangbofu on 2020/7/30 下午8:48
 */
@Data
@ApiModel(description = "返回结构")
public class Result<T> {
    @ApiModelProperty("错误信息")
//    @JSONField(name = "errorMsg")
    @JsonProperty("errorMsg")
    private String message;
    @ApiModelProperty("数据")

//    @JSONField(name = "result")
    @JsonProperty("result")
    private T data;
    @ApiModelProperty("是否成功")
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

