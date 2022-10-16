<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=baseUrl%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <script type="text/javascript">

        $(function () {
            //给"阶段"下拉框添加change事件
            $("#create-stage").change(function () {
                //收集参数
                //alert($(this).find("option:selected").text());
                var id=$("#create-stage").val();
                //表单验证
                if(id==''){
                    //清空可能性输入框
                    $("#create-possibility").val("");
                    return;
                }
                //发送请求
                $.ajax({
                    url:'workbench/transaction/queryPossibility.do',
                    data:{
                        id:id,
                    },
                    type:'post',
                    dataType:'json',
                    success:function (data) {
                        //把可能性显示在输入框中
                        $("#create-possibility").val(data.text);
                    }
                });
            });

            //当容器加载完成之后，对容器调用工具函数
            $("#create-customerName").typeahead({
                source:function (jquery,process) {//每次键盘弹起，都自动触发本函数；我们可以向后台送请求，查询客户表中所有的名称，把客户名称以[]字符串形式返回前台，赋值给source
                    //发送查询请求
                    $.ajax({
                        url:'workbench/transaction/queryCustomerNameByName.do',
                        data:{
                            customerName:jquery
                        },
                        type:'post',
                        dataType:'json',
                        success:function (data) {//['xxx','xxxxx','xxxxxx',.....]
                            process(data);
                        }
                    });
                }
            });


            //给"保存"按钮添加单击事件
            $("#saveBtn").click(function () {
                //收集参数
                var owner = $("#create-owner").val();
                var money = $.trim($("#create-money").val());
                var name = $.trim($("#create-name").val());
                var expectedDate = $.trim($("#create-expectedDate").val());
                var customerId = $.trim($("#create-customerName").val());
                var stage = $.trim($("#create-stage").val());
                var type = $.trim($("#create-transactionType").val());
                var source = $.trim($("#create-source").val());
                var activityId = $.trim($("#activityId").val());
                var contactsId = $.trim($("#contactsId").val());
                var description = $.trim($("#create-describe").val());
                var contactSummary = $("#create-contactSummary").val();
                var nextContactTime = $("#create-nextContactTime").val();
                //表单验证(作业)
                //带*非空
                if (owner == '') {
                    alert("所有者不能为空")
                    return;
                }
                if (name == '') {
                    alert("名称不能为空");
                    return;
                }
                if (customerId == '') {
                    alert("客户名称不能为空");
                    return;
                }

                //正则表达式验证
                //邮箱验证
                // var emailReg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
                // var mphoneReg = /^1[3-9]\d{9}$/;
                // var phoneReg = /^(0[0-9]{2,3}\-)([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
                // if(!emailReg.test(email)) {
                // 	alert("邮箱格式错误");
                // 	return;
                // }
                // if (!mphoneReg.test(mphone)) {
                // 	alert("手机号码格式错误");
                // 	return;
                // }
                // if (!phoneReg.test(phone)) {
                //     alert("座机号码格式错误")
                //     return;
                // }
                //发送请求
                $.ajax({
                    url: 'workbench/transaction/saveCreateTran.do',
                    data: {
                        owner: owner,
                        money: money,
                        name: name,
                        expectedDate: expectedDate,
                        customerId: customerId,
                        stage: stage,
                        type: type,
                        source: source,
                        activityId: activityId,
                        contactsId: contactsId,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            //关闭模态窗口
                            alert("保存成功!");
                            //刷新线索列表，显示第一页数据，保持每页显示条数不变(作业)
                            window.location.href = "workbench/transaction/index.do";
                        } else {
                            //提示信息
                            alert(data.message);
                            //模态窗口不关闭
                        }
                    }
                });
            });
            //给"市场活动搜索图标"按钮添加单击事件
            $("#findActivity").click(function () {
                //初始化工作
                //清空搜索框
                $("#searchActivityTxt").val("");
                //清空搜索的市场活动列表
                $("#activityTBody").html("");

                //弹出"线索关联市场活动"的模态窗口
                $("#findMarketActivity").modal("show");
            });

            //给"联系人搜索图标"按钮添加单击事件
            $("#findContacts").click(function () {
                //初始化工作
                //清空搜索框
                $("#searchContactsTxt").val("");
                //清空搜索的联系人列表
                $("#contactsTBody").html("");
                //弹出"线索关联市场活动"的模态窗口
                $("#findContactsModel").modal("show");
            });

            //给市场活动搜索框添加键盘弹起事件
            $("#searchActivityTxt").keyup(function () {
                //收集参数
                var name = $("#searchActivityTxt").val();
                //发送请求
                $.ajax({
                    url: 'workbench/transaction/queryActivityByName.do',
                    data: {
                        name: name,
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        //遍历data，显示搜索到的市场活动列表
                        var htmlStr='';
                        $.each(data.retData, function (index, obj) {
                            htmlStr += "<tr>";
                            /*"<td><input type=\"radio\" value=\""+obj.id+"\" activityName=\""+obj.name+"\" name=\"activity\"/></td>";*/
                            htmlStr += "<td><input type=\"radio\" value=\""+obj.id+"\" activityName=\""+obj.name+"\" name=\"activity\"/></td>";;
                            htmlStr += "<td>" + obj.name + "</td>";
                            htmlStr += "<td>" + obj.startDate + "</td>";
                            htmlStr += "<td>" + obj.endDate + "</td>";
                            htmlStr += "<td>" + obj.owner + "</td>";
                            htmlStr += "</tr>";
                        });
                        $("#activityTBody").html(htmlStr);
                    }
                });
            });

            $("#searchContactsTxt").keyup(function () {
                //收集参数
                var name = $("#searchContactsTxt").val();
                //发送请求
                $.ajax({
                    url: 'workbench/transaction/queryContactsByName.do',
                    data: {
                        name: name,
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        //遍历data，显示搜索到的市场活动列表
                        var htmlStr='';
                        $.each(data.retData, function (index, obj) {
                            htmlStr += "<tr>";
                            /*htmlStr += "<td><input type=\"radio\" value=\"" + obj.id + "\"/></td>";*/
                            htmlStr += "<td><input type=\"radio\" value=\""+obj.id+"\" contactsName=\""+obj.fullname+"\" name=\"contacts\"/></td>";;
                            htmlStr += "<td>" + obj.fullname + "</td>";
                            htmlStr += "<td>" + obj.email + "</td>";
                            htmlStr += "<td>" + obj.mphone + "</td>";
                            htmlStr += "</tr>";
                        });
                        $("#contactsTBody").html(htmlStr);
                    }
                });
            });

            //给所有市场活动的单选按钮添加单击事件
            $("#activityTBody").on("click","input[type='radio']",function () {
                //获取市场活动的id和name
                var id=this.value;
                var activityName=$(this).attr("activityName");
                //把市场活动的id写到隐藏域，把name写到输入框中
                $("#activityId").val(id);
                $("#create-activity").val(activityName);
                //关闭搜索市场活动的模态窗口
                $("#findMarketActivity").modal("hide");
            });

            $("#contactsTBody").on("click","input[type='radio']",function () {
                //获取市场活动的id和name
                var id=this.value;
                var contactsName=$(this).attr("contactsName");
                //把市场活动的id写到隐藏域，把name写到输入框中
                $("#contactsId").val(id);
                $("#create-contactsName").val(contactsName);
                //关闭搜索市场活动的模态窗口
                $("#findContactsModel").modal("hide");
            });
        });
    </script>
</head>
<body>

<!-- 查找市场活动 -->
<div class="modal fade" id="findMarketActivity" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找市场活动</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" id="searchActivityTxt" class="form-control" style="width: 300px;"
                                   placeholder="请输入市场活动名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable3" class="table table-hover"
                       style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>开始日期</td>
                        <td>结束日期</td>
                        <td>所有者</td>
                    </tr>
                    </thead>
                    <tbody id="activityTBody">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 查找联系人 -->
<div class="modal fade" id="findContactsModel" role="dialog">
    <div class="modal-dialog" role="document" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">查找联系人</h4>
            </div>
            <div class="modal-body">
                <div class="btn-group" style="position: relative; top: 18%; left: 8px;">
                    <form class="form-inline" role="form">
                        <div class="form-group has-feedback">
                            <input type="text" id="searchContactsTxt" class="form-control" style="width: 300px;"
                                   placeholder="请输入联系人名称，支持模糊查询">
                            <span class="glyphicon glyphicon-search form-control-feedback"></span>
                        </div>
                    </form>
                </div>
                <table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
                    <thead>
                    <tr style="color: #B3B3B3;">
                        <td></td>
                        <td>名称</td>
                        <td>邮箱</td>
                        <td>手机</td>
                    </tr>
                    </thead>
                    <tbody id="contactsTBody">

                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>


<div style="position:  relative; left: 30px;">
    <h3>创建交易</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" class="btn btn-primary" id="saveBtn">保存</button>
        <button type="button" class="btn btn-default" onclick="window.location.href='workbench/transaction/index.do'">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
    <div class="form-group">
        <label for="create-owner" class="col-sm-2 control-label">所有者<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-owner">
                <option></option>
                <c:forEach items="${users}" var="user">
                    <option value="${user.id}">${user.name}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-money" class="col-sm-2 control-label">金额</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-money">
        </div>
    </div>

    <div class="form-group">
        <label for="create-name" class="col-sm-2 control-label">名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-name">
        </div>
        <label for="create-expectedDate" class="col-sm-2 control-label">预计成交日期<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-expectedDate">
        </div>
    </div>

    <div class="form-group">
        <label for="create-customerName" class="col-sm-2 control-label">客户名称<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-customerName"
                   placeholder="支持自动补全，输入客户不存在则新建">
        </div>
        <label for="create-stage" class="col-sm-2 control-label">阶段<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-stage">
                <option></option>
                <c:forEach items="${stageList}" var="stage">
                    <option value="${stage.id}">${stage.value}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="create-transactionType" class="col-sm-2 control-label">类型</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-transactionType">
                <option></option>
                <c:forEach items="${transactionTypeList}" var="type">
                    <option value="${type.id}">${type.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-possibility" class="col-sm-2 control-label">可能性</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-possibility">
        </div>
    </div>

    <div class="form-group">
        <label for="create-source" class="col-sm-2 control-label">来源</label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-source">
                <option></option>
                <c:forEach items="${sourceList}" var="source">
                    <option value="${source.id}">${source.value}</option>
                </c:forEach>
            </select>
        </div>
        <label for="create-activity" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a
                href="javascript:void(0);" id="findActivity"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="hidden" id="activityId">
            <input type="text" class="form-control" id="create-activity">
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a
                href="javascript:void(0);" id="findContacts"><span
                class="glyphicon glyphicon-search"></span></a></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="hidden" id="contactsId">
            <input type="text" class="form-control" id="create-contactsName">
        </div>
    </div>

    <div class="form-group">
        <label for="create-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-describe"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
        <div class="col-sm-10" style="width: 70%;">
            <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-nextContactTime">
        </div>
    </div>

</form>
</body>
</html>