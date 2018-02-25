<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<style>
a.active{background: #0ae;color: #fff}
</style>
<body>
	<div id="bar" class="easyui-toolbar datagrid-toolbar" >
		<c:forEach items="${templates }" var="template">
		<a class="easyui-linkbutton btn-blue" data-name="${template }">${template }</a>
		</c:forEach>
	</div>
	<div style="height: 100%">
		<textarea style="width: 100%;padding: 5px;border:0;box-sizing: border-box;resize: none"></textarea>
	</div>
	<div class="easyui-toolbar datagrid-toolbar" style="position: fixed;bottom: 0;width: 100%;border-top:1px solid #ddd">
		<a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-save'" onclick="saveTemplate()">保存</a>
	</div>
</body>
<script>
$(function(){
	$('.easyui-linkbutton[data-name]').click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		$.ajax({
			url:'${ctx}/coder/templates/'+$(this).data('name'),
			dataType:'json',
			success:function(data){
				$('textarea').val(data.content);
			}
		});
	});
	$('.easyui-linkbutton[data-name]').first().click();
	autoHeight();
});
$(window).resize(function() {
	autoHeight()
});
// 文本框高度适应
function autoHeight(){
	$('textarea').height(parent.$('#z-tabs').height()-90-$('#bar').height());
}
function saveTemplate(){
	$.ajax({
		url:'${ctx}/coder/templates/'+$('.active[data-name]').data('name')+'/edit',
		type:'POST',
		dataType:'json',
		data:{
			content:$('textarea').val()
		},
		success:function(data){
			if(data.statusCode==200){
				layer.msg('操作成功');
			}
		}
	});
}

</script>
</html>