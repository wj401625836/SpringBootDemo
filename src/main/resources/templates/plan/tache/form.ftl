<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title> - 表单验证 jQuery Validation</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="/favicon.ico">
    <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0" rel="stylesheet">
    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">


    <#--多选下拉框-->
    <link rel="stylesheet" href="/bootstrap_select/dist/css/bootstrap-select.css">

</head>

<body class="gray-bg">
    <div class="wrapper wrapper-content animated fadeInRight">
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>功能简介</h5>
                    </div>
                    <div class="ibox-content">
                        <p>每个环节由响应的人员编辑修改,每次修改都有记录其操作内容,如此功能模块删除则下面环节以及环节下面的操作记录都删除。</p>
                        <p>实际时间是记录这个责任人执行时间,责任人变更,时间时间开始时间变为修改时的时间。</p>
                        <p>状态为【新创建>新执行中>测试中>归档完成】只能基于当前变更或者往后面变更,要想回退,找有响应权限的人</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>环节编辑</h5>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/plan/tache/edit">
                        	<input type="hidden" id="id" name="id" value="${tache.id}">
                                <div class="form-group" style="font: 15px solid black;">
                                    <label class="col-sm-3 control-label">项目名：</label>
                                    <div class="col-sm-2">
                                        <input   readonly="readonly" class="form-control" type="text" value="${tache.module.project.name}">
                                    </div>

                                    <label class="col-sm-3 control-label">功能名称：</label>
                                    <div class="col-sm-2">
                                        <input  readonly="readonly" class="form-control" type="text" value="${tache.module.name}">
                                    </div>
                                </div>
                                <div class="form-group" style="font-size: 15px;">
                                    <label class="col-sm-3 control-label">环节序号：</label>
                                    <div class="col-sm-2">
                                        <input id="tacheIndex" name="tacheIndex" readonly="readonly" class="form-control" type="text" value="${tache.tacheIndex}">
                                    </div>
                                    <label class="col-sm-3 control-label">环节名称：</label>
                                    <div class="col-sm-2">
                                        <input id="name" name="name" class="form-control" readonly="readonly" type="text" value="${tache.name}">
                                    </div>
                                </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">负责人：</label>
                                <div class="col-sm-2">
                                    <select name="user.id" class="form-control">
                                        <option value="" >----请选择负责人---</option>
                                    <#list userList as u >
                                        <option value="${u.id }" <#if u.id == (tache.user.id ) > selected="selected"</#if>>${u.nickName }</option>
                                    </#list>
                                    </select>
                                </div>
                                <label class="col-sm-3 control-label">小组成员：</label>
                                <div class="col-sm-2">

                                    <select class="selectpicker" name="groupUserIds" id = "groupUserIds" multiple data-live-search="true" data-live-search-placeholder="Search" data-actions-box="true">
                                        <#--<optgroup label="filter1">-->
                                            <#list userList as u >
                                                <option value="${u.id }" <#if userIdList?seq_contains(u.id) > selected="selected"</#if>>${u.nickName }</option>
                                            </#list>
                                        <#--</optgroup>
                                        <optgroup label="filter2">
                                            <option>option1</option>
                                            <option>option2</option>
                                            <option>option3</option>
                                            <option>option4</option>
                                        </optgroup>
                                        <optgroup label="filter3">
                                            <option>option1</option>
                                            <option>option2</option>
                                            <option>option3</option>
                                            <option>option4</option>
                                        </optgroup>-->
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label">计划时间：</label>
                                <div class="col-sm-3">
                                    <input id="planBeginTime" name="planBeginTime" readonly="readonly" required="true"  class="laydate-icon form-control layer-date" value="${(tache.planBeginTime?string('yyyy-MM-dd'))!''}">
                                </div>
                                <div class="col-sm-3">
                                    <input id="planEndTime" name="planEndTime" readonly="readonly" class="laydate-icon form-control layer-date" value="${(tache.planEndTime?string('yyyy-MM-dd'))!''}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">实际时间：</label>
                                <div class="col-sm-3">
                                    <input id="realBeginTime" name="realBeginTime" readonly="readonly" class="laydate-icon form-control layer-date" value="${(tache.realBeginTime?string('yyyy-MM-dd HH:mm:ss'))!''}">
                                </div>
                                <div class="col-sm-3">
                                    <input id="realEndTime" name="realEndTime" readonly="readonly" class="laydate-icon form-control layer-date" value="${(tache.realEndTime?string('yyyy-MM-dd HH:mm:ss'))!''}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">状态：</label>
                                <div class="col-sm-2">
                                <#-- 回退环节状态, 有权限:所有修改, 负责人权限:可以回退到上一个版本，否则: 只能往下一个版本进行   -->
                                <@shiro.hasPermission name="plan:tache:rollbackStatus"><#--有权限-->
                                    <select name="status" class="form-control">
                                        <#--
                                            <option value="0" <#if tache.status == 0>selected="selected"</#if>>新创建</option>
                                            <option value="1" <#if tache.status == 1>selected="selected"</#if>>新执行中</option>
                                            <option value="2" <#if tache.status == 2>selected="selected"</#if>>测试中</option>
                                            <option value="3" <#if tache.status == 3>selected="selected"</#if>>归档完成</option>
                                        -->
                                        <#list statusList as item ><#--系统所有状态-->
                                            <#if haveStatusListIds?seq_contains(item.id) ><#--当前环节拥有的状态 -->
                                                <option value="${item.id }"  <#if u.id == tache.status > selected="selected"</#if> >${item.name }</option>
                                            </#if>
                                        </#list>
                                    </select>
                                </@shiro.hasPermission>
                                <@shiro.lacksPermission name="plan:tache:rollbackStatus"><#--没有权限-->

                                    <select name="status" class="form-control">
                                        <#--<#if tache.status lte 0> &lt;#&ndash;小等于0&ndash;&gt;
                                            <option value="0" <#if tache.status == 0>selected="selected"</#if>>新创建</option>
                                        </#if>
                                        <#if tache.status lte 1> &lt;#&ndash;小等于1&ndash;&gt;
                                            <option value="1" <#if tache.status == 1>selected="selected"</#if>>新执行中</option>
                                        </#if>
                                        <#if tache.status lte 2> &lt;#&ndash;小等于2&ndash;&gt;
                                            <option value="2" <#if tache.status == 2>selected="selected"</#if>>测试中</option>
                                        </#if>
                                        <#if tache.status lte 3> &lt;#&ndash;小等于3&ndash;&gt;
                                            <option value="3" <#if tache.status == 3>selected="selected"</#if>>归档完成</option>
                                        </#if>-->
                                        <#-- 因为状态的顺序是由 ProjectStatus 中的 status先做判断,再以 sortIndex判断,所以现在以这个实现比较大小 -->
                                        <#list statusList as item ><#--系统所有状态-->
                                            <#if haveStatusListIds?seq_contains(item.id) ><#--当前环节拥有的状态 -->
                                                <#if (tacheStatus.status*100+tacheStatus.sortIndex ) lte (item.status * 100+ item.sortIndex )>
                                                    <option value="${item.id }"  <#if item.id == tacheStatus.id > selected="selected"</#if> >${item.name } <#--(${(tacheStatus.status * 100+ tacheStatus.sortIndex)})<= (${(item.status * 100+ item.sortIndex )}) --></option>
                                                </#if>
                                            </#if>
                                        </#list>

                                    </select>
                                </@shiro.lacksPermission>
                                </div>
                            </div>

                            <#--<div class="form-group">
                                <label class="col-sm-3 control-label">创建描述${tache.createComment}：</label>
                                <div class="col-sm-5">
                                    <input id="createComment" name="createComment" class="form-control" value="${tache.createComment}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">修改描述：</label>
                                <div class="col-sm-5">
                                    <input id="updateComment" name="updateComment" class="form-control" value="${tache.updateComment}">
                                </div>
                            </div>-->
                            <div class="form-group">
                                <label class="col-sm-3 control-label">备注：</label>
                                <div class="col-sm-5">
                                    <textarea style="height: 150px;" id="updateComment" name="updateComment" class="form-control">${tache.updateComment}</textarea>
                                </div>
                            </div>
                                <div class="form-group">
                                    <div class="col-sm-5 col-sm-offset-3">
                                        <@shiro.hasPermission name="plan:project:edit">
                                            <button class="btn btn-primary" type="submit">提交</button>
                                        </@shiro.hasPermission>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                        <button class="btn btn-primary" type="button" onclick="closeWindow();">取消</button>
                                    </div>
                                </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>


    <!-- 全局js -->
    <script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
    <script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>

    <!-- 自定义js -->
    <script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>

    <!-- jQuery Validation plugin javascript-->
    <script src="${ctx!}/assets/js/plugins/validate/jquery.validate.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/validate/messages_zh.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>
    <script src="${ctx!}/assets/js/plugins/layer/laydate/laydate.js"></script>

    <#--多选下拉框-->
    <script src="${ctx!}/bootstrap_select/dist/js/bootstrap-select.js"></script>



    <script type="text/javascript">
    $(document).ready(function () {
        //外部js调用
        laydate({
            elem: '#planBeginTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
            event: 'focus', //响应事件。如果没有传入event，则按照默认的click
            format:"YYYY-MM-DD"
        });
        laydate({
            elem: '#planEndTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
            event: 'focus' ,//响应事件。如果没有传入event，则按照默认的click
            format:"YYYY-MM-DD"
        });
//        laydate({
//            elem: '#realBeginTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
//            event: 'focus' ,//响应事件。如果没有传入event，则按照默认的click
//            format:"YYYY-MM-DD"
//        });
//        laydate({
//            elem: '#realEndTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
//            event: 'focus' ,//响应事件。如果没有传入event，则按照默认的click
//            format:"YYYY-MM-DD"
//        });
	    $("#frm").validate({
    	    rules: {
    	    	name: {
    	        required: true,
    	        minlength: 2,
    	    	maxlength: 20
    	      },
                status: {
    	        required: true
    	      },
              updateComment: {
              required: false,
              maxlength: 2000
              }
    	    },
    	    messages: {},
    	    submitHandler:function(form){
    	    	$.ajax({
   	    		   type: "POST",
   	    		   dataType: "json",
   	    		   url: "${ctx!}/plan/tache/edit",
   	    		   data: $(form).serialize(),
   	    		   success: function(msg){
	   	    			layer.msg(msg.message, {time: 2000},function(){
	   						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	   						parent.layer.close(index);
	   					});
   	    		   }
   	    		});
            }
    	});
    });
    var closeWindow = function(){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);
    }

    </script>

</body>

</html>
