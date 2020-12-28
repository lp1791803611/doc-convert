package top.plgxs.common;

import java.io.Serializable;

/**
 * 
 * @Filename: JsonData.java
 * @Version: 1.0
 * @Author: taoyong
 * @Date: 2018年11月15日 下午5:53:45
 */
public class JsonData<T> implements Serializable {

    /**
     *Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 4231939441433804296L;

    private boolean           ret;

    private String            msg;

    private T                 data;

    private int               status;

    public JsonData() {

    }

    public JsonData(boolean ret) {
        this.ret = ret;
    }

    public JsonData(boolean ret, T data) {
        super();
        this.ret = ret;
        this.data = data;
    }

    public JsonData(boolean ret, T data, int status) {
        super();
        this.ret = ret;
        this.data = data;
        this.status = status;
    }

    public JsonData(boolean ret, String msg, T data) {
        super();
        this.ret = ret;
        this.msg = msg;
        this.data = data;
    }

    public JsonData(boolean ret, String msg, T data, int status) {
        super();
        this.ret = ret;
        this.msg = msg;
        this.data = data;
        this.status = status;
    }

    public static <T> JsonData<T> success() {
        return new JsonData<T>(true);
    }

    public static <T> JsonData<T> success(String msg) {
        JsonData<T> jsonData = new JsonData<T>(true);
        jsonData.msg = msg;
        return jsonData;
    }

    public static <T> JsonData<T> success(T data) {
        return new JsonData<T>(true, data);
    }

    public static <T> JsonData<T> success(T data, String msg) {
        return new JsonData<T>(true, msg, data);
    }

    public static <T> JsonData<T> build(int status, String msg) {
        JsonData<T> jsonData = new JsonData<T>(true);
        jsonData.status = status;
        jsonData.msg = msg;
        return jsonData;
    }

    public static <T> JsonData<T> result(int status, String msg, T data) {
        return new JsonData<T>(true, msg, data, status);
    }

    public static <T> JsonData<T> msssage(String msg) {
        JsonData<T> jsonData = new JsonData<T>(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public static <T> JsonData<T> error() {
        return new JsonData<T>(false);
    }

    public static <T> JsonData<T> error(String msg) {
        JsonData<T> jsonData = new JsonData<T>(false);
        jsonData.msg = msg;
        return jsonData;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
