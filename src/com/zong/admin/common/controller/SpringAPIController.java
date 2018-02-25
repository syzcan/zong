package com.zong.admin.common.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.zong.core.bean.DataMap;

@Controller
public class SpringAPIController {

	@RequestMapping("/spring/api")
	public String test(HttpServletRequest request) {
		WebApplicationContext wc = (WebApplicationContext) request
				.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
		request.setAttribute("list", handlerMethods);
		return "/test";
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/spring/api_json")
	public String api(String domain, HttpServletRequest request) {
		WebApplicationContext wc = (WebApplicationContext) request
				.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		RequestMappingHandlerMapping bean = wc.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
		request.setAttribute("list", handlerMethods);
		List<DataMap> list = new ArrayList<DataMap>();
		DataMap folders = new DataMap();
		String collectionId = UUID.randomUUID().toString();
		for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
			if (mappingInfo.getPatternsCondition().toString().indexOf("/spring/api") > -1) {
				continue;
			}
			HandlerMethod method = handlerMethods.get(mappingInfo);
			// ResponseBody注解的才是接口
			ResponseBody obj = method.getMethodAnnotation(ResponseBody.class);
			if (obj == null) {
				continue;
			}
			
			String folderId = UUID.randomUUID().toString();
			DataMap folder = (DataMap) folders.get(method.getBean());
			if (folder == null) {
				folder = new DataMap();
				folder.put("id", folderId);
				folder.put("name", method.getBean());
				folder.put("description", "");
				folder.put("order", new ArrayList<String>());
				folder.put("owner", 0);
				folders.put(method.getBean(), folder);
			}
			DataMap map = new DataMap();
			String url = mappingInfo.getPatternsCondition().toString().replace("[", "").replace("]", "") + ".json";
			url = url.replaceAll("\\{", ":").replaceAll("\\}", "");

			map.put("id", UUID.randomUUID().toString());
			map.put("headers", "");
			String requestUrl = request.getRequestURL().toString().replace(request.getRequestURI(),
					request.getContextPath());
			map.put("url", (domain == null || domain.equals("") ? requestUrl : domain) + url);
			map.put("preRequestScript", null);
			map.put("pathVariables", new DataMap());
			map.put("method", mappingInfo.getMethodsCondition().toString().replace("[", "").replace("]", ""));

			map.put("bean", folder.get("name"));
			map.put("beanMethod", folder.get("name") + "_" + method.getMethod().getName());

			map.put("data", null);
			map.put("dataMode", "params");
			map.put("tests", null);
			map.put("currentHelper", "normal");
			map.put("helperAttributes", new DataMap());
			map.put("time", System.currentTimeMillis());
			map.put("name", url);
			map.put("description", url);
			map.put("collectionId", collectionId);
			map.put("responses", new ArrayList<DataMap>());
			map.put("folderId", folder.get("id"));
			list.add(map);
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		DataMap postMan = new DataMap();
		Collection<DataMap> foldersValue = folders.values();
		for (DataMap map : foldersValue) {
			for (DataMap data : list) {
				if (map.get("id").equals(data.get("folderId"))) {
					((List<String>) map.get("order")).add(data.getString("id"));
				}
			}
		}
		postMan.put("id", collectionId);
		postMan.put("folders", folders.values());
		postMan.put("order", new ArrayList<DataMap>());
		postMan.put("requests", list);
		postMan.put("description", "");
		postMan.put("name", "项目接口" + dateFormat.format(new Date()));
		postMan.put("timestamp", System.currentTimeMillis());
		postMan.put("owner", 0);
		postMan.put("remoteLink", "");
		postMan.put("public", false);
		return postMan.toJson();
	}
}
