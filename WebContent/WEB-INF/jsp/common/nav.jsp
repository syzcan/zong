<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="layout bg-main bg-inverse fixed-top" style="z-index: 9;">
	<div class="container-layout">
		<div class="navbar">
			<!--描述：LOGO及系统名称-->
			<div class="navbar-head">
				<button class="button icon-navicon" data-target="#navbar1"></button>
				<a href="${ctx }/"> <img src="${ctx }/${zparam.LOGO.param_value}" onerror="this.src='${ctx }/static/images/logo.png'" /> <strong class="hidden-l hidden-s">${zparam.WEB_NAME.param_value}</strong></a>
			</div>
			<!--描述：导航-->
			<div class="navbar-body nav-navicon" id="navbar1">
				<ul class="nav nav-inline nav-menu">
					<c:forEach items="${sessionScope.sessionMenus }" var="firstMenu">
					<li><a class="${firstMenu.icon }" href="javascript:;"> ${firstMenu.name }<c:if test="${fn:length(firstMenu.childMenus)>0 }"><span class="arrow"></span></c:if></a>
						<c:if test="${fn:length(firstMenu.childMenus)>0 }">
						<ul class="drop-menu">
						<c:forEach items="${firstMenu.childMenus }" var="secondMenu">
						<li><a href="${ctx}/${secondMenu.url}"><span class="${secondMenu.icon }"></span> ${secondMenu.name }<c:if test="${fn:length(secondMenu.childMenus)>0 }"><span class="arrow"></span></c:if></a>
							<c:if test="${fn:length(secondMenu.childMenus)>0 }">
							<ul class="drop-menu">
							<c:forEach items="${secondMenu.childMenus }" var="menu">
							<li><a href="${ctx}/${menu.url}"><span class="${menu.icon }"></span> ${menu.name }</a></li>
							</c:forEach>
							</ul>
							</c:if>
						</li>
						</c:forEach>
						</ul>
						</c:if>
					</li>	
					</c:forEach>
				</ul>
				<!--描述：右侧用户资料-->
				<ul class="nav nav-inline nav-menu navbar-right">
					<li><a class="bg-main" href="javascript:;"> <span> <img src="${ctx }/static/images/avatar.jpg" width="28" class="radius-circle"></span> ${sessionScope.sessionUser.nickname } <span
							class="downward"></span>
					</a>
						<ul class="drop-menu">
							<li><a class="icon-user" target="_blank" href="javascript:;" onclick="openFrame('修改','${ctx}/system/user/toEdit?id=${sessionScope.sessionUser.id }','760px','475px')">修改资料</a></li>
							<li><a class="icon-key" target="_blank" href="javascript:;" onclick="openFrame('修改密码','${ctx}/password','300px','350px')">修改密码</a></li>
						</ul></li>
					<li><a class="bg-yellow" href="${ctx }/logout"><span class="icon-sign-out"></span>注销</a></li>
				</ul>
			</div>
		</div>
	</div>
</div>
