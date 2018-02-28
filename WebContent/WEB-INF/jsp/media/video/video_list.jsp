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
            url: '${ctx}/media/video/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'title', title: '标题', formatter: function(value){
                	var html = '';
                	if(value){
                		var i=0;
	                	while(i<value.length){
	                		html += value.substring(i,i+10) +'<br>';
	                		i = i+10;
	                	}
                	}
                	return html;
        		}},
                {field: 'cover', title: '封面图', formatter: function(value){
                	return '<img style="width:120px" src="${ctx}/'+ value +'">';
                }},
                //{field: 'url', title: '文件路径'},
                {field: 'remark', title: '备注'},
                {field: 'length', title: '长度', formatter: function(value){
                	min = parseInt(value/60);
                	return (min<10?('0'+min):min) + ':' + value%60;
                }},
                {field: 'size', title: '大小', formatter: function(value){
                	return (value/1024/1024).toFixed(2) + 'M';
                }},
                {field: 'width', title: '帧宽度'},
                {field: 'height', title: '帧高度'},
                {field: 'star', title: '星级'},
                {field: 'tags', title: '标签'},
                {field: 'createTime', title: '创建时间'},
                {field: 'updateTime', title: '更新时间'}
            ]],
            rownumbers : false
        });
		// 新增数据
		$('#add').zbutton({
			operate: 'add',
			title: '新增', 
			href: '${ctx}/media/video/toAdd', 
			posturl: '${ctx}/media/video/add.json'
		});
		// 编辑选中数据
		$('#edit').zbutton({
			operate: 'edit',
			title: '编辑', 
			href: '${ctx}/media/video/toEdit', 
			posturl: '${ctx}/media/video/edit.json', 
			dataurl: '${ctx}/media/video/data.json',
			pk: 'id'
		});
		// 删除选中数据
		$('#del').zbutton({
			operate: 'del',
			posturl: '${ctx}/media/video/delete.json',
			pk: 'id'
		});
		// 查询数据
		$('#search').zbutton({
			operate: 'search'
		});
		// 点击标题或封面弹出视频播放器
		$(document).on('click','.datagrid-btable td[field="title"],.datagrid-btable td[field="cover"]',function(){
			var index = parseInt($(this).parent().attr('datagrid-row-index'));
			var row = $("#dg").datagrid('getRows')[index];
			parent.addTab({
				title:row.title.length>6?row.title.substr(0,6):row.title,
				url:ctx+'/media/video/show?id='+row.id,
				iconCls:'fa fa-video-camera'
			});
		});
</script>
</body>
</html>