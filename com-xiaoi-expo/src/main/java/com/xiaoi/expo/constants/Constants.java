package com.xiaoi.expo.constants;

/**
 * @author bright.liang
 * @Description: ${todo}
 * @date 2018/3/1217:29
 */
public class Constants {

    /**************** 错误码 ***************/
    public static final Integer SUCESS_CODE = 200;
    public static  final Integer ERROR_CODE_SYSTEM = 500; // 系统错误

    public static final Integer ERROR_CODE_VALIDATION = 403; // 后台验证不通过


    /****************用户角色 ***************/
    public static final String USER_ROLE_ADMIN="0"; // 管理员

    public static final String USER_ROLE_NORMAL = "1"; // 普通用户

    public static final String USER_ROLE_VISTER = "2"; // 访客
    
    /****************企业库中下拉框的父ID ***************/
    public static final String ENTERPRISE_TYPE_PARENTID="ENTTYPE"; // 企业类型父ID

    public static final String ENTERPRISE_SHOWTYPE_PARENTID = "SHOWTYPE"; // 企业展示类型父ID

    /****************用户状态 ***************************/
    public static final String USER_STATUS_NORMAL = "0"; // 用户状态 正常

    public static final String USER_STATUS_STOP = "1"; // 用户状态 停用

}
