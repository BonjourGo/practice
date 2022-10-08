package com.bonjour.practice.common.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 通用返回结果封装
 *
 * @authur tc
 * @date 2022/10/8 11:41
 */
@Data
public class Result<T> {

    @ApiModelProperty("返回编码")
    private String code;

    @ApiModelProperty("返回消息")
    private String msg;

    @ApiModelProperty("返回数据")
    private T data;

    public Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public Result(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 操作错误信息
     *
     * @return 返回status=500,操作信息="未知异常，请联系管理员"，返回数据=""的结果
     */
    public static Result error() {
        return custom("500", "", "");
    }

    /**
     * 操作错误信息
     *
     * @return 返回status=500,操作信息=msg，返回数据=""的结果
     */
    public static Result error(String msg) {
        return custom("500", msg, "");
    }

    /**
     * 操作成功信息
     *
     * @return 返回status=200,操作信息="操作成功"，返回数据=""的结果
     */
    public static Result ok() {
        return custom("200", "操作成功", "");
    }

    /**
     * 操作成功信息
     *
     * @return 返回status=200,操作信息="操作成功"，返回数据=data的结果
     */
    public static Result ok(Object data) {
        return custom("200", "操作成功", data);
    }

    /**
     * 自定义操作信息
     *
     * @param status 操作结果编码
     * @param msg    操作结果描述
     * @param data   操作结果数据
     * @return Result
     */
    public static Result custom(String status, String msg, Object data) {
        Result Result = new Result(status, msg, data);
        return Result;
    }

    /**
     * 自定义操作信息
     *
     * @param status 操作结果编码
     * @param msg    操作结果描述
     * @return
     */
    public static Result custom(String status, String msg) {
        Result Result = new Result(status, msg);
        return Result;
    }

    /**
     * 自定义操作信息
     *
     * @param status 操作结果编码
     * @param data   操作结果数据
     * @return Result
     */
    public static Result custom(String status, Object data) {
        Result Result = new Result(status, "", data);
        return Result;
    }

    /**
     * 自定义操作信息
     *
     * @param status 操作结果编码
     * @return Result
     */
    public static Result custom(String status) {
        Result Result = new Result(status, "");
        return Result;
    }

    @Override
    public String toString() {
        return "{" +
                "code:'" + this.code + "'," +
                "msg:\"" + this.msg + "\"," +
                "data:'" + this.data +
                "'}";
    }
}
