<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath }/static/pintuer/pintuer.css" />
<script type="text/javascript" src="${pageContext.request.contextPath }/plugins/jquery/jquery.min.js"></script>
<style>
.tab-nav li:first-child{margin-right:6px}
.tab-nav li:not(:first-child){margin-right: 10px}
.badge-corner .badge{padding: 1px;width:14px;top:-6px;right:-6px}
.tab .tab-nav li a{padding:5px 10px}
</style>
	<div class="tab" style="padding:10px 15px">
		<div class="tab-head">
			<strong><a class="icon-plus" href="javascript:;" onclick="addThread()"> 增加</a></strong>
			<ul class="tab-nav">
				<li class="active"><a href="javascript:;">线程1</a></li>
			</ul>
		</div>
		<div class="tab-body">
			<div class="tab-panel active">
				<iframe src="${pageContext.request.contextPath }/craw/toCrawDetail?rule_id=${param.rule_id }&craw_store=${param.craw_store}&js_enabled=${param.js_enabled}" onload="changeFrameHeight(this)" scrolling="auto" frameborder="0" width="100%" height="100%" ></iframe>
			</div>
		</div>
	</div>
<script>
function addThread(){
	var index = $('.tab-nav li').size()+1;
	$('.tab-nav').append('<li class="badge-corner"><a href="javascript:;">线程'+index+'</a><span class="badge">×</span></li>');
	$('.tab-body').append($('.tab-body>div').first().clone());
	$('.tab-nav li').last().find('a').click();
}
$(document).on('click','.tab-nav a',function(){
	$(this).parent().addClass('active').siblings().removeClass('active');
	$('.tab-body>div').removeClass('active');
	$('.tab-body>div').eq($(this).parent().index()).addClass('active');
	// 调整iframe高度
	$('iframe').each(function(i,n){
		changeFrameHeight(this);  
	});
});
$(document).on('click','.tab-nav .badge',function(){
	if($(this).parent().hasClass('active')){
		$('.tab-nav>li').eq(0).addClass('active');
		$('.tab-body>div').eq(0).addClass('active');
	}
	$('.tab-body>div').eq($(this).parent().index()).remove();
	$(this).parent().remove();
	$('.tab-nav li').each(function(){
		$(this).find('a').html('线程'+($(this).index()+1));
	});
});
function changeFrameHeight(obj){
    obj.height=obj.contentWindow.$('#list_div').height();
}
window.onresize=function(){
	$('iframe').each(function(i,n){
		changeFrameHeight(this);  
	});
} 
</script>