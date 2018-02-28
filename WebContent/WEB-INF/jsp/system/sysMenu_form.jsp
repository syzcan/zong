<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<form id="form" method="post">
    <input type="hidden" name="id" />
    <table class="table-form">
        <tr>
            <td align="right">菜单名称：</td>
            <td>
                <input name="name" class="easyui-textbox" data-options="required:true" />
            </td>
        </tr>
        <tr>
            <td align="right">菜单地址：</td>
            <td>
                <input name="url" class="easyui-textbox" data-options="required:true" />
            </td>
        </tr>
        <tr>
            <td align="right">上级菜单：</td>
            <td>
                <input name="pid" class="easyui-combotree" data-options="required:true,url:ctx+'/system/menu/datas.json?pid=1&id=${param.id}'" />
            </td>
        </tr>
        <tr>
            <td align="right">排序：</td>
            <td>
                <input name="sort" class="easyui-numberspinner" data-options="required:true,min:1,max:60,prompt:'输入范围1到99之间'" />
            </td>
        </tr>
        <tr>
            <td align="right">菜单图标：</td>
            <td>
                <input name="icon" class="easyui-textbox" data-options="" />
            </td>
        </tr>
        <tr>
            <td align="right">类别：</td>
            <td>
                <select class="easyui-combobox" name="type" style="width: 60px">
                	<option value="nav">导航</option>
                	<option value="button">按钮</option>
                </select>
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
    </table>
</form>