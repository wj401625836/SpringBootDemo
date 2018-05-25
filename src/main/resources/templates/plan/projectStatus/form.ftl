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
                        <p>项目环节状态为项目环节中全局状态,可以配置功能下的环节使用全局状态中的哪些状态,项目环节中选择要哪些。</p>
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
                        <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/plan/status/edit">
                        	<input type="hidden" id="id" name="id" value="${status.id}">

                            <div class="form-group">
                                <label class="col-sm-3 control-label" >状态名称：</label>
                                <div class="col-sm-2">
                                    <input id="name" name="name" class="form-control" type="text" value="${status.name}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">状态类型：</label>
                                <div class="col-sm-2">
                                	<select name="status" class="form-control">
                                		<option value="0" <#if status.status == 0>selected="selected"</#if>>新创建</option>
                                		<option value="1" <#if status.status == 1>selected="selected"</#if>>执行中</option>
                                        <option value="2" <#if status.status == 2>selected="selected"</#if>>已完成</option>
                                	</select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">创建描述：</label>
                                <div class="col-sm-4">
                                    <textarea style="height: 80px;" id="createComment" name="createComment" class="form-control">${status.createComment}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-sm-3 control-label">修改描述：</label>
                                <div class="col-sm-4">
                                    <textarea style="height: 80px;" id="updateComment" name="updateComment" class="form-control">${status.updateComment}</textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                    <div class="col-sm-8 col-sm-offset-3">
                                        <@shiro.hasPermission name="plan:status:edit">
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
    	    	$.ajax({
   	    		   type: "POST",
   	    		   dataType: "json",
   	    		   url: "${ctx!}/plan/status/edit",
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
