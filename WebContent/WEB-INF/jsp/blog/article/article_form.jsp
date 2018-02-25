<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
	<input type="hidden" name="id" />
	<table class="table-form">
		<tr>
			<td align="right">类别：</td>
			<td>
				<input class="easyui-combobox" name="cateId"
		    	data-options="required:true,valueField:'id',textField:'name',
		    	url:ctx+'/blog/article/cateBox.json'
		    	">
			</td>
		</tr>
		<tr>
			<td align="right">标题：</td>
			<td>
				<input name="title" class="easyui-textbox" data-options="required:true" />
			</td>
		</tr>
		<tr>
			<td align="right">内容：</td>
			<td>
				<textarea name="content" class="ueditor" style="width:600px;height:250px;"></textarea>
			</td>
		</tr>
		<tr>
			<td align="right">摘要：</td>
			<td>
				<textarea name="description" class="easyui-textbox" data-options="multiline:true" style="width:300px;height:60px;"></textarea>
			</td>
		</tr>
		<tr>
			<td align="right">封面：</td>
			<td>
				<input name="cover" class="zuploadbox" data-options="width:240,buttonText:'选择图片',uploadType:'picture'" />
			</td>
		</tr>
		<tr>
			<td align="right">文章性质：</td>
			<td>
				<select class="easyui-combobox" name="nature" data-options="width:60">
		        	<option value="1">原创</option>
		        	<option value="2">转载</option>
		        </select>
			</td>
		</tr>
		<tr>
			<td align="right">标签：</td>
			<td>
				<input name="tags" class="easyui-textbox" data-options="prompt:'最多添加5个标签，多个标签之间用“,”分隔'" />
			</td>
		</tr>
		<tr>
			<td align="right">来源地址：</td>
			<td>
				<input name="url" class="easyui-textbox" data-options="validType:'url'" />
			</td>
		</tr>
		<tr>
			<td align="right">状态：</td>
			<td>
				<select class="easyui-combobox" name="status" data-options="width:60">
		        	<option value="0">草稿</option>
		        	<option value="1">发布</option>
		        </select>
			</td>
		</tr>
	</table>
</form>