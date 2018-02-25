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
        $("#dg").zdatagrid({
            url: '${ctx}/blog/category/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'name', title: '名称'},
                {field: 'description', title: '描述'}
            ]],
            pagination : false
        });
		// 新增数据
		$('#add').zbutton({
			operate: 'add',
			title: '新增', 
			width: 400,
			height: 250,
			href: '${ctx}/blog/category/toAdd', 
			posturl: '${ctx}/blog/category/add.json'
		});
		// 编辑选中数据
		$('#edit').zbutton({
			operate: 'edit',
			title: '编辑', 
			width: 400,
			height: 250,
			href: '${ctx}/blog/category/toEdit', 
			posturl: '${ctx}/blog/category/edit.json', 
			dataurl: '${ctx}/blog/category/data.json',
			pk: 'id'
		});
		// 删除选中数据
		$('#del').zbutton({
			operate: 'del',
			posturl: '${ctx}/blog/category/delete.json',
			pk: 'id'
		});
		// 查询数据
		$('#search').zbutton({
			operate: 'search'
		});
</script>
</body>
</html>