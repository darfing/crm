<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>
<script type="text/javascript">
	$(function () {
		//给保存按钮添加单击事件
		$("#update").click(function () {
			var code = $("#code").val();
			var name = $.trim($("#name").val());
			var description = $("#describe").val();
			if (code == "") {
				alert("code不能为空");
				return;
			}
			if (name == "") {
				alert("名称不能为空");
				return;
			}
			if (description == "") {
				alert("描述不能为空");
				return;
			}
			$.ajax({
				url: 'settings/dictionary/saveEditedDictionaryType.do',
				data: {
					code: code,
					name: name,
					description: description,
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == "1") {
						alert("修改成功!")
						window.location.href="settings/dictionary/index.do";
					} else {
						//提示信息
						alert(data.message);
					}
				}
			})
		})
	})
</script>
<body>

	<div style="position:  relative; left: 30px;">
		<h3>修改字典类型</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="update">更新</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="code" class="col-sm-2 control-label">编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="code" style="width: 200%;" value="${requestScope.editType.code}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="name" class="col-sm-2 control-label">名称</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="name" style="width: 200%;" value="${requestScope.editType.name}">
			</div>
		</div>
		
		<div class="form-group">
			<label for="describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 300px;">
				<textarea class="form-control" rows="3" id="describe" style="width: 200%;">${requestScope.editType.description}</textarea>
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>