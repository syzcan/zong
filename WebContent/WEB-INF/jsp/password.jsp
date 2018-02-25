<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
	<table class="table-form">
	    <tr>
	        <td align="right">旧密码</td>
	        <td><input type="password" class="easyui-textbox" name="oldPassword" data-options="required:true">
	        </td>
	    </tr>
	    <tr>
	        <td align="right">新密码</td>
	        <td><input type="password" class="easyui-textbox" id="password" name="password" data-options="required:true,validType:'length[6,15]'">
	        </td>
	    </tr>
	    <tr>
	        <td align="right">重复新密码</td>
	        <td><input type="password" class="easyui-textbox" data-options="required:true,validType:{equals:['#password','两次密码不一致']}"></td>
	    </tr>
	</table>
</form>