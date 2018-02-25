<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
	<input type="hidden" name="id" />
	<table class="table-form">
		<tr>
			<td align="right">文件：</td>
			<td>
				<input name="url" class="zuploadbox" data-options="buttonText:'选择音乐',width:240,uploadType:'music',success:function(data){
					$('#title').textbox('setValue',data.fileName.substring(0,data.fileName.lastIndexOf('.')));
					$('#artist').textbox('setValue',data.artist);
					$('#album').textbox('setValue',data.album);
					$('#length').textbox('setValue',data.length);
					$('#size').textbox('setValue',data.size);
				}" />
			</td>
		</tr>
		<tr>
			<td align="right">标题：</td>
			<td>
				<input id="title" name="title" class="easyui-textbox" data-options="required:true" />
			</td>
		</tr>
		<tr>
			<td align="right">艺术家：</td>
			<td>
				<input id="artist" name="artist" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">专辑：</td>
			<td>
				<input id="album" name="album" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">流派：</td>
			<td>
				<input id="style" name="style" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">时间长度-秒：</td>
			<td>
				<input id="length" name="length" class="easyui-numberbox" data-options="editable:false" />
			</td>
		</tr>
		<tr>
			<td align="right">文件大小-字节：</td>
			<td>
				<input id="size" name="size" class="easyui-numberbox" data-options="editable:false" />
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