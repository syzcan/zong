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
        <a class="easyui-linkbutton btn-yellow" data-options="iconCls:'fa fa-download'" id="export">导出</a>
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
            url: '${ctx}/system/user/datas.json',
            columns: [[
                {field: 'id', title: '主键', checkbox: true},
                {field: 'avatar', title: '头像', formatter:avatarFormatter},
                {field: 'username', title: '用户名'},
                {field: 'nickname', title: '昵称'},
                {field: 'gender', title: '性别', formatter:genderFormatter},
                {field: 'status', title: '状态', formatter:statusFormatter},
                {field: 'lastLogin', title: '上次登录时间', sortable: true},
                {field: 'ip', title: '登录ip'},
                {field: 'updateTime', title: '最后修改', sortable: true}
            ]],
            singleSelect: false
        });
        function avatarFormatter(value, rowData, rowIndex){
			return '<img style="width: 60px;height: 60px;border-radius:50%" src="${ctx }/'+value+'" onerror="this.src=\'${ctx }/static/images/avatar.jpg\'" />';
		}
		
		function statusFormatter(value, rowData, rowIndex){
			return value == 1?'<span class="layui-badge layui-bg-green">启用</span>':'<span class="layui-badge layui-bg-red">禁用</span>';
		}
		
		function genderFormatter(value, rowData, rowIndex){
			return value == '男'?'<span class="fa fa-2x fa-mars text-blue"></span>':'<span class="fa fa-2x fa-venus text-red"></span>';
		}
        // 新增数据
        $('#add').zbutton({
            operate: 'add',
            height: 450,
            href: '${ctx}/system/user/toAdd',
            posturl: '${ctx}/system/user/add.json'
        });
        // 编辑选中数据
        $('#edit').zbutton({
            operate: 'edit',
            height: 450,
            href: '${ctx}/system/user/toEdit',
            posturl: '${ctx}/system/user/edit.json',
            dataurl: '${ctx}/system/user/data.json'
        });
        // 删除选中数据
        $('#del').zbutton({
            operate: 'del',
            posturl: '${ctx}/system/user/delete.json'
        });
        // 导出选中数据
        $('#export').zbutton({
            operate: 'export',
            posturl: '${ctx}/system/user/exportExcel'
        });
        // 查询数据
        $('#search').zbutton({
            operate: 'search'
        });
</script>
</body>
</html>