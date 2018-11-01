package com.xiaoi.expo.management.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bright.liang
 * @Description: 用户信息表
 * @date 2018/3/1216:43
 */
@Entity
@Table(name="expo_user")
public class User implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id; // 主键

    @Column(name="real_user_name")
    private String realUserName; // 用户真实姓名

    @Column(name="password")
    private String password;// 用户登录密码

    @Column(name="user_name")
    private String userName;  // 用户登录名

    @Column(name="user_phone")
    private String userPhone; // 用户联系电话

    @Column(name="sex")
    private String sex;

    @Column(name = "status")
    private String status; // 用户状态 0正常 1 停用

    @Column(name = "role")
    private String role;  // 用户角色 0管理员 1普通用户

    @Column(name="create_time")
    private Date createTime; // 创建时间

    @Column(name = "update_time")
    private Date updateDate; // 最近修改时间

    @Column(name = "last_login_time")
    private Date lastLoginTime; // 上次登录时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealUserName() {
        return realUserName;
    }

    public void setRealUserName(String realUserName) {
        this.realUserName = realUserName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
