<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<form id="form" method="post">
	<input type="hidden" name="id" />
	<table class="table-form">
		<tr>
			<td align="right" width="90">名称</td>
			<td><input class="easyui-textbox" name="name"
				data-options="required:true" /></td>
		</tr>
		<c:if test="${formType!='edit' }">
		<tr>
			<td align="right">存储表</td>
			<td><input class="easyui-textbox" name="craw_store"
				data-options="required:true,validType:{check:['${ctx }/craw/rule/check','craw_store','已存在']}" />
			</td>
		</tr>
		</c:if>
		<c:if test="${formType=='edit' }">
		<tr>
			<td align="right">存储表</td>
			<td data-field="craw_store">
			</td>
		</tr>
		</c:if>
		<tr>
			<td align="right">样本地址</td>
			<td><input class="easyui-textbox" name="craw_url" data-options="required:true" size="50" /></td>
		</tr>
		<tr>
			<td align="right">条目规则</td>
			<td><input class="easyui-textbox" name="craw_item" data-options="required:true" size="50" /></td>
		</tr>
		<tr>
			<td align="right">下一页规则</td>
			<td><input class="easyui-textbox" name="craw_next" size="50" /></td>
		</tr>
		<tr>
			<td align="right" valign="top">列表规则</td>
			<td>
				<input type="hidden" id="list_ext" name="list_ext" />
				<table id="dg_list_ext"></table>
			</td>
		</tr>
		<tr>
			<td align="right" valign="top">内容规则</td>
			<td>
				<input type="hidden" id="content_ext" name="content_ext" />
				<table id="dg_content_ext"></table>
			</td>
		</tr>
	</table>
</form>
