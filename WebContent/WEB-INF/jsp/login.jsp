<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>${empty zparam.WEB_NAME.param_value?'月光边境':zparam.WEB_NAME.param_value}</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
    <style type="text/css">
    .panel-title{height: 30px;line-height: 30px;text-align: center;}
    .textbox-icon{line-height: 28px}
    </style>
</head>
<body style="background: #EBF2FC">
<form id="form" method="post" action="${ctx }/login.json" style="margin:100px auto;width: 400px">
	<div class="easyui-panel" title="欢迎使用${zparam.WEB_NAME['param_value'] }" style="width:100%;width:400px;padding:30px 60px;text-align: center;">
		<div style="margin-bottom:20px">
			<input id="username" name="username" class="easyui-textbox" required label="账号:" iconCls="fa fa-user" labelPosition="top" style="width:100%;height:52px">
		</div>
		<div style="margin-bottom:20px">
			<input id="password" name="password" class="easyui-textbox" required label="密码:" iconCls="fa fa-key" labelPosition="top" style="width:100%;height:52px" type="password">
		</div>
		
		<div style="margin-bottom:20px">
			<a onclick="$('#form').submit()" class="easyui-linkbutton" iconCls="fa fa-check text-green" style="width:100%;height:32px">登录</a>
		</div>
		<div style="float: left;">
			<input type="checkbox" id="rememberMe" name="rememberMe" value="1" />记住我
		</div>
	</div>
</form>
</body>
<script type="text/javascript">
$('#form').form({
	onSubmit : function() {
		var valid = $('#form').form('validate');
		if(valid){
			saveCookie();
		}
		return valid;
	},
	success : function(result) {
		result = eval('(' + result + ')');
		if(result.retCode == 200){
			location.href='${ctx}/';
		}else{
			$.messager.alert('提示', result.retMsg, 'warning');
		}
	}
});
//记住我，把账号密码保存到cookie
function saveCookie() {
	if ($("#rememberMe").prop("checked")) {
		$.cookie('username', $("#username").textbox('getValue'), {
			expires : 7
		});
		$.cookie('password', $("#password").textbox('getValue'), {
			expires : 7
		});
	}else{
		$.cookie('username', '', {
			expires : -1
		});
		$.cookie('password', '', {
			expires : -1
		});
	}
}

$(function() {
	var username = $.cookie('username');
	var password = $.cookie('password');
	if (typeof(username) != "undefined" && username != null) {
		$('#username').textbox('setValue',username);
		$('#password').textbox('setValue',password);
		$("#rememberMe").prop("checked", true);
	}
	
	$(document).on('keydown','#form input',function(e){
		if(e.which == 13){
			$('#form').submit();
		}
	});
});
</script>
</html>