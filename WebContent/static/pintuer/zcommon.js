/*!
 * zutil工具类，包含表单序列化操作，部分内容依赖layer
 * author: zong
 * version: 1.0
 * build: 2017-03-11
 */
var zutil = function() {
	/**
	 * 序列化form表单数据为json对象 Object {name: "zong", age: "27"}
	 * 
	 * @param $form
	 * @returns json对象
	 */
	this.formJson = function($form) {
		var array = $form.serializeArray();
		var json = {};
		$.each(array, function(i, field) {
			if (json[field.name]) {
				if ($.isArray(json[field.name])) {
					json[field.name].push(field.value);
				} else {
					json[field.name] = [ json[field.name], field.value ];
				}
			} else {
				json[field.name] = field.value;
			}
		});
		return json;
	}

	/**
	 * 序列化form表单数据为字符窜 name=zong&age=27
	 * 
	 * @param $form
	 */
	this.formData = function($form) {
		return $form.serialize();
	}

	/**
	 * ajax方式提交form数据
	 * 
	 * @param $form
	 * @param 回调函数func
	 */
	this.formAjax = function($form, func) {
		var url = $form.attr('action');
		layer.load(1, {
			shade : [ 0.1, '#fff' ]
		});
		$.ajax({
			url : url,
			type : 'POST',
			dataType : 'json',
			data : formJson($form),
			success : function(data) {
				layer.close(layer.index);
				if (data.statusCode == 200) {
					if (typeof func === "function") {
						func(data);
					} else {
						layer.msg('操作成功');
					}
				} else {
					layer.msg(data.message);
				}
			},
			error : function() {
				layer.closeAll();
				layer.msg('请求错误');
			}
		});
	}
	return this;
}()

/**
 * 处理表单复选框选中主键id的数据，更改状态或删除
 * 
 * @param id 表单id
 * @param url 请求地址
 * @param func 回调函数
 */
function dealForm(id, url, func) {
	layer.load(1, {
		shade : [ 0.1, '#fff' ]
	});
	$.ajax({
		url : url,
		type : 'post',
		data : $('#' + id + ' input[name="id"]:checked').serialize(),
		dataType : 'json',
		success : function(data) {
			layer.closeAll();
			if (data.statusCode == 200) {
				if (typeof func === "function") {
					func();
				} else {
					layer.msg('操作成功');
				}
			} else {
				layer.msg(data.message);
			}
		}
	});
}

/**
 * 删除表单复选框选中数据
 * 
 * @param id
 * @param url
 * @param func
 */
function deleteForm(id, url, func) {
	layer.confirm('确定删除吗？', {
		shadeClose : true,
		btn : [ '确定' ]
	}, function() {
		dealForm(id, url, func);
	});
}

/**
 * 处理数据
 * 
 * @param url 请求地址
 * @param func 回调函数
 */
function dealData(url, func) {
	layer.load(1, {
		shade : [ 0.1, '#fff' ]
	});
	$.ajax({
		type : "post",
		url : url,
		dataType : "json",
		success : function(data) {
			layer.closeAll();
			if (data.statusCode == 200) {
				if (typeof func === "function") {
					func();
				} else {
					layer.msg('操作成功');
				}
			} else {
				layer.msg(data.message);
			}
		}
	});
}

/**
 * 删除数据
 * 
 * @param url
 * @param func
 */
function deleteData(url, func) {
	layer.confirm('确定删除吗？', {
		shadeClose : true,
		btn : [ '确定' ]
	// 按钮
	}, function() {
		dealData(url, func);
	});
}

/**
 * 同步校验数据，如验证username是否可用
 * 
 * @param url
 * @returns {Boolean}
 */
function checkData(url) {
	var flag = false;
	$.ajax({
		type : "get",
		async : false,
		url : url,
		dataType : "json",
		success : function(data) {
			if (data.statusCode == 200) {
				flag = true;
			}
		}
	});
	return flag;
}

// 视频弹出层
function openVideo(title, url) {
	layer.open({
		type : 2,
		title : title,
		area : [ '830px', '550px' ],
		shadeClose : true,
		maxmin : false,
		content : url
	});
}
// 通用弹出层iframe
function openFrame(title, url, width, height) {
	if (width == undefined || width == '') {
		width = "800px";
	}
	if (height == undefined || height == '') {
		height = "500px";
	}
	var index = layer.open({
		type : 2,
		title : title,
		area : [ width, height ],
		shadeClose : true,
		maxmin : true,
		content : url
	});
	// layer.full(index);
}
// 通用弹出层Div
function openDiv(title, url, width, height) {
	if (width == undefined || width == '') {
		width = "600px";
	}
	if (height == undefined || height == '') {
		height = "400px";
	}
	layer.open({
		title : title,
		type : 1,
		skin : 'layui-layer-demo', // 样式类名
		area : [ width, height ],
		shift : 2,
		maxmin : true,
		shadeClose : true, // 开启遮罩关闭
		success : function(layer) {
			layer.find(".layui-layer-content").load(url);
		}
	});
}