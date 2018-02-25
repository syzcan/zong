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
	<div style="float: left;">
	    <a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-plus'" id="add">新增</a>
	    <a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-edit'" id="edit">编辑</a>
	    <a class="easyui-linkbutton btn-red" data-options="iconCls:'fa fa-remove'" id="del">删除</a>
	    <a class="easyui-linkbutton btn-yellow" data-options="iconCls:'fa fa-cloud-upload'" id="upload">上传图片</a>
    </div>
    <form style="float: left;margin-left: 20px">
        <input type="text" name="keyword" class="easyui-textbox" data-options="prompt:'关键字',width:120">
        <a class="easyui-linkbutton btn-cyan" data-options="iconCls:'fa fa-search'" id="search">查询</a>
    </form>
    <div style="clear: both;"></div>
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
            url: '${ctx}/media/photo/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'name', title: '名称', editor:'text'},
                {field: 'cover', title: '封面', formatter: function(value){
                	return '<img style="width:120px" src="${ctx}/'+ value +'">';
                }},
                {field: 'tabs', title: '标签'},
                {field: 'remark', title: '备注'},
                {field: 'status', title: '状态', formatter: function(value){
                	return value==1?'<span class="badge bg-green">发布</span>':'<span class="badge bg-yellow">待发布</span>';
                }},
                {field: 'createTime', title: '创建时间'},
                {field: 'updateTime', title: '更新时间'}
            ]],
            rownumbers : false
        });
		// 新增数据
		$('#add').zbutton({
			operate: 'add',
			title: '新增', 
			href: '${ctx}/media/photo/toAdd', 
			posturl: '${ctx}/media/photo/add.json'
		});
		// 编辑选中数据
		$('#edit').zbutton({
			operate: 'edit',
			title: '编辑', 
			href: '${ctx}/media/photo/toEdit', 
			posturl: '${ctx}/media/photo/edit.json', 
			dataurl: '${ctx}/media/photo/data.json',
			pk: 'id'
		});
		// 删除选中数据
		$('#del').zbutton({
			operate: 'del',
			posturl: '${ctx}/media/photo/delete.json',
			pk: 'id'
		});
		// 查询数据
		$('#search').zbutton({
			operate: 'search'
		});
		// 删除选中数据
		$('#upload').zbutton({
			operate: 'tab',
			title: '相册-{name}', 
			iconCls: 'fa fa-cloud-upload',
			href: '${ctx}/media/photoItem/list',
			pk: 'id'
		});
		
</script>
</body>
</html>