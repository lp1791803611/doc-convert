package top.plgxs.common;

import java.util.List;

import com.github.pagehelper.PageInfo;

public class Page<T> {

    private int     code;

    private List<T> data;

    private String  msg;

    private Long    count;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Page(List<T> data, PageInfo pageInfo) {
        this.code = 0;
        this.data = data;
        this.msg = "";
        this.count = pageInfo.getTotal();
    }

}
