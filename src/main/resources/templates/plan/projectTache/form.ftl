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

    <script src="${ctx!}/css/plan.css"></script>
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
                        <p>项目环节为全局环节,后续添加功能所具有的环节都从这里选,新添加的功能中环节由此继承</p>

                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="ibox float-e-margins">
                    <div class="ibox-title">
                        <h5>功能编辑</h5>
                    </div>
                    <div class="ibox-content">
                        <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/plan/module/edit">
                        	<input type="hidden" id="id" name="id" value="${projectTache.id}">

                            <div class="form-group">
                                <label class="col-sm-3 control-label" >项目环节名称：</label>
                                <div class="col-sm-2">
                                    <input id="name" name="name" class="form-control" type="text" value="${projectTache.name}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">项目环节简称：</label>
                                <div class="col-sm-1">
                                    <input id="name" name="simpleName" class="form-control" type="text" value="${projectTache.simpleName}">
                                </div>
                            </div>
                            <#--<div class="form-group">
                                <label class="col-sm-3 control-label">环节顺序(再次环节之后)：</label>
                                <div class="col-sm-2">
                                    <select name="sortIndex" class="form-control">
                                        &lt;#&ndash;<option value="1" >请选择</option>&ndash;&gt;
                                        <#list projectTacheList as item >
                                            <option value="${item.id }" <#if item.id == (projectTache.sortIndex ) > selected="selected"</#if>>${item.name }</option>
                                        </#list>
                                    </select>
                                </div>
                            </div>-->
                            <div class="form-group">
                                <label class="col-sm-3 control-label">状态：</label>
                                <div class="col-sm-2">
                                	<select name="status" class="form-control">
                                		<option value="0" <#if projectTache.status == 0>selected="selected"</#if>>正常</option>
                                		<option value="1" <#if projectTache.status == 1>selected="selected"</#if>>异常</option>
                                	</select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">拥有状态：</label>
                                <div class="col-sm-2">

                                    <select class="selectpicker" name="haveStatus" id = "haveStatus" multiple data-live-search="true" data-live-search-placeholder="Search" data-actions-box="true">
                                    <#--<optgroup label="filter1">-->
                                    <#list statusList as u >
                                        <option value="${u.id }"  <#if haveStatusList?seq_contains(u.id) > selected="selected"</#if> >${u.name }</option>
                                    </#list>
                                    <#--</optgroup>
                                    <optgroup label="filter2">
                                        <option>option1</option>
                                        <option>option2</option>
                                        <option>option3</option>
                                        <option>option4</option>
                                    </optgroup>
                                    -->
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">所属阶段：</label>
                                <div class="col-sm-2">
                                    <select name="stage" class="form-control">
                                        <option value="0" <#if projectTache.stage == 0>selected="selected"</#if>>产品</option>
                                        <option value="1" <#if projectTache.stage == 1>selected="selected"</#if>>开发</option>
                                        <option value="2" <#if projectTache.stage == 2>selected="selected"</#if>>测试</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">创建描述：</label>
                                <div class="col-sm-4">
                                    <textarea style="height: 80px;" id="createComment" name="createComment" class="form-control">${projectTache.createComment}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">修改描述：</label>
                                <div class="col-sm-4">
                                    <textarea style="height: 80px;" id="updateComment" name="updateComment" class="form-control">${projectTache.updateComment}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                    <div class="col-sm-8 col-sm-offset-3">
                                        <@shiro.hasPermission name="plan:projectTache:edit">
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

    <script src="${ctx!}/js/plan.js"></script>
    <script type="text/javascript">
    $(document).ready(function () {

	    $("#frm").validate({
    	    rules: {
    	    	name: {
    	        required: true,
    	        minlength: 2,
    	    	maxlength: 20
    	      },
                simpleName: {
                required: true,
                minlength: 1,
                maxlength: 10
              },
                haveStatus: {
                    required: true,
                    minlength: 1,
                    maxlength: 10
              },
                sortIndex: {
                required: false
              },
                status: {
    	        required: true
    	      },
                createComment: {
                required: false,
                maxlength: 2000
              },
                updateComment: {
                required: false,
                maxlength: 2000
              }
    	    },
    	    messages: {},
    	    submitHandler:function(form){

                var haveStatus = $("#haveStatus").val();
                var flag = (haveStatus==null);
               if(flag){
                   layer.msg("必须选择一个以上拥有的状态", {time: 4000},function(){
                       $("#haveStatus").focus();
                   });
                   return;
               }
    	    	$.ajax({
   	    		   type: "POST",
   	    		   dataType: "json",
   	    		   url: "${ctx!}/plan/projectTache/edit",
   	    		   data: $(form).serialize(),
   	    		   success: function(msg){
	   	    			layer.msg(msg.message, {time: 2000},function(){
                            closeWindow();
	   					});
   	    		   }
   	    		});
            } 
    	});

    });

    </script>

</body>

</html>
