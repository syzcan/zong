/**
 * @desc 封装easyui基于datagrid通用的弹出层增删改等操作，通过绑定按钮实现
 *       【上传控件依赖webuploder和layer，富文本umeditor】
 * @author zong
 * @date 2018年2月10日
 */
(function($) {
	// 新增数据
	function add(options) {
		var defaults = {
			title : '新增'
		};
		options = $.extend(defaults, options);
		initDialogForm(options);
	}
	// 编辑选中数据
	function edit(options) {
		var defaults = {
			title : '编辑'
		};
		options = $.extend(defaults, options);
		var rows = $(options.datagrid).datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条数据', 'warning');
			return;
		}
		if (options.selectRowCheck) {
			if (!options.selectRowCheck(rows[0])) {
				return;
			}
			;
		}
		options.row = rows[0];
		if (options.href) {
			// 拼接按主键查询参数
			options.href += '?' + options.pk + '=' + rows[0][options.pk];
		}
		initDialogForm(options);
	}
	// 表单弹出并初始化
	function initDialogForm(options) {
		// 弹出表单
		$(options.dialog).dialog({
			title : options.title,
			width : options.width,
			height : options.height,
			href : options.href,
			cache : false,
			modal : true,
			collapsible : options.collapsible,
			minimizable : options.minimizable,
			maximizable : options.maximizable,
			resizable : options.resizable,
			onLoad : function() {
				// 自定义方法调用onLoadHead
				if (options.onLoadHead) {
					options.onLoadHead();
				}
				// 如果有详情json链接，加载进表单
				if (options.dataurl) {
					var arr = options.href.split('?');
					options.dataurl += arr.length > 1 ? ('?' + arr[1]) : '';
					$.ajax({
						url : options.dataurl,
						dataType : "json",
						async : false,
						cache : false,
						success : function(result) {
							$(options.form).form('load', result.data);
							// 标签data-field赋值text
							$(options.form).find('*[data-field]').each(function() {
								$(this).text(result.data[$(this).attr('data-field')]);
							});
							$(options.form).find('*[data-src]').each(function() {
								var src = result.data[$(this).attr('data-src')];
								if (src) {
									$(this).attr('src', src.indexOf('http') == 0 ? src : ctx + '/' + src);
								}
							});
							// 自定义方法调用onLoadData
							if (options.onLoadData) {
								options.onLoadData(result.data);
							}
						}
					});
				}
				initFormOnLoadOrOpen(options);
			},
			onOpen : function() {
				// 自定义方法调用onOpenHead
				if (options.onOpenHead) {
					options.onOpenHead(options.row);
				}
				initFormOnLoadOrOpen(options)
			},
			onClose : function() {
				// 销毁所有的ueditor
				$(options.form).find('textarea.ueditor').each(function() {
					UM.getEditor($(this).prop('id')).destroy();
				});
			}
		});
	}
	// 加载完表单后初始化表单内部
	function initFormOnLoadOrOpen(options) {
		// 表单初始化
		$(options.form).form({
			url : options.posturl,
			onSubmit : function() {
				if (options.onSubmit) {
					if (!options.onSubmit()) {
						return false;
					}
				}
				return $(options.form).form('validate');
			},
			success : function(result) {
				result(options, eval('(' + result + ')'));
			}
		});
		// 渲染umeditor
		$(options.form).find('textarea.ueditor').each(function() {
			var id = 'editor_' + new Date().getTime();
			$(this).prop('id', id);
			UM.getEditor(id);
		});
		// 自定义的上传控件渲染
		$(options.form).find('.zuploadbox').each(function() {
			// 解析参数
			var ops = '{' + $.trim($(this).data('options')) + '}';
			ops = (new Function("return " + ops))();
			$(this).zuploadbox(ops);
		});
	}

	// 删除选中数据
	function del(options) {
		var rows = $(options.datagrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择要操作的数据', 'warning');
			return;
		}
		if (options.selectRowCheck) {
			if (!options.selectRowCheck(rows)) {
				return;
			}
		}
		var data = {};
		var id = '';
		$.each(rows, function(i, n) {
			id += n[options.pk] + (i < rows.length - 1 ? ',' : '');
		});
		data[options.pk] = id;
		$.messager.confirm('提示', '确定删除数据吗', function(r) {
			if (r) {
				$.ajax({
					url : options.posturl,
					type : 'post',
					dataType : 'json',
					data : data,
					success : function(result) {
						result(options, result);
					}
				});
			}
		});
	}
	// 导出选中数据
	function exports(options) {
		var rows = $(options.datagrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择要操作的数据', 'warning');
			return;
		}
		var id = '';
		$.each(rows, function(i, n) {
			id += n[options.pk] + (i < rows.length - 1 ? ',' : '');
		});
		// 通过隐藏表单提交
		var $exportForm = $('#exportForm');
		if ($exportForm.length == 0) {
			$exportForm = $('<form id="exportForm" method="post"><input type="hidden" name="id"></form>').hide();
			$('body').append($exportForm);
		}
		$exportForm.find('input[name="id"]').val(id);
		$exportForm.prop('action', options.posturl).submit();
	}
	// 搜索查询列表
	function search(target, options) {
		options.gridtype == 'treegrid' ? $(options.datagrid).treegrid('load', serialize($(target).parents('form'))) : $(options.datagrid).datagrid(
				'load', serialize($(target).parents('form')));
	}
	// 上传文件，使用webuploader封装
	function upload(target, options) {
		var id = new Date().getTime();
		var $div = $('<div></div>').attr('id', id).text(options.buttonText).height(options.height).css('line-height', options.height + 'px').css(
				'float', 'left');
		var $text = $(
				'<input type="text" ' + (options.editable ? '' : 'readonly') + ' style="float:left;padding:0 4px;height:' + (options.height - 2)
						+ 'px;border:1px solid ' + options.background + '" />').attr('name', $(target).attr('name')).val($(target).val());
		$(target).after($div).hide().attr('uploadboxname', $(target).attr('name')).removeAttr('name');
		$div.wrap('<div id="wrap_' + id + '"></div>').before($text);
		var uploadOptions = {
			auto : true,
			swf : ctx + '/plugins/webuploader/Uploader.swf',
			server : ctx + '/common/uploadFile.json?uploadType=' + options.uploadType,
			pick : {
				id : '#' + id,
				multiple : options.multiple
			}
		};
		if (options.uploadType == 'picture') {
			$.extend(uploadOptions, {
				accept : {
					title : 'Images',
					extensions : 'gif,jpg,jpeg,bmp,png',
					// 直接用image/*在chrome下出现迟钝
					mimeTypes : 'image/gif,image/jpg,image/jpeg,image/bmp,image/png'
				}
			});
		} else if (options.uploadType == 'music') {
			$.extend(uploadOptions, {
				accept : {
					title : 'Musics',
					extensions : 'mp3,wma,wav',
					mimeTypes : 'audio/mpeg,audio/x-wav'
				}
			});
		} else if (options.uploadType == 'video') {
			$.extend(uploadOptions, {
				accept : {
					title : 'Videos',
					extensions : '3gp,mp4,rmvb,mov,avi,m4v,flv',
					mimeTypes : 'video/*'
				}
			});
		} else if (options.uploadType == 'document') {
		}
		uploader = WebUploader.create(uploadOptions);
		// 创建控件后设置颜色
		$div.find('.webuploader-pick').css('background', options.background);
		$text.width(options.width - $('#' + id).width());
		// 上传按钮隐藏控件，触发按钮点击事件
		if (options.type == 'uploadbt') {
			$('#wrap_' + id).hide();
			$(target).show().click(function() {
				$('#wrap_' + id).find('.webuploader-element-invisible').click();
			});
		}
		// 上传开始弹出加载层
		uploader.on('uploadStart', function(file) {
			layer.load(1);
		});
		// 文件上传过程中创建进度条实时显示。
		uploader.on('uploadProgress', function(file, percentage) {
			var progress = '<div style="padding-top: 40px;text-align:center">' + parseInt(percentage * 100) + '%</div>';
			$('.layui-layer[type="loading"] .layui-layer-content').html(progress);
		});
		// 文件上传成功
		uploader.on('uploadSuccess', function(file, result) {
			layer.closeAll();
			$(target).val(result.filePath);
			$text.val(result.filePath);
			// success回调方法
			options.success(result);
		});
		uploader.on('uploadError', function(file, reason) {
			layer.closeAll();
			layer.msg('上传失败:' + reason);
		});
	}
	// 从上级打开tab
	function tab(options) {
		var rows = $(options.datagrid).datagrid('getSelections');
		if (rows.length != 1) {
			$.messager.alert('提示', '请选择一条数据', 'warning');
			return;
		}
		// 拼接按主键查询参数
		url = options.href + '?' + options.pk + '=' + rows[0][options.pk];
		title = options.title;
		// 匹配正则表达式替换占位符，占位符为row其中属性
		var reg = /{(.*?)}/g;
		if (title.match(reg)) {
			var keys = [];
			while ((str = reg.exec(title)) != null) {
				keys.push(str[1]);
			}
			$.each(keys, function(i, n) {
				title = title.replace('{' + n + '}', rows[0][n]);
			});
		}
		parent.addTab({
			title : title,
			url : url,
			iconCls : options.iconCls
		});
	}

	// 请求响应信息提示
	function result(options, result, fn) {
		if (result.retCode == 200) {
			$.messager.show({
				title : '提示',
				msg : '操作成功',
				timeout : 2000,
				showType : 'slide'
			});
			if ($(options.dialog).dialog().length > 0) {
				$(options.dialog).dialog('close');
			}
			// 刷新
			if ($(options.datagrid).length > 0) {
				options.gridtype == 'treegrid' ? $(options.datagrid).treegrid('reload') : $(options.datagrid).datagrid('reload');
			}
		} else {
			$.messager.alert('提示', result.retMsg, 'warning');
		}
	}

	// 表单序列化，返回对象{}
	function serialize($form) {
		var array = $form.serializeArray();
		var data = {};
		$.each(array, function(i, field) {
			if (data[field.name]) {
				if ($.isArray(data[field.name])) {
					data[field.name].push(field.value);
				} else {
					data[field.name] = [ data[field.name], field.value ];
				}
			} else {
				data[field.name] = field.value;
			}
		});
		return data;
	}

	$.fn.zbutton = function(options) {
		var defaults = {
			operate : '',// add/edit/del/search/ajax
			dialog : '#dialog',
			datagrid : '#dg',
			datatype : '#datagrid',// datagrid/treegrid
			form : '#form',
			// title : '新增/编辑',
			width : 600,
			height : 400,
			pk : 'id',
			collapsible : false,
			minimizable : false,
			maximizable : false,
			resizable : true
		};
		options = $.extend(defaults, options);
		this.click(function() {
			if (options.operate == 'add') {
				add(options);
			} else if (options.operate == 'edit') {
				edit(options);
			} else if (options.operate == 'del') {
				del(options);
			} else if (options.operate == 'search') {
				search(this, options);
			} else if (options.operate == 'export') {
				exports(options);
			} else if (options.operate == 'tab') {
				tab(options);
			} else if (options.operate == 'ajax') {
				initDialogForm(options);
			} else {
				$.messager.alert('提示', '没有设置operate', 'warning');
			}
		});
		return this;
	}

	$.fn.zdatagrid = function(options) {
		var defaults = {
			method : 'get',
			pagination : true,
			pageSize : 10,
			rownumbers : true,
			fit : true,
			striped : true,
			border : false,
			singleSelect : true
		}
		options = $.extend(defaults, options);
		return this.datagrid(options);
	}
	$.fn.ztreegrid = function(options) {
		var defaults = {
			idField : 'id',
			treeField : 'name',
			method : 'get',
			rownumbers : true,
			fit : true,
			striped : true,
			border : false,
			singleSelect : true
		}
		options = $.extend(defaults, options);
		return this.treegrid(options);
	}

	// 上传控件，text等文本输入框使用
	$.fn.zuploadbox = function(options) {
		var defaults = {
			type : 'uploadbox',
			buttonText : '选择文件',
			uploadType : 'file',// file/picture/music/video/document
			width : 200,// 包括按钮和text
			height : 30,
			background : '#00b7ee',
			color : '#fff',
			editable : true,
			multiple : false,
			success : function(data) {

			}
		};
		options = $.extend(defaults, options);
		upload(this, options);
		return this;
	}

	// 扩展easyui校验，服务端验证重名等，服务端只返回true/false
	$.extend($.fn.validatebox.defaults.rules, {
		check : {
			validator : function(value, param) {
				var data = {};
				data[param[1]] = value;
				var result = $.ajax({
					url : param[0],
					dataType : "json",
					data : data,
					async : false,
					cache : false,
					type : "post"
				}).responseText;
				return result == "true";
			},
			message : '{2}'
		}
	});

	// 判断两个输入框不一致，如确认密码
	$.extend($.fn.validatebox.defaults.rules, {
		equals : {
			validator : function(value, param) {
				return value == $(param[0]).val();
			},
			message : '{1}'
		}
	});
	// 上传按钮
	$(function() {
		$('.zuploadbt').each(function() {
			// 解析参数
			var ops = '{' + $.trim($(this).data('options')) + '}';
			ops = (new Function("return " + ops))();
			ops['type'] = 'uploadbt';
			$(this).zuploadbox(ops);
		});
	});
}(jQuery));