<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta name="keywords" content="关键词" />
<meta name="description" content="描述" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<style type="text/css">
.table img{max-width: 100%}
</style>
</head>
<body class="easyui-layout">
	<div class="easyui-panel" data-options="width:'100%',height:'100%',border:false">
		<table class="table-form">
			<tr>
				<td align="right" style="width: 100px"><strong>标题</strong></td>
				<td style="word-break: break-all;">${store.title }</td>
			</tr>
			<tr>
				<td align="right"><strong>来源</strong></td>
				<td><a href="${store.url }" target="_blank">${store.url }</a></td>
			</tr>
			<tr>
				<td align="right"><strong>创建时间</strong></td>
				<td><fmt:formatDate value="${store.create_time }"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
			</tr>
			<tr>
				<td align="right"><strong>更新时间</strong></td>
				<td><fmt:formatDate value="${store.update_time }"
						pattern="yyyy-MM-dd HH:mm:ss" /></td>
			</tr>
			<c:if test="${!empty rule }">
				<c:forEach items="${rule.list_ext }" var="ext">
				<c:if test="${ext.rule_ext_name!='title'&&ext.rule_ext_name!='url'&&ext.rule_ext_name!='create_time'&&ext.rule_ext_name!='content'&&ext.rule_ext_name!='status'&&ext.rule_ext_name!='id' }">
				<tr>
					<td align="right" valign="top"><strong>${empty ext.rule_ext_desc?ext.rule_ext_name:ext.rule_ext_desc }</strong></td>
					<td style="word-break: break-all;">
					<c:if test="${ext.rule_ext_type=='attr' && ext.rule_ext_attr=='href' }">
					<a href="${store[ext.rule_ext_name] }" target="_blank">${store[ext.rule_ext_name] }</a>
					</c:if>
					<c:if test="${ext.rule_ext_type=='attr' && ext.rule_ext_attr=='src' }">
					<img src="${store[ext.rule_ext_name] }" />
					</c:if>
					<c:if test="${ext.rule_ext_type!='attr' || (ext.rule_ext_attr!='href' && ext.rule_ext_attr!='src') }">
					${store[ext.rule_ext_name] }
					</c:if>
					</td>
				</tr>
				</c:if>
				</c:forEach>
				<c:forEach items="${rule.content_ext }" var="ext">
				<c:if test="${ext.rule_ext_name!='title'&&ext.rule_ext_name!='url'&&ext.rule_ext_name!='create_time'&&ext.rule_ext_name!='content'&&ext.rule_ext_name!='status'&&ext.rule_ext_name!='id' }">
				<tr>
					<td align="right" valign="top"><strong>${empty ext.rule_ext_desc?ext.rule_ext_name:ext.rule_ext_desc }</strong></td>
					<td style="word-break: break-all;">
					<c:if test="${ext.rule_ext_type=='attr' && ext.rule_ext_attr=='href' }">
					<a href="${store[ext.rule_ext_name] }" target="_blank">${store[ext.rule_ext_name] }</a>
					</c:if>
					<c:if test="${ext.rule_ext_type=='attr' && ext.rule_ext_attr=='src' }">
					<img src="${store[ext.rule_ext_name] }" />
					</c:if>
					<c:if test="${ext.rule_ext_type!='attr' || (ext.rule_ext_attr!='href' && ext.rule_ext_attr!='src') }">
					${store[ext.rule_ext_name] }
					</c:if>
					</td>
				</tr>
				</c:if>
				</c:forEach>
			</c:if>
			<tr>
				<td align="right" valign="top"><strong>内容</strong></td>
				<td>${store.content }</td>
			</tr>
		</table>
	</div>
</body>
</html>