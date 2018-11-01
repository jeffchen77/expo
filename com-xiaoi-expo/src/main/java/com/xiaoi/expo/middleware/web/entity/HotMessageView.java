package com.xiaoi.expo.middleware.web.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author bright.liang
 * @Description: 用户信息表
 * @date 2018/3/1216:43
 */
@Entity
@Table(name="v_expo_hotmessage")
public class HotMessageView implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id; // 主键

    @Column(name = "create_time")
    private Date cerateDate; // 创建时间

    @Column(name = "title")
    private String title; // 标题

    @Column(name = "content")
    private String content; // 内容

    @Column(name = "image")
    private String image; // 图片

    @Column(name = "knowledge")
    private String knowledge; // 知识链接

    @Column(name = "type")
    private String type; // 类型: 0热点资讯 1大佬说

    @Column(name = "name")
    private String name;// 嘉宾姓名

    @Column(name = "rank")
    private String rank;

    //访问地址
    @Value("${ftp.domain}")
    @Transient
    private String domain;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Column(name = "guset_photo")
    private String gusetPhoto; // 嘉宾头像

    public List<String> getImages() {
        return images;
    }

    public void setImages(String domain) {
        List<String> list = new ArrayList<String>();
        if(!StringUtils.isEmpty(image)){
            String[] imgs = image.split(",");

            for(String temp: imgs){
                list.add(domain + temp);
            }
        }
        this.images = list;
    }

    @Column(name = "position")
    private String position; // 嘉宾职位

    @Transient
    private List<String> images;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonFormat(pattern="MM月dd日",timezone = "GMT+8")
    public Date getCerateDate() {
        return cerateDate;
    }

    public void setCerateDate(Date cerateDate) {
        this.cerateDate = cerateDate;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGusetPhoto() {
        return gusetPhoto;
    }

    public void setGusetPhoto(String gusetPhoto) {
        this.gusetPhoto = gusetPhoto;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
