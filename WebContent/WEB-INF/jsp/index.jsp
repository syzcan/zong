<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>${empty zparam.WEB_NAME.param_value?'月光边境':zparam.WEB_NAME.param_value}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
    <style type="text/css">
    body{margin: 0;padding: 0}
    .banner{height:40px;line-height:40px;background: #3c8dbc;color: #fff }
    .banner .logo{float: left;}
    .banner .logo img{width:30px;height:30px;margin:5px;float: left;border-radius:50%}
    .banner .logo .webname{font-weight: bold;font-size: 20px;float: left;}
    /**加上下面，否则右侧iframe会出现很小的滚动条*/
 	.tabs-panels .panel-body{overflow: hidden;} 
 	.panel-header{height: 25px;}
.panel-header .panel-title{height:25px;line-height: 25px}
.panel-header,.tree-node{padding-left: 20px;}
.panel-icon{padding-left: 12px}
.tree-node,
.tree-expanded,
.tree-collapsed,
.tree-folder,
.tree-file,
.tree-checkbox,
.tree-indent,
.tree-title,
.tree-node-proxy,
.tree-dnd-icon,
.tree-editor,
.tree-node .tree-icon{height: 30px;line-height: 30px;}
.banner .l-btn{color: #fff}
.banner .l-btn:hover{color: #000}
	</style>
</head>
<body class="easyui-layout">
	<!-- 头部信息 -->
	<div class="banner" data-options="region:'north',border:false">
		<div class="logo">
			<img src="${ctx }/${zparam.LOGO.param_value}" onerror="this.src='${ctx }/static/images/logo.png'"/>
			<div class="webname">
	            ${zparam.WEB_NAME.param_value}
			</div>
		</div>
		<div style="padding-right:10px;float: right;">
			<a class="easyui-linkbutton" data-options="iconCls:'fa fa-user fa-lg',plain:true">${sessionScope.sessionUser.username }</a>|
			<a class="easyui-menubutton" data-options="menu:'#setting',iconCls:'fa fa-cog fa-lg'">设置</a>|
			<a class="easyui-linkbutton" data-options="iconCls:'fa fa-sign-out fa-lg',plain:true" onclick="location.href='${ctx}/logout'">注销</a>
		</div>
		<div id="setting" style="width:100px;">
			<div data-options="iconCls:'fa fa-info-circle'" id="userinfoBt">个人信息</div>
			<div data-options="iconCls:'fa fa-key'" id="passwordBt">修改密码</div>
		</div>
		<!-- 表单弹出层 -->
		<div style="display: none">
		    <div id="dialog" data-options="buttons:'#btns'"></div>
		    <div id="btns">
		        <a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-save'" onclick="$('#form').submit()">保存</a>
		        <a class="easyui-linkbutton btn-red" data-options="iconCls:'fa fa-close'" onclick="$('#dialog').dialog('close')">关闭</a>
		    </div>
		</div>
	</div>
	<!-- 左侧导航 -->
	<div data-options="region:'west',width:180">
		<div id="z-accordion" class="easyui-accordion" data-options="border:false"></div>
	</div>
	<!-- 中间内容 -->
	<div data-options="region:'center'">
		<div id="z-tabs" class="easyui-tabs" data-options="border:false,fit:true,tools:[{iconCls:'fa fa-refresh',plain:true,
		handler:function(){
			$('#mm-tabupdate').click();
		}}]">
			<div title="主页" data-options="iconCls:'fa fa-home'">
				<img src="http://127.0.0.1:66/a/60478875_p1_master1200.jpg"/>
			</div>
		</div>
		<!-- 导航栏右键菜单 -->
		<div id="mm" class="easyui-menu" style="width: 100px;">
			<div id="mm-tabupdate" data-options="iconCls:'fa fa-refresh'">刷新</div>
			<div class="menu-sep"></div>
			<div id="mm-tabclose" data-options="iconCls:'fa fa-close'">关闭</div>
			<div id="mm-tabcloseall">全部关闭</div>
			<div id="mm-tabcloseother">关闭其他</div>
		</div>
	</div>
	<!-- 底部版权 -->
	<%-- <div data-options="region:'south',border:false" style="height:30px;line-height:30px;text-align: center;">
		Copyright © 2018-2020 ${zparam.WEB_NAME.param_value }
	</div> --%>
</body>
<script type="text/javascript">
// 生成菜单
function generateMenu(){
	$.get('${ctx}/system/menu/userMenus.json',function(data){
		$.each(data,function(i,n){
            var isSelected = i == 0 ? true : false;
			// 第一级菜单使用手风琴
			$('#z-accordion').accordion('add',{
                title: n.text,
                content: "<ul id='tree" + n.id + "' ></ul>",
                selected: isSelected,
                iconCls: n.iconCls
            });
			// 第二级以下菜单树
            $("#tree" + n.id).tree({
                data: n.children,
                lines: false,
                animate: true,
                onClick: function (node) {
                	node.title = node.text;
                    if (node.url && node.url!='#') {
                        addTab(node);
                    } else {
                        if (node.state == "closed") {
                            $("#tree" + n.id).tree('expand', node.target);
                        } else if (node.state == 'open') {
                            $("#tree" + n.id).tree('collapse', node.target);
                        }
                    }
                }
            });
		});
	});
}
// 创建tab
function addTab(params){
	var content = createFrame(params.url);
	// 如果存在则选中
	if($('#z-tabs').tabs('exists',params.title)){
		$('#z-tabs').tabs('select',params.title);
		var extab = $('#z-tabs').tabs('getTab',params.title);
		var url = $(extab.panel('options').content).attr('src');
		if (url != params.url) {
			$('#z-tabs').tabs('update', {
				tab : extab,
				options : {
					content : createFrame(params.url)
				}
			});
		}
		return;
	}
	$('#z-tabs').tabs('add',{
		title: params.title,
		iconCls: params.iconCls,
		content: content,
		closable: true
	});
}
function createFrame(url){
	return '<iframe src="'+ url +'"  scrolling="auto" frameborder="0" style="width:100%;height:100%" ></iframe>';
}

// 页面初始化
$(function(){
	// 创建左侧菜单
	generateMenu();
	// tab右键菜单事件
	$(document).on('contextmenu', '.tabs-inner', function(e) {
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY
		});

		var subtitle = $(this).children(".tabs-closable").text();
		// 右键记录当前tab
		$('#mm').data("currtab", subtitle);
		$('#z-tabs').tabs('select', subtitle);
		return false;
	});
	//刷新
	$(document).on('click', '#mm-tabupdate', function() {
		var currTab = $('#z-tabs').tabs('getSelected');
		var url = $(currTab.panel('options').content).attr('src');
		if (url) {
			$('#z-tabs').tabs('update', {
				tab : currTab,
				options : {
					content : createFrame(url)
				}
			});
		}
	});
	//关闭当前
	$(document).on('click', '#mm-tabclose', function() {
		var currtab_title = $('#mm').data("currtab");
		$('#z-tabs').tabs('close', currtab_title);
	})
	//全部关闭
	$(document).on('click', '#mm-tabcloseall', function() {
		$('.tabs-inner span').each(function(i, n) {
			var t = $(n).text();
			$('#z-tabs').tabs('close', t);
		});
	});
	//关闭除当前之外的TAB
	$(document).on('click', '#mm-tabcloseother', function() {
		$('.tabs-selected').siblings().each(function(i,n){
			var t=$('a:eq(0) span',$(n)).text();
			$('#z-tabs').tabs('close',t);
		});
	});
	
	$('#userinfoBt').zbutton({
		operate: 'ajax',
		title: '个人信息',
		href: '${ctx}/userInfo',
		posturl: '${ctx}/saveInfo.json',
		dataurl: '${ctx}/userInfo.json'
	});
	
	$('#passwordBt').zbutton({
		operate: 'ajax',
		title: '修改密码',
		width: 400,
		height: 250,
		href: '${ctx}/password',
		posturl: '${ctx}/savePassword.json'
	});
});
</script>
<div id="dialog_common"></div>
</html>