<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<body>
<!-- 工具栏 -->
<div id="toolbar" class="easyui-toolbar">
    <a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-plus'" id="add">新增</a>
    <a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-edit'" id="edit">编辑</a>
    <a class="easyui-linkbutton btn-red" data-options="iconCls:'fa fa-remove'" id="del">删除</a>
</div>
<!-- 数据网格 -->
<table id="dg" data-options="toolbar:'#toolbar'"></table>
<!-- 表单弹出层 -->
<div style="display: none">
    <div id="dialog" data-options="buttons:'#btns'"></div>
    <div id="btns">
        <a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-save'" onclick="$('#form').submit()">保存</a>
        <a class="easyui-linkbutton btn-red" data-options="iconCls:'fa fa-close'" onclick="$('#dialog').dialog('close')">关闭</a>
    </div>
</div>
<script>
        // 数据列表
        $("#dg").ztreegrid({
            url: '${ctx}/system/menu/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'name', title: '菜单名称'},
                {field: 'url', title: '菜单地址'},
                {field: 'sort', title: '排序'},
                {field: 'icon', title: '菜单图标'},
                {field: 'type', title: '类别', formatter: function(value){
                    if (value == 'nav'){
                        return '导航';
                    } else if (value == 'button'){
                        return '按钮';
                    }
                }},
                {field: 'status', title: '状态', formatter: function(value){
                	return value==1?'<span class="badge bg-green">启用</span>':'<span class="badge bg-red">禁用</span>';
                }}
            ]]
        });
        // 新增数据
        $('#add').zbutton({
            operate: 'add',
            href: '${ctx}/system/menu/toAdd',
            posturl: '${ctx}/system/menu/add.json',
            gridtype: 'treegrid'
        });
        // 编辑选中数据
        $('#edit').zbutton({
            operate: 'edit',
            href: '${ctx}/system/menu/toEdit',
            posturl: '${ctx}/system/menu/edit.json',
            dataurl: '${ctx}/system/menu/data.json',
            gridtype: 'treegrid'
        });
        // 删除选中数据
        $('#del').zbutton({
            operate: 'del',
            posturl: '${ctx}/system/menu/delete.json',
            gridtype: 'treegrid'
        });
</script>
</body>
</html>