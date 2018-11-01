package com.xiaoi.expo.common.response;

import java.util.HashMap;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1217:21
 */
public class MapResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private MapResult() { }

    /**
     * 返回成功
     */
    public static MapResult ok() {
        return ok("操作成功！");
    }

    /**
     * 返回成功
     */
    public static MapResult ok(String message) {
        return ok(200, message);
    }

    /**
     * 返回成功
     */
    public static MapResult ok(int code,String message) {
        MapResult resultMap = new MapResult();
        resultMap.put("code", code);
        resultMap.put("msg", message);
        return resultMap;
    }

    /**
     * 返回失败
     */
    public static MapResult error() {
        return error("操作失败！");
    }

    /**
     * 返回失败
     */
    public static MapResult error(String messag) {
        return error(500, messag);
    }

    /**
     * 返回失败
     */
    public static MapResult error(int code, String message) {
        return ok(code, message);
    }

    /**
     * 设置code
     */
    public MapResult setCode(int code){
        super.put("code", code);
        return this;
    }

    /**
     * 设置message
     */
    public MapResult setMessage(String message){
        super.put("msg", message);
        return this;
    }

    /**
     * 放入object
     */
    @Override
    public MapResult put(String key, Object object){
        super.put(key, object);
        return this;
    }
}