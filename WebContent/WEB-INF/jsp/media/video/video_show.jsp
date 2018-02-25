<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" />
<style>*{margin:0}</style>
<script type="text/javascript" src="${ctx}/plugins/jquery/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/plugins/ckplayer/ckplayer.js"></script>
<div id="video" style="width: 830px;height:460px;"></div>
<script type="text/javascript">
	var currentTime=0;
	var videoObject = {
		container: '#video',//“#”代表容器的ID，“.”或“”代表容器的class
		variable: 'player',//该属性必需设置，值等于下面的new chplayer()的对象
		loaded:'loadedHandler',
		volume:0.7,
		poster:'${ctx }/${video.cover}',//封面图片
		video:'${ctx }/${video.url}'//视频地址
	};
	var player=new ckplayer(videoObject);
	function loadedHandler(){//播放器加载后会调用该函数
		player.addListener('time', timeHandler); //监听播放时间,addListener是监听函数，需要传递二个参数，'time'是监听属性，这里是监听时间，timeHandler是监听接受的函数
	}
	function timeHandler(t){
		//console.log('当前播放的时间：'+t);
		currentTime = t;
	}
//设置当前画面为视频封面
function setCover() {
	var id = '${video.id}';
	parent.layer.load(1);
	parent.$.ajax({
		url : '${ctx}/media/video/setCover',
		type : 'post',
		data : {
			id : id,
			currentTime : currentTime
		},
		dataType : 'text',
		success : function(data) {
			parent.layer.closeAll('loading');
		}
	});
}
//layer这个样式影响全屏
$('#video').parents('.layer-anim').removeClass('layer-anim');
</script>

<div style="padding: 0 20px; line-height: 45px">
	<input type="button" class="button border-green" value="设为封面" onclick="setCover()"/> 
	【时长：${video.lengthString } &nbsp;&nbsp;大小：${video.sizeString } &nbsp;&nbsp;分辨率：${video.width }x${video.height }】
	<span style="color: #CC0000; float: right; font-size: 12px"><fmt:formatDate
			value="${video.createTime }" pattern="yyyy-MM-dd HH:mm" /></span>
</div>