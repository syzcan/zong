<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
    <input type="hidden" name="id" />
    <table class="table-form">
        <tr>
            <td align="right">用户名：</td>
            <td>
                <input name="username" class="easyui-textbox" data-options="prompt:'由后台生成',editable:false" />
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
                <input name="avatar" type="hidden" class="zuploadbox" data-options="
                width: 240,
                height: 26,
                buttonText:'选择图片',
                uploadType:'picture',
				success : function(data) {
					$('img[data-src]').attr('src', ctx + '/' + data.filePath);
				}" />
                <img data-src="avatar" style="position: absolute;left: 300px;bottom:0;width:120px;height:120px">
            </td>
        </tr>
        <tr>
            <td align="right">状态：</td>
            <td>
                <select class="easyui-combobox" name="status" style="width: 60px">
                	<option value="1">启用</option>
                	<option value="2">禁用</option>
                </select>
            </td>
        </tr>
        <tr>
            <td align="right">角色：</td>
            <td>
                <input name="roleId" class="easyui-combobox" data-options="required:true,
                	multiple:true,
                	valueField:'id',
					textField:'text',
					url:ctx + '/system/user/roleBox.json?userId=${param.id }'" />
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
