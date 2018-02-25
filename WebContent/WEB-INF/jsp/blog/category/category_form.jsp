<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
	<input type="hidden" name="id" />
	<table class="table-form">
		<tr>
			<td align="right">名称：</td>
			<td>
				<input name="name" class="easyui-textbox" data-options="required:true" />
			</td>
		</tr>
		<tr>
			<td align="right">描述：</td>
			<td>
				<input name="description" class="easyui-textbox" data-options="multiline:true" style="width:250px;height:60px;" />
			</td>
		</tr>
	</table>
</form>