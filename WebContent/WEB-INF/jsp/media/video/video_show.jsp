<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<title>月光边境</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@ include file="/WEB-INF/jsp/common/style_easyui.jsp"%>
<script type="text/javascript" src="${ctx}/plugins/ckplayer/ckplayer.js"></script>
</head>
<body class="easyui-layout">
	<div id="video" class="easyui-panel"
		data-options="width:'100%',height:'100%',border:false,footer:'#footer'"></div>
	<script type="text/javascript">
		var currentTime = 0;
		var videoObject = {
			container : '#video',//“#”代表容器的ID，“.”或“”代表容器的class
			variable : 'player',//该属性必需设置，值等于下面的new chplayer()的对象
			loaded : 'loadedHandler',
			volume : 0.7,
			poster : ctx + '/${video.cover}',//封面图片
			video : ctx + '/${video.url}'//视频地址
		};
		var player = new ckplayer(videoObject);
		function loadedHandler() {//播放器加载后会调用该函数
			player.addListener('time', timeHandler); //监听播放时间,addListener是监听函数，需要传递二个参数，'time'是监听属性，这里是监听时间，timeHandler是监听接受的函数
		}
		function timeHandler(t) {
			//console.log('当前播放的时间：'+t);
			currentTime = t;
		}
		//设置当前画面为视频封面
		function setCover() {
			var id = '${video.id}';
			layer.load(1);
			$.ajax({
				url : ctx + '/media/video/setCover.json',
				type : 'post',
				data : {
					id : id,
					currentTime : currentTime
				},
				dataType : 'json',
				success : function(result) {
					layer.closeAll('loading');
					if (result.retCode == 200) {
						$.messager.show({
							title : '提示',
							msg : '操作成功',
							timeout : 2000,
							showType : 'slide'
						});
					} else {
						$.messager.alert('提示', result.retMsg, 'warning');
					}
				}
			});
		}
		var angle = 0
		function rotation(type) {
			if (type == 'left') {
				angle = angle - 90;
			} else {
				angle = angle + 90;
			}
			if (angle == -360 || angle == 360) {
				angle = 0;
			}
			player.videoRotation(angle);
		}
		var scale = 1;
		function zoom(type) {
			if (type == 'in') {
				scale = scale - 0.1;
			} else if (type == 'out'){
				scale = scale + 0.1;
			} else if (type == 'one'){
				scale = 1;
			}
			player.videoZoom(scale);
		}
	</script>
	<div id="footer" style="padding: 0 20px; line-height: 45px">
		<div class="video-bt">
			<a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-reply fa-lg'"
			onclick="rotation('left')" title="左旋90">
			</a>
			<a class="easyui-linkbutton btn-green" data-options="iconCls:'fa fa-share fa-lg'"
			onclick="rotation('right')" title="右旋90">
			</a>
			<a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-search-minus fa-lg'"
			onclick="zoom('in')" title="缩小10%">
			</a>
			<a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-search-plus fa-lg'"
			onclick="zoom('out')" title="放大10%">
			</a>
			<a class="easyui-linkbutton btn-blue" data-options="iconCls:'fa fa-one'"
			onclick="zoom('one')" title="还原">
			</a>
			<a class="easyui-linkbutton btn-yellow" data-options="iconCls:'fa fa-tag fa-lg'"
			onclick="setCover()" title="设为封面">
			</a>
		</div>
		<div class="video-info">
			<div class="text-red">
				<fmt:formatDate value="${video.createTime }"
					pattern="yyyy-MM-dd HH:mm" />
			</div>
			<div>【时长：${video.lengthString }</div>
			<div>大小：${video.sizeString }</div>
			<div>分辨率：${video.width }x${video.height }】</div>
		</div>
	</div>
</body>
<style>
.video-bt {
	float: left;
}
.video-info {
	float: right;
}
.video-info>div {
	float: left;
	margin-left: 10px
}
.fa-one::before {
    content: "1:1";
    background-position: -40px 0px;
}
</style>
</html>