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
            url: '${ctx}/media/music/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'title', title: '标题'},
                {field: 'artist', title: '艺术家'},
                {field: 'album', title: '专辑'},
                {field: 'style', title: '流派'},
                {field: 'remark', title: '备注'},
                {field: 'length', title: '长度', formatter: function(value){
                	min = parseInt(value/60);
                	return (min<10?('0'+min):min) + ':' + value%60;
                }},
                {field: 'size', title: '大小', formatter: function(value){
                	return (value/1024/1024).toFixed(2) + 'M';
                }},
                //{field: 'url', title: '路径'},
                {field: 'createTime', title: '创建时间'}
            ]]
        });
		// 新增数据
		$('#add').zbutton({
			operate: 'add',
			title: '新增', 
			height: 450,
			href: '${ctx}/media/music/toAdd', 
			posturl: '${ctx}/media/music/add.json'
		});
		// 编辑选中数据
		$('#edit').zbutton({
			operate: 'edit',
			title: '编辑', 
			height: 450,
			href: '${ctx}/media/music/toEdit', 
			posturl: '${ctx}/media/music/edit.json', 
			dataurl: '${ctx}/media/music/data.json',
			pk: 'id'
		});
		// 删除选中数据
		$('#del').zbutton({
			operate: 'del',
			posturl: '${ctx}/media/music/delete.json',
			pk: 'id'
		});
		// 查询数据
		$('#search').zbutton({
			operate: 'search'
		});
</script>
</body>
</html>