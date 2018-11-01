package com.xiaoi.expo.common.response;

import java.util.List;

/**
 * @author bright.liang
 * @Description: 返回分页结果
 * @date 2018/3/1217:20
 */
public class PageResult<T> {

    private int code; //状态码, 0表示成功

    private String msg;  //提示信息

    private long count; // 总数量, bootstrapTable是total

    private long totalPage; // 总页数

    private int currentPage; // 当前页

    private List<T> data; // 当前数据, bootstrapTable是rows

    public PageResult() {
    }

    public PageResult(long total, List<T> rows) {
        this.count = total;
        this.data = rows;
        this.code = 0;
        this.msg = "";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }


    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}