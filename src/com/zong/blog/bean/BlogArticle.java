package com.zong.blog.bean;

import java.util.Date;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc article实体类
 * @author zong
 * @date 2018年02月18日
 */
@JsonInclude(Include.NON_NULL)
public class BlogArticle implements Serializable {
	private String id;
	private String cateId;
	private String title;
	private String content;
	private String description;
	private String cover;
	private Integer clickCount;
	private Integer replyCount;
	private String nature;
	private String tags;
	private String url;
	private Integer status;
	private String userId;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date updateTime;
	
	public BlogArticle(){
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getCateId(){
		return cateId;
	}
	
	public void setCateId(String cateId){
		this.cateId = cateId;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getCover(){
		return cover;
	}
	
	public void setCover(String cover){
		this.cover = cover;
	}
	
	public Integer getClickCount(){
		return clickCount;
	}
	
	public void setClickCount(Integer clickCount){
		this.clickCount = clickCount;
	}
	
	public Integer getReplyCount(){
		return replyCount;
	}
	
	public void setReplyCount(Integer replyCount){
		this.replyCount = replyCount;
	}
	
	public String getNature(){
		return nature;
	}
	
	public void setNature(String nature){
		this.nature = nature;
	}
	
	public String getTags(){
		return tags;
	}
	
	public void setTags(String tags){
		this.tags = tags;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
	
	public Date getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	
	public Date getUpdateTime(){
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}