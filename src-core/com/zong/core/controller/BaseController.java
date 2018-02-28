package com.zong.core.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zong.core.bean.Page;
import com.zong.core.bean.PageData;

/**
 * @desc Controller基类，包含表单参数封装等通用操作
 * @author zong
 * @date 2016年3月13日
 */
public class BaseController {

	public static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 封装分页page，同时转发到页面，返回page给service查询
	 */
	public Page getPage() {
		Page page = new Page(getPageData());// 分页组件封装查询参数
		// pd移除分页查询的参数
		page.getPd().remove("currentPage");
		page.getPd().remove("showCount");
		//easy分页参数
		page.getPd().remove("page");
		page.getPd().remove("rows");
		//page.getPd().remove("sort");
		//page.getPd().remove("order");
		this.getRequest().setAttribute("page", page);// 分页组件页面展示
		// 剩余参数继续通过pageData回传到页面
		getPageData();
		return page;
	}

	/**
	 * new PageData对象
	 * 
	 * @return PageData
	 */
	public PageData getPageData() {
		PageData pd = new PageData(this.getRequest());
		getRequest().setAttribute("pd", pd);
		return pd;
	}

	/**
	 * 得到request对象
	 * 
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request;
	}

	/**
	 * httpsession
	 * 
	 * @return HttpSession
	 */
	public HttpSession getSession() {
		return getRequest().getSession();
	}

	public ServletContext getApplication() {
		return getRequest().getSession().getServletContext();
	}
	
	/**
	 * 项目真实根路径
	 * 
	 * @return
	 */
	public String getRealPath() {
		return getApplication().getRealPath("/");
	}

	public String toJson(Object object) {
		String json = "";
		try {
			json = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
