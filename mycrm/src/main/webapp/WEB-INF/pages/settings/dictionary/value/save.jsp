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
		$("#save").click(function () {
			var typeCode = $("#create-dicTypeCode").val();
			var value = $("#create-dicValue").val();
			var text = $.trim($("#create-text").val());
			var orderNo = $("#create-orderNo").val();
			if (typeCode == "") {
				alert("字典类型编码不能为空");
				return;
			}
			if (value == "") {
				alert("字典值不能为空");
				return;
			}
			if (text == "") {
				alert("文本不能为空");
				return;
			}
			if (orderNo == "") {
				alert("排序号不能为空");
				return;
			}
			$.ajax({
				url: 'settings/dictionary/saveCreatedDicValue.do',
				data: {
					typeCode: typeCode,
					value: value,
					text: text,
					orderNo: orderNo,
				},
				type: 'post',
				dataType: 'json',
				success: function (data) {
					if (data.code == "1") {
						alert("保存成功")
						window.location.href="settings/dictionary/value/index.do";
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
		<h3>新增字典值</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="save">保存</button>
			<button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form">
					
		<div class="form-group">
			<label for="create-dicTypeCode" class="col-sm-2 control-label">字典类型编码<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-dicTypeCode" style="width: 200%;">
				  <option></option>
				  <c:forEach items="${requestScope.codes}" var="code">
					  <option>${code}</option>
				  </c:forEach>
				</select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-dicValue" class="col-sm-2 control-label">字典值<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-dicValue" style="width: 200%;">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-text" class="col-sm-2 control-label">文本</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-text" style="width: 200%;">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-orderNo" class="col-sm-2 control-label">排序号</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-orderNo" style="width: 200%;">
			</div>
		</div>
	</form>
	
	<div style="height: 200px;"></div>
</body>
</html>