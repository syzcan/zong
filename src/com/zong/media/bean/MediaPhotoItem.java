package com.zong.media.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc photoItem实体类
 * @author zong
 * @date 2018年02月19日
 */
@JsonInclude(Include.NON_NULL)
public class MediaPhotoItem implements Serializable {
	private String id;
	private String pid;
	private String name;
	private String url;
	private String remark;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;
	
	private List<MediaPhotoItem> items;
	
	public MediaPhotoItem(){
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getPid(){
		return pid;
	}
	
	public void setPid(String pid){
		this.pid = pid;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getRemark(){
		return remark;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public Date getCreateTime(){
		return createTime;
	}
	
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	public List<MediaPhotoItem> getItems() {
		return items;
	}

	public void setItems(List<MediaPhotoItem> items) {
		this.items = items;
	}
	
}