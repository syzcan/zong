<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
	<input type="hidden" name="id" />
	<table class="table-form">
		<tr>
			<td align="right">视频文件：</td>
			<td>
				<input name="url" class="zuploadbox" data-options="buttonText:'选择视频',width:240,uploadType:'video',success:function(data){
					$('#title').textbox('setValue',data.fileName.substring(0,data.fileName.lastIndexOf('.')));
					$('#cover').textbox('setValue',data.cover);
					$('#length').textbox('setValue',data.length);
					$('#size').textbox('setValue',data.size);
					$('#width').textbox('setValue',data.width);
					$('#height').textbox('setValue',data.height);
					$('#md5').val(data.md5);
				}" />
				<input id="md5" name="md5" type="hidden"/>
			</td>
		</tr>
		<tr>
			<td align="right">标题：</td>
			<td>
				<input id="title" name="title" class="easyui-textbox" data-options="required:true" />
			</td>
		</tr>
		<tr>
			<td align="right">封面图：</td>
			<td>
				<input id="cover" name="cover" class="easyui-textbox" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">长度秒：</td>
			<td>
				<input id="length" name="length" class="easyui-numberbox" data-options="editable:false" />
			</td>
		</tr>
		<tr>
			<td align="right">大小-字节：</td>
			<td>
				<input id="size" name="size" class="easyui-numberbox" data-options="editable:false" />
			</td>
		</tr>
		<tr>
			<td align="right">帧宽度：</td>
			<td>
				<input id="width" name="width" class="easyui-numberbox" data-options="editable:false" />
			</td>
		</tr>
		<tr>
			<td align="right">帧高度：</td>
			<td>
				<input id="height" name="height" class="easyui-numberbox" data-options="editable:false" />
			</td>
		</tr>
		<tr>
			<td align="right">星级：</td>
			<td>
				<input name="star" class="easyui-numberspinner" data-options="" />
			</td>
		</tr>
		<tr>
			<td align="right">标签：</td>
			<td>
				<input name="tags" class="easyui-textbox" data-options="" />
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