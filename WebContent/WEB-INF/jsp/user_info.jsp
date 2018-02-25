<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<form id="form" method="post">
    <input type="hidden" name="id" />
    <table class="table-form">
        <tr>
            <td align="right">用户名：</td>
            <td data-field="username">
            </td>
        </tr>
        <tr>
            <td align="right">昵称：</td>
            <td>
                <input name="nickname" class="easyui-textbox" data-options="required:true" />
            </td>
        </tr>
        <tr>
            <td align="right">性别：</td>
            <td>
                <select class="easyui-combobox" name="gender" style="width: 60px">
                	<option value="1">男</option>
                	<option value="2">女</option>
                </select>
            </td>
        </tr>
        <tr>
            <td align="right">生日：</td>
            <td>
                <input name="birthday" class="easyui-datebox" data-options="editable:false" />
            </td>
        </tr>
        <tr>
            <td align="right">头像：</td>
            <td style="position: relative;">
                <input name="avatar" class="zuploadbox" data-options="editable:false,
                width: 240,
                height: 26,
				success : function(data) {
					$('img[data-src]').attr('src', ctx + '/' + data.filePath);
				}" />
				<img data-src="avatar" style="position: absolute;left: 300px;bottom:0;width:120px;height:120px">
            </td>
        </tr>
        <tr>
            <td align="right">备注：</td>
            <td>
                <textarea name="remark" class="easyui-textbox" data-options="multiline:true" style="width:300px;height:60px;"></textarea>
            </td>
        </tr>
    </table>
</form>