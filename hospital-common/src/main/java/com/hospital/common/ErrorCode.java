
package com.hospital.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 统一错误码枚举
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 通用失败
     */
    FAIL(500, "操作失败"),

    /**
     * 参数校验失败
     */
    PARAM_ERROR(400, "参数校验失败"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未登录或token已过期"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "没有权限访问该资源"),

    /**
     * 请求资源不存在
     */
    NOT_FOUND(404, "请求资源不存在"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(1000, "业务处理失败"),

    /**
     * 用户不存在
     */
    USER_NOT_FOUND(1001, "用户不存在"),

    /**
     * 密码错误
     */
    PASSWORD_ERROR(1002, "密码错误"),

    /**
     * 用户名已存在
     */
    USER_EXISTS(1003, "用户名已存在"),

    /**
     * 手机号已存在
     */
    PHONE_EXISTS(1004, "手机号已存在"),

    /**
     * 身份证号已存在
     */
    ID_CARD_EXISTS(1005, "身份证号已存在"),

    /**
     * 预约冲突
     */
    APPOINTMENT_CONFLICT(2001, "该时段已有预约，请选择其他时间"),

    /**
     * 号源不足
     */
    SCHEDULE_EMPTY(2002, "号源已约满"),

    /**
     * 病历不存在
     */
    RECORD_NOT_FOUND(3001, "病历记录不存在"),

    /**
     * 处方不存在
     */
    PRESCRIPTION_NOT_FOUND(4001, "处方不存在"),

    /**
     * 药品库存不足
     */
    MEDICINE_STOCK_LOW(4002, "药品库存不足"),

    /**
     * 处方状态异常
     */
    PRESCRIPTION_STATUS_ERROR(4003, "处方状态异常，无法执行该操作"),

    /**
     * 库存不足
     */
    STOCK_INSUFFICIENT(4004, "库存不足，无法发药"),

    /**
     * 重复操作
     */
    DUPLICATE_OPERATION(9001, "请勿重复操作"),

    /**
     * 数据已存在
     */
    DATA_EXISTS(9002, "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(9003, "数据不存在"),

    /**
     * 系统内部错误
     */
    SYSTEM_ERROR(9999, "系统内部错误，请联系管理员");

    private final Integer code;
    private final String message;
}