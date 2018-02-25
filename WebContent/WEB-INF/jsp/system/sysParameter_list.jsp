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
            url: '${ctx}/system/parameter/datas.json',
            columns: [[
                {field: 'id', title: '主键', hidden: true},
                {field: 'name', title: '参数名称'},
                {field: 'paramKey', title: '英文键，唯一'},
                {field: 'paramValue', title: '参数值'},
                {field: 'remark', title: '备注', formatter: function(value){
        			if(value.length>20){
        				value = value.substr(0,20) + '...';
        			}
        			return value;
        		}},
                {field: 'createTime', title: '创建时间', sortable: true}
            ]]
        });
        // 新增数据
        $('#add').zbutton({
            operate: 'add',
            href: '${ctx}/system/parameter/toAdd',
            posturl: '${ctx}/system/parameter/add.json'
        });
        // 编辑选中数据
        $('#edit').zbutton({
            operate: 'edit',
            href: '${ctx}/system/parameter/toEdit',
            posturl: '${ctx}/system/parameter/edit.json',
            dataurl: '${ctx}/system/parameter/data.json'
        });
        // 删除选中数据
        $('#del').zbutton({
            operate: 'del',
            posturl: '${ctx}/system/parameter/delete.json'
        });
        // 查询数据
        $('#search').zbutton({
            operate: 'search'
        });
</script>
</body>
</html>