package com.xiaoi.expo.management.hotmessage.message.entity;

import com.xiaoi.expo.management.baseconfig.enterprise.entity.Enterprise;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author guangjian.zeng
 * @Description: 图文短信表
 * @date 2018/3/1911:12
 */

@Entity
@Table(name="expo_message")
public class Message {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "title")
    private String title; //图文咨询标题

    @Column(name = "content")
    private String content; //图文咨询内容

    /*@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;*/ // 嘉宾所属企业id，外键管理企业信息表

    @Column(name = "picture")
    private String picture; //图文咨询图片地址

    @Column(name = "knowledge")
    private String knowledge; //知识链接

    @Column(name = "status")
    private String status; //状态：0正常， 1停用

    @Column(name = "is_show_screen")
    private String isShowScreen; //大屏滚动：0正常，1停用

    @Column(name = "is_show_index")
    private String isShowIndex; //首页显示：0显示，1不显示

    @Column(name = "create_time")
    private Date createTime; //创建时间

    @Column(name = "update_time")
    private Date updateTime; //修改时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

   /* public Enterprise getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(Enterprise enterprise) {
        this.enterprise = enterprise;
    }*/

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsShowScreen() {
        return isShowScreen;
    }

    public void setIsShowScreen(String isShowScreen) {
        this.isShowScreen = isShowScreen;
    }

    public String getIsShowIndex() {
        return isShowIndex;
    }

    public void setIsShowIndex(String isShowIndex) {
        this.isShowIndex = isShowIndex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
