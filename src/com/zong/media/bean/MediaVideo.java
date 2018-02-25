package com.zong.media.bean;

import java.util.Date;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @desc video实体类
 * @author zong
 * @date 2018年02月19日
 */
@JsonInclude(Include.NON_NULL)
public class MediaVideo implements Serializable {
	private String id;
	private String md5;
	private String title;
	private String cover;
	private String url;
	private String remark;
	private Integer length;
	private Integer size;
	private Integer width;
	private Integer height;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date createTime;
	@JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
	private Date updateTime;
	private Integer star;
	private String tags;
	
	public MediaVideo(){
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	public String getMd5(){
		return md5;
	}
	
	public void setMd5(String md5){
		this.md5 = md5;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getCover(){
		return cover;
	}
	
	public void setCover(String cover){
		this.cover = cover;
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
	
	public Integer getLength(){
		return length;
	}
	
	public void setLength(Integer length){
		this.length = length;
	}
	
	public Integer getSize(){
		return size;
	}
	
	public void setSize(Integer size){
		this.size = size;
	}
	
	public Integer getWidth(){
		return width;
	}
	
	public void setWidth(Integer width){
		this.width = width;
	}
	
	public Integer getHeight(){
		return height;
	}
	
	public void setHeight(Integer height){
		this.height = height;
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
	
	public Integer getStar(){
		return star;
	}
	
	public void setStar(Integer star){
		this.star = star;
	}
	
	public String getTags(){
		return tags;
	}
	
	public void setTags(String tags){
		this.tags = tags;
	}
	
	/**
	 * 长度转为00：00字符串
	 * 
	 * @return
	 */
	public String getLengthString() {
		StringBuffer length = new StringBuffer("0");
		int minute = getLength() / 60;
		int second = getLength() % 60;
		if (minute > 10) {
			length = new StringBuffer("0");
		}
		length.append(minute + ":");
		if (second < 10) {
			length.append("0");
		}
		length.append(second);
		return length.toString();
	}

	/**
	 * 长度转为00：00字符串
	 * 
	 * @return
	 */
	public String getSizeString() {
		long kb = getSize() / 1024;
		long m = kb / 1024;
		// 不足1m返回kb
		if (m == 0) {
			return kb + "KB";
		}
		long ekb = (kb % 1024) * 1000 / 1024 / 10;
		return m + "." + (ekb < 10 ? ("0" + ekb) : ekb) + "M";
	}
}