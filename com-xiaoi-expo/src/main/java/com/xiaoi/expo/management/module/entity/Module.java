package com.xiaoi.expo.management.module.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author bright.liang
 * @Description: 权限信息
 * @date 2018/3/1314:16
 */
@Entity
@Table(name="expo_module")
public class Module implements Serializable{
    @Id
    @Column(name="id")
    private String id;

    @Column(name="parent_id")
    private String parentId;

    @Column(name="module_name")
    private String moduleName;

    @Column(name="module_value")
    private String moduleValue;

    @Column(name="create_time")
    private Date createTime;

    @Column(name="module_icon")
    private String moduleIcon;

    @Column(name="module_order")
    private Integer moduleOrder;

    @Transient
    private List<Module> subModules;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleValue() {
        return moduleValue;
    }

    public void setModuleValue(String moduleValue) {
        this.moduleValue = moduleValue;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getModuleIcon() {
        return moduleIcon;
    }

    public void setModuleIcon(String moduleIcon) {
        this.moduleIcon = moduleIcon;
    }

    public Integer getModuleOrder() {
        return moduleOrder;
    }

    public void setModuleOrder(Integer moduleOrder) {
        this.moduleOrder = moduleOrder;
    }

    public List<Module> getSubModules() {
        return subModules;
    }

    public void setSubModules(List<Module> subModules) {
        this.subModules = subModules;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
