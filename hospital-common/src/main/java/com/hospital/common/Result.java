
package com.hospital.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回结果封装
 *
 * @param <T> 返回数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一返回结果")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码", example = "200")
    private Integer code;

    @Schema(description = "返回消息", example = "操作成功")
    private String message;

    @Schema(description = "返回数据")
    private T data;

    @Schema(description = "时间戳")
    private Long timestamp;

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回（带数据和消息）
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ErrorCode.FAIL.getCode(), message, null);
    }

    /**
     * 失败返回（带错误码）
     */
    public static <T> Result<T> fail(ErrorCode errorCode) {
        return new Result<>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回（自定义状态码和消息）
     */
    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 未授权返回
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage(), null);
    }

    /**
     * 禁止访问返回
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(ErrorCode.FORBIDDEN.getCode(), ErrorCode.FORBIDDEN.getMessage(), null);
    }
}