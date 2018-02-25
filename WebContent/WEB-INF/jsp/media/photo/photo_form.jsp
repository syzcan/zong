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
			<td align="right">封面：</td>
			<td>
				<input name="cover" class="zuploadbox" data-options="width:240,uploadType:'picture',buttonText:'选择图片'" />
			</td>
		</tr>
		<tr>
			<td align="right">标签：</td>
			<td>
				<input name="tabs" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">备注：</td>
			<td>
				<textarea name="remark" class="easyui-textbox" data-options="multiline:true" style="width:300px;height:60px;"></textarea>
			</td>
		</tr>
		<tr>
			<td align="right">状态：</td>
			<td>
				<select class="easyui-combobox" name="status" data-options="width:70">
		        	<option value="0">待发布</option>
		        	<option value="1">发布</option>
		        </select>
			</td>
		</tr>
	</table>
</form>