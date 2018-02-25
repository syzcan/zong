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
    	<input class="easyui-combobox" name="cate_id"
    	data-options="valueField:'id',textField:'name',
    	url:'${ctx}/blog/article/cateBox.json',
    	onChange:function(){
    		$('#search').click();
    	}
    	">
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
            url: '${ctx}/blog/article/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'cover', title: '封面', formatter: function(value){
                	if(value!=null && value!=''){
                		if(value.indexOf('http') == 0){
	                		return '<img style="width:120px" src="'+ value +'">';
                		}else{
	                		return '<img style="width:120px" src="${ctx}/'+ value +'">';
                		}
                	}
                	return value;
                }},
                {field: 'title', title: '标题'},
                {field: 'description', title: '摘要', formatter: function(value){
        			if(value.length>20){
        				value = value.substr(0,20) + '...';
        			}
        			return value;
        		}},
                {field: 'clickCount', title: '浏览'},
                {field: 'replyCount', title: '回复'},
                {field: 'nature', title: '文章性质', formatter: function(value){
                	return value==1?'原创':'转载';
                }},
                {field: 'tags', title: '标签'},
                {field: 'status', title: '状态', formatter: function(value){
                	return value==1?'<span class="badge bg-green">发布</span>':'<span class="badge bg-yellow">草稿</span>';
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
			width: 800,
			height: 500,
			maximizable: true,
			href: '${ctx}/blog/article/toAdd', 
			posturl: '${ctx}/blog/article/add.json'
		});
		// 编辑选中数据
		$('#edit').zbutton({
			operate: 'edit',
			title: '编辑', 
			width: 800,
			height: 500,
			maximizable: true,
			href: '${ctx}/blog/article/toEdit', 
			posturl: '${ctx}/blog/article/edit.json', 
			dataurl: '${ctx}/blog/article/data.json',
			pk: 'id'
		});
		// 删除选中数据
		$('#del').zbutton({
			operate: 'del',
			posturl: '${ctx}/blog/article/delete.json',
			pk: 'id'
		});
		// 查询数据
		$('#search').zbutton({
			operate: 'search'
		});
</script>
</body>
</html>