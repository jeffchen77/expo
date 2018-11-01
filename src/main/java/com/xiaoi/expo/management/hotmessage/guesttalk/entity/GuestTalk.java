package com.xiaoi.expo.management.hotmessage.guesttalk.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaoi.expo.management.baseconfig.guest.entity.Guest;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author bright.liang
 * @Description: 大佬说信息表
 * @date 2018/3/1216:43
 */
@Entity
@Table(name="expo_guest_talk")
public class GuestTalk implements Serializable{

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(name = "title")
    private String title; //嘉宾发言主题标题

    @Column(name = "content")
    private String content; //嘉宾发言主题内容

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id")
    private Guest guest; //嘉宾信息，（外键）关联嘉宾信息表

    @Column(name = "knowledge")
    private String knowledge; //知识链接

    @Column(name = "picture")
    private String picture; //图片地址

    @Column(name = "create_time")
    private Date createTime; //创建时间

    @Column(name = "update_time")
    private Date updateTime; //修改时间

    @Column(name = "is_show_screen")
    private String isShowScreen;

    public String getIsShowScreen() {
        return isShowScreen;
    }

    public void setIsShowScreen(String isShowScreen) {
        this.isShowScreen = isShowScreen;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    @Transient

    private String guestId;

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

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
