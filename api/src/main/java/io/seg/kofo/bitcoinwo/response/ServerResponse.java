package io.seg.kofo.bitcoinwo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * @author gin
 */
//@JsonInclude(value = JsonInclude.Include.NON_NULL)
    @Data
public class ServerResponse<T>  {
    private static final long serialVersionUID = -5559978730557912852L;

    private String code;

    private String subCode;
    private String msg;
    private T data;

    public ServerResponse() {
    }

    public ServerResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ServerResponse(String code, String subCode, String msg) {
        this.code = code;
        this.subCode = subCode;
        this.msg = msg;
    }

    public ServerResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ServerResponse(String code, String subCode, String msg, T data) {
        this.code = code;
        this.subCode = subCode;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code.equals(ResponseEnum.SUCCESS.getMainCode());
    }


    private static String buildMsg(ResponseEnum responseEnum, String extMsg) {
        return responseEnum.getDesc() + StringUtils.defaultString(extMsg);
    }

    public static <T> ServerResponse<T> createBySuccessData(T data) {
        return new ServerResponse<T>(ResponseEnum.SUCCESS.getMainCode(), ResponseEnum.SUCCESS.getSubCode(), ResponseEnum.SUCCESS.getDesc(), data);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg) {
        return new ServerResponse<T>(ResponseEnum.SUCCESS.getMainCode(), ResponseEnum.SUCCESS.getSubCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseEnum.SUCCESS.getMainCode(), ResponseEnum.SUCCESS.getSubCode(), ResponseEnum.SUCCESS.getDesc());
    }

    public static <T> ServerResponse<T> createByError(ResponseEnum responseEnum) {
        return new ServerResponse<T>(responseEnum.getMainCode(), responseEnum.getSubCode(), responseEnum.getDesc());
    }

    public static <T> ServerResponse<T> createByError(ResponseEnum responseEnum, String extMsg) {
        return new ServerResponse<T>(responseEnum.getMainCode(), responseEnum.getSubCode(), buildMsg(responseEnum, extMsg));
    }

}
