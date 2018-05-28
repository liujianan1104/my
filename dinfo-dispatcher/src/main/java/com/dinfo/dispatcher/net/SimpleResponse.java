package com.dinfo.dispatcher.net;

/**
 * @author yangxf
 */
public class SimpleResponse<T> implements Response<T> {
    private static final long serialVersionUID = 3105068323605898016L;

    private Boolean success;

    /**
     * message of error, if {@link #success} is false, otherwise "completed"
     */
    private String msg = "completed";

    /**
     * response data
     */
    private T data;

    @Override
    public Boolean success() {
        return success;
    }

    @Override
    public T data() {
        return data;
    }

    @Override
    public String message() {
        return msg;
    }

    public static Response buildFailure() {
        return buildFailure("failure");
    }

    public static Response buildFailure(String message, Object... args) {
        return new SimpleResponse()
                .setSuccess(false)
                .setMsg(String.format(message, args));
    }

    public static Response buildSuccess() {
        return buildSuccess(null);
    }

    public static <D> Response<D> buildSuccess(D data) {
        return new SimpleResponse<D>()
                .setSuccess(true)
                .setData(data);
    }

    public Boolean getSuccess() {
        return success;
    }

    public SimpleResponse<T> setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public SimpleResponse<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public SimpleResponse<T> setData(T data) {
        this.data = data;
        return this;
    }
}
