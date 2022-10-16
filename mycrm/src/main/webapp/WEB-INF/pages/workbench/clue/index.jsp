<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">

    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css"
          href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.min.js"></script>

    <script type="text/javascript">
        $(function () {
            //给"创建"按钮添加单击事件
            $("#createClueBtn").click(function () {
                //初始化工作
                $("#createClueForm")[0].reset();
                //弹出模态窗口
                $("#createClueModal").modal("show");

            });


            //给"保存"按钮添加单击事件
            $("#saveCreateClueBtn").click(function () {
                //收集参数
                var fullname = $.trim($("#create-fullname").val());
                var appellation = $("#create-appellation").val();
                var owner = $("#create-owner").val();
                var company = $.trim($("#create-company").val());
                var job = $.trim($("#create-job").val());
                var email = $.trim($("#create-email").val());
                var phone = $.trim($("#create-phone").val());
                var website = $.trim($("#create-website").val());
                var mphone = $.trim($("#create-mphone").val());
                var state = $("#create-state").val();
                var source = $("#create-source").val();
                var description = $.trim($("#create-description").val());
                var contactSummary = $.trim($("#create-contactSummary").val());
                var nextContactTime = $.trim($("#create-nextContactTime").val());
                var address = $.trim($("#create-address").val());
                //表单验证(作业)
                //带*非空
                if(owner=='') {
                    alert("所有者不能为空")
                    return;
                }
                if(company=='') {
                    alert("公司不能为空");
                    return;
                }
                if(fullname=='') {
                    alert("姓名不能为空");
                    return;
                }
                //正则表达式验证
                //邮箱验证
                var emailReg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
                var mphoneReg = /^1[3-9]\d{9}$/;
                var phoneReg =  /^(0[0-9]{2,3}\-)([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
                if(!emailReg.test(email)) {
                    alert("邮箱格式错误");
                    return;
                }
                if(!mphoneReg.test(mphone)) {
                    alert("手机号码格式错误");
                    return;
                }
                if(!phoneReg.test(phone)) {
                    alert("座机号码格式错误")
                    return;
                }
                //发送请求
                $.ajax({
                    url: 'workbench/clue/saveCreateClue.do',
                    data: {
                        fullname: fullname,
                        appellation: appellation,
                        owner: owner,
                        company: company,
                        job: job,
                        email: email,
                        phone: phone,
                        website: website,
                        mphone: mphone,
                        state: state,
                        source: source,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                        address: address,
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            alert("保存成功!");
                            //关闭模态窗口
                            $("#createClueModal").modal("hide");
                            //刷新线索列表，显示第一页数据，保持每页显示条数不变(作业)
                            queryClueByConditionForPage(1, 10);
                        } else {
                            //提示信息
                            alert(data.message);
                            //模态窗口不关闭
                            $("#createClueModal").modal("show");
                        }
                    }
                });
            });

            $(".mydate").datetimepicker({
                language: 'en', //语言
                format: 'yyyy-mm-dd',//日期的格式
                minView: 'month', //可以选择的最小视图
                initialDate: new Date(),//初始化显示的日期
                autoClose: true,//设置选择完日期或者时间之后，是否自动关闭日历
                todayBtn: true,//设置是否显示"今天"按钮,默认是false
                clearBtn: true,//设置是否显示"清空"按钮，默认是false

            });

            queryClueByConditionForPage(1, 10);

            //给"查询"按钮添加单击事件
            $("#queryClueBtn").click(function () {
                //查询所有符合条件数据的第一页以及所有符合条件数据的总条数;
                queryClueByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
            });

            //给"全选"按钮添加单击事件
            $("#chckAll").click(function () {
                $("#tBody input[type='checkbox']").prop("checked", this.checked);
            });

            $("#tBody").on("click", "input[type='checkbox']", function () {
                //如果列表中的所有checkbox都选中，则"全选"按钮也选中
                if ($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()) {
                    $("#chckAll").prop("checked", true);
                } else {//如果列表中的所有checkbox至少有一个没选中，则"全选"按钮也取消
                    $("#chckAll").prop("checked", false);
                }
            });

            //给删除按钮添加单击事件
            $("#deleteBtn").click(function () {
                //收集参数
                //获取列表中所有被选中的checkbox
                var chekkedIds = $("#tBody input[type='checkbox']:checked");
                if (chekkedIds.size() == 0) {
                    alert("请选择要删除的线索");
                    return;
                }

                if (window.confirm("确定删除吗？")) {
                    var ids = '';
                    $.each(chekkedIds, function () {//id=xxxx&id=xxx&.....&id=xxx&
                        ids += "id=" + this.value + "&";
                    });
                    ids = ids.substr(0, ids.length - 1);//id=xxxx&id=xxx&.....&id=xxx id='xxx' or id=
                    //发送请求
                    $.ajax({
                        url: 'workbench/clue/deleteClue.do',
                        data:ids,
                        type: 'post',
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == "1") {
                                //刷新市场活动列表,显示第一页数据,保持每页显示条数不变
                                queryClueByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                                alert("删除成功");
                            } else {
                                //提示信息
                                alert(data.message);
                            }
                        }
                    });
                }
            });


            //给"修改"按钮添加单击事件
            $("#editClueBtn").click(function () {
                //收集参数
                //获取列表中被选中的checkbox
                var chkedIds = $("#tBody input[type='checkbox']:checked");
                if (chkedIds.size() == 0) {
                    alert("请选择要修改的线索");
                    return;
                }
                if (chkedIds.size() > 1) {
                    alert("每次只能修改一条线索");
                    return;
                }
                // $("#editClueModal").modal("show");
                var id = chkedIds[0].value;
                //发送请求
                $.ajax({
                    url: 'workbench/clue/queryClueById.do',
                    data: {
                        id: id
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        //把线索的信息显示在修改的模态窗口上
                        $("#edit-clueId").val(data.id);
                        $.trim($("#edit-surname").val(data.fullname));
                        $("#edit-call").val(data.appellation);
                        $("#edit-clueOwner").val(data.owner);
                        $.trim($("#edit-company").val(data.company));
                        $.trim($("#edit-job").val(data.job));
                        $.trim($("#edit-email").val(data.email));
                        $.trim($("#edit-phone").val(data.phone));
                        $.trim($("#edit-website").val(data.website));
                        $.trim($("#edit-mphone").val(data.mphone));
                        $("#edie-state").val(data.state);
                        $("#edit-source").val(data.source);
                        $.trim($("#edit-describe").val(data.description));
                        $.trim($("#edit-contactSummary").val(data.contactSummary));
                        $.trim($("#edit-nextContactTime").val(data.nextContactTime));
                        $.trim($("#edit-address").val(data.address));
                        //弹出模态窗口
                        $("#editClueModal").modal("show");
                    }
                });
            });

            //给"更新"按钮添加单击事件
            $("#updateClueBtn").click(function () {
                //收集参数
                var id = $("#edit-clueId").val();
                var fullname = $.trim($("#edit-surname").val());
                var appellation = $("#edit-call").val();
                var owner = $("#edit-clueOwner").val();
                var company = $.trim($("#edit-company").val());
                var job = $.trim($("#edit-job").val());
                var email = $.trim($("#edit-email").val());
                var phone = $.trim($("#edit-phone").val());
                var website = $.trim($("#edit-website").val());
                var mphone = $.trim($("#edit-mphone").val());
                var state = $("#edit-state").val();
                var source = $("#edit-source").val();
                var description = $.trim($("#edit-describe").val());
                var contactSummary = $.trim($("#edit-contactSummary").val());
                var nextContactTime = $.trim($("#edit-nextContactTime").val());
                var address = $.trim($("#edit-address").val());
                //表单验证(作业)
                //发送请求
                $.ajax({
                    url: 'workbench/clue/saveEditClue.do',
                    data: {
                        id:id,
                        fullname: fullname,
                        appellation: appellation,
                        owner: owner,
                        company: company,
                        job: job,
                        email: email,
                        phone: phone,
                        website: website,
                        mphone: mphone,
                        state: state,
                        source: source,
                        description: description,
                        contactSummary: contactSummary,
                        nextContactTime: nextContactTime,
                        address: address,
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        if (data.code == "1") {
                            alert("修改成功!");
                            //关闭模态窗口
                            $("#editClueModal").modal("hide");
                            //刷新市场活动列表,保持页号和每页显示条数都不变
                            queryClueByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'), $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
                        } else {
                            //提示信息
                            alert(data.message);
                            //模态窗口不关闭
                            $("#editClueModal").modal("show");
                        }
                    }
                });
            });
        });

        function queryClueByConditionForPage(pageNo, pageSize) {
            //收集参数
            var fullname = $("#clueFullname").val();
            var owner = $("#clueOwner").val();
            var company = $("#clueCompany").val();
            var phone = $("#cluePhone").val();
            var mphone = $("#clueMphone").val();
            var state = $("#clueState").val();
            var source = $("#clueSource").val();
            //发送请求
            $.ajax({
                url: 'workbench/clue/queryClueByConditionForPage.do',
                data: {
                    fullname: fullname,
                    owner: owner,
                    company: company,
                    phone: phone,
                    mphone: mphone,
                    state: state,
                    source: source,
                    pageNo: pageNo,
                    pageSize: pageSize,
                },
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    //显示总条数
                    $("#totalRowsB").text(data.total);
                    //显示市场活动的列表
                    //遍历clueList，拼接所有行数据
                    var htmlStr = "";
                    $.each(data.list, function (index, obj) {
                        htmlStr += "<tr class=\"active\">";
                        htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id + "\"/></td>";
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/clue/detail.do/" + obj.id + "'\">" + obj.fullname + "</a></td>";
                        htmlStr += "<td>" + obj.company + "</td>";
                        htmlStr += "<td>" + obj.phone + "</td>";
                        htmlStr += "<td>" + obj.mphone + "</td>";
                        htmlStr += "<td>" + obj.source + "</td>";
                        htmlStr += "<td>" + obj.owner + "</td>";
                        htmlStr += "<td>" + obj.state + "</td>";
                        htmlStr += "</tr>";
                    });
                    $("#tBody").html(htmlStr);

                    //取消"全选"按钮
                    $("#chckAll").prop("checked", false);


                    //计算总页数
                    var totalPages = 1;
                    if (data.total % pageSize == 0) {
                        totalPages = data.total / pageSize;
                    } else {
                        totalPages = parseInt(data.total / pageSize) + 1;
                    }

                    //对容器调用bs_pagination工具函数，显示翻页信息
                    $("#demo_pag1").bs_pagination({
                        currentPage: pageNo,//当前页号,相当于pageNo
                        rowsPerPage: pageSize,//每页显示条数,相当于pageSize
                        totalRows: data.total,//总条数
                        totalPages: totalPages,  //总页数,必填参数.
                        visiblePageLinks: 5,//最多可以显示的卡片数
                        showGoToPage: true,//是否显示"跳转到"部分,默认true--显示
                        showRowsPerPage: true,//是否显示"每页显示条数"部分。默认true--显示
                        showRowsInfo: true,//是否显示记录的信息，默认true--显示

                        //用户每次切换页号，都自动触发本函数;
                        //每次返回切换页号之后的pageNo和pageSize
                        onChangePage: function (event, pageObj) { // returns page_num and rows_per_page after a link has clicked
                            //js代码
                            //alert(pageObj.currentPage);
                            //alert(pageObj.rowsPerPage);
                            queryClueByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
                        }
                    });
                }
            });
        }

    </script>
</head>
<body>

<!-- 创建线索的模态窗口 -->
<div class="modal fade" id="createClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">创建线索</h4>
            </div>
            <div class="modal-body">
                <form id="createClueForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-owner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-owner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-company">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-appellation" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-appellation">
                                <option></option>
                                <c:forEach items="${appellationList}" var="app">
                                    <option value="${app.id}">${app.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-fullname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-fullname">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-job">
                        </div>
                        <label for="create-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-email">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-phone">
                        </div>
                        <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-website">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-mphone">
                        </div>
                        <label for="create-state" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-state">
                                <option></option>
                                <c:forEach items="${clueStateList}" var="cs">
                                    <option value="${cs.id}">${cs.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-source">
                                <option></option>
                                <c:forEach items="${sourceList}" var="sl">
                                    <option value="${sl.id}">${sl.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">线索描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <%--<input type="text" class="form-control mydate" name="mydate" id="create-startDate" readonly>--%>
                            <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control mydate" name="mydate" id="create-nextContactTime" readonly>
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1" id="create-address"></textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveCreateClueBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改线索的模态窗口 -->
<div class="modal fade" id="editClueModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 90%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title">修改线索</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-clueId">
                    <div class="form-group">
                        <label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-clueOwner">
                                <c:forEach items="${userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-company" class="col-sm-2 control-label">公司<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-company" value="动力节点">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-call" class="col-sm-2 control-label">称呼</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-call">
                                <option></option>
                                <c:forEach items="${appellationList}" var="app">
                                    <option value="${app.id}">${app.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-surname" class="col-sm-2 control-label">姓名<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-surname" value="李四">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-job" class="col-sm-2 control-label">职位</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-job" value="CTO">
                        </div>
                        <label for="edit-email" class="col-sm-2 control-label">邮箱</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-phone" value="010-84846003">
                        </div>
                        <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-website"
                                   value="http://www.bjpowernode.com">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-mphone" class="col-sm-2 control-label">手机</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-mphone" value="12345678901">
                        </div>
                        <label for="edit-state" class="col-sm-2 control-label">线索状态</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-state">
                                <option></option>
                                <c:forEach items="${clueStateList}" var="cs">
                                    <option value="${cs.id}">${cs.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-source" class="col-sm-2 control-label">线索来源</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-source">
                                <option></option>
                                <c:forEach items="${sourceList}" var="sl">
                                    <option value="${sl.id}">${sl.value}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-describe">这是一条线索的描述信息</textarea>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                    <div style="position: relative;top: 15px;">
                        <div class="form-group">
                            <label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3"
                                          id="edit-contactSummary">这个线索即将被转换</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control mydate" name="mydate" id="edit-nextContactTime" value="2017-05-01" readonly>
                            </div>
                        </div>
                    </div>

                    <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                    <div style="position: relative;top: 20px;">
                        <div class="form-group">
                            <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="1"
                                          id="edit-address">北京大兴区大族企业湾</textarea>
                            </div>
                        </div>
                    </div>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateClueBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>线索列表</h3>
        </div>
    </div>
</div>

<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">

    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="clueFullname">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司</div>
                        <input class="form-control" type="text" id="clueCompany">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">公司座机</div>
                        <input class="form-control" type="text" id="cluePhone">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索来源</div>
                        <select class="form-control" id="clueSource">
                            <option></option>
                            <c:forEach items="${sourceList}" var="sl">
                                <option value="${sl.id}">${sl.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <br>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <%--<input class="form-control" type="text" id="clueOwner">--%>
                        <select class="form-control" id="clueOwner">
                            <option value=""></option>
                            <c:forEach items="${userList}" var="u">
                                <option value="${u.id}">${u.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">手机</div>
                        <input class="form-control" type="text" id="clueMphone">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">线索状态</div>
                        <select class="form-control" id="clueState">
                            <option></option>
                            <c:forEach items="${clueStateList}" var="cs">
                                <option value="${cs.id}">${cs.value}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryClueBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary" id="createClueBtn"><span
                        class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default" data-toggle="modal" id="editClueBtn"><span
                        class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteBtn"><span
                        class="glyphicon glyphicon-minus"></span> 删除
                </button>
            </div>


        </div>
        <div style="position: relative;top: 50px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="chckAll"/></td>
                    <td>名称</td>
                    <td>公司</td>
                    <td>公司座机</td>
                    <td>手机</td>
                    <td>线索来源</td>
                    <td>所有者</td>
                    <td>线索状态</td>
                </tr>
                </thead>
                <tbody id="tBody">
                <%--<tr>
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/clue/detailClue.do?id=a67c52e8702e4a4e89ab47d1751fc78b';">张三教授</a></td>
                    <td>动力节点</td>
                    <td>010-84846003</td>
                    <td>12345678901</td>
                    <td>广告</td>
                    <td>zhangsan</td>
                    <td>已联系</td>
                </tr>
                <tr class="active">
                    <td><input type="checkbox" /></td>
                    <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">李四先生</a></td>
                    <td>动力节点</td>
                    <td>010-84846003</td>
                    <td>12345678901</td>
                    <td>广告</td>
                    <td>zhangsan</td>
                    <td>已联系</td>
                </tr>--%>
                </tbody>
            </table>
            <div id="demo_pag1"></div>
        </div>

<%--        <div style="height: 50px; position: relative;top: 60px;">--%>
<%--            <div>--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>--%>
<%--            </div>--%>
<%--            <div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
<%--                <div class="btn-group">--%>
<%--                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--                        10--%>
<%--                        <span class="caret"></span>--%>
<%--                    </button>--%>
<%--                    <ul class="dropdown-menu" role="menu">--%>
<%--                        <li><a href="#">20</a></li>--%>
<%--                        <li><a href="#">30</a></li>--%>
<%--                    </ul>--%>
<%--                </div>--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
<%--            </div>--%>

            <%--<div style="position: relative;top: -88px; left: 285px;">
                <nav>
                    <ul class="pagination">
                        <li class="disabled"><a href="#">首页</a></li>
                        <li class="disabled"><a href="#">上一页</a></li>
                        <li class="active"><a href="#">1</a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a href="#">5</a></li>
                        <li><a href="#">下一页</a></li>
                        <li class="disabled"><a href="#">末页</a></li>
                    </ul>
                </nav>
            </div>--%>
        </div>

    </div>

</div>
</body>
</html>