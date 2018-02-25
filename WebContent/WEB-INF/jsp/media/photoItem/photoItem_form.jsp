<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
	<input type="hidden" name="id" />
	<table class="table-form">
		<tr>
			<td align="right">所属相册：</td>
			<td>
				<input name="pid" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">原文件名：</td>
			<td>
				<input name="name" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">url：</td>
			<td>
				<input name="url" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">remark：</td>
			<td>
				<input name="remark" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">createTime：</td>
			<td>
				<input name="createTime" class="easyui-datebox" data-options="" />
			</td>
		</tr>
	</table>
</form>