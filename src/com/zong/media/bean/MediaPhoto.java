package com.zong.media.bean;

import java.util.Date;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc photo实体类
 * @author zong
 * @date 2018年02月19日
 */
@JsonInclude(Include.NON_NULL)
public class MediaPhoto implements Serializable {
	private String id;
	private String name;
	private String cover;
	private String tabs;
	private String remark;
	private String status;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date updateTime;
	
	public MediaPhoto(){
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getCover(){
		return cover;
	}
	
	public void setCover(String cover){
		this.cover = cover;
	}
	
	public String getTabs(){
		return tabs;
	}
	
	public void setTabs(String tabs){
		this.tabs = tabs;
	}
	
	public String getRemark(){
		return remark;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String status){
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
	
}