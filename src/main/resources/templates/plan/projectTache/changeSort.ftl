<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title> - 任务清单</title>
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
<div class="wrapper wrapper-content  animated fadeInRight">

    <form class="form-horizontal m-t" id="frm" method="post" action="${ctx!}/plan/status/changeSorted">
        <div class="row">
            <button class="btn btn-primary" type="submit">保存页面修改</button>
            <p class="small"> 在列表之间拖动任务面板</p>
        </div>
        <div class="row">
            <div class="col-sm-8">
                <div class="ibox">
                    <div class="ibox-content">
                        <h3>产品</h3>
                        <p class="small"><i class="fa fa-hand-o-up"></i> 在列表之内拖动任务面板</p>

                        <ul class="sortable-list connectList agile-list">
                        <#list projectTacheList as item >
                            <#--<#if item.stage == 0>-->
                            <#switch item.stage>
                                <#case 0>
                                    <li class="warning-element">产品:
                                        <input type="hidden" name="projectTache1Ids" value="${item.id }"/>
                                        ${item.name }
                                        <div class="agile-detail">
                                            <a href="#" class="pull-right btn btn-xs btn-white">拖动</a>
                                            <i class="fa fa-clock-o"></i> ${item.createTime }
                                        </div>
                                    </li> <#break>
                                <#case 1>
                                    <li class="success-element">开发:
                                        <input type="hidden" name="projectTache1Ids" value="${item.id }"/>
                                    ${item.name }
                                        <div class="agile-detail">
                                            <a href="#" class="pull-right btn btn-xs btn-white">拖动</a>
                                            <i class="fa fa-clock-o"></i> ${item.createTime }
                                        </div>
                                    </li> <#break>
                                <#case 2>
                                    <li class="danger-element">测试:
                                        <input type="hidden" name="projectTache1Ids" value="${item.id }"/>
                                    ${item.name }
                                        <div class="agile-detail">
                                            <a href="#" class="pull-right btn btn-xs btn-white">拖动</a>
                                            <i class="fa fa-clock-o"></i> ${item.createTime }
                                        </div>
                                    </li> <#break>
                                <#default>
                                    <li class="alert-element">其他:
                                        <input type="hidden" name="projectTache1Ids" value="${item.id }"/>
                                    ${item.name }
                                        <div class="agile-detail">
                                            <a href="#" class="pull-right btn btn-xs btn-white">拖动</a>
                                            <i class="fa fa-clock-o"></i> ${item.createTime }
                                        </div>
                                    </li>
                            </#switch>

                            <#--</#if>-->
                        </#list>
                        </ul>
                    </div>
                </div>
            </div>
            <#--<div class="col-sm-4">
                <div class="ibox">
                    <div class="ibox-content">
                        <h3>产品</h3>
                        <p class="small"><i class="fa fa-hand-o-up"></i> 在列表之内拖动任务面板</p>

                        <ul class="sortable-list connectList agile-list">
                        <#list projectTacheList as item >
                            <#if item.stage == 0>
                                <li class="warning-element">
                                    <input type="hidden" name="projectTache1Ids" value="${item.id }"/>
                                ${item.name }
                                </li>
                            </#if>
                        </#list>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="ibox">
                    <div class="ibox-content">
                        <h3>开发</h3>
                        <p class="small"><i class="fa fa-hand-o-up"></i> 在列表之内拖动任务面板</p>
                        <ul class="sortable-list connectList agile-list">
                        <#list projectTacheList as item >
                            <#if item.stage == 1>
                                <li class="success-element">
                                    <input type="hidden" name="projectTache2Ids" value="${item.id }"/>
                                ${item.name }
                                </li>
                            </#if>
                        </#list>
                        &lt;#&ndash;<li class="">
                             根据自己以前所了解的和从其他途径搜索到的信息，录入客户资料150家。
                             <div class="agile-detail">
                                 <a href="#" class="pull-right btn btn-xs btn-white">标记</a>
                                 <i class="fa fa-clock-o"></i> 2015.05.12
                             </div>
                         </li>&ndash;&gt;
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="ibox">
                    <div class="ibox-content">
                        <h3>测试</h3>
                        <p class="small"><i class="fa fa-hand-o-up"></i> 在列表之内拖动任务面板</p>
                        <ul class="sortable-list connectList agile-list">
                        <#list projectTacheList as item >
                            <#if item.stage == 2>
                                <li class="warning-element">
                                    <input type="hidden" name="projectTache3Ids" value="${item.id }"/>
                                ${item.name }
                                </li>
                            </#if>
                        </#list>
                        </ul>
                    </div>
                </div>
            </div>-->

        </div>

    </form>
</div>

<!-- 全局js -->
<script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>
<script src="${ctx!}/assets/js/jquery-ui-1.10.4.min.js"></script>

<!-- 自定义js -->
<script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>


<!-- jQuery Validation plugin javascript-->
<script src="${ctx!}/assets/js/plugins/validate/jquery.validate.min.js"></script>
<script src="${ctx!}/assets/js/plugins/validate/messages_zh.min.js"></script>
<script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>
<script src="${ctx!}/assets/js/plugins/layer/laydate/laydate.js"></script>
<script src="${ctx!}/js/plan.js"></script>
<script>
    $(document).ready(function () {
        $(".sortable-list").sortable({
            connectWith: ".connectList"
        }).disableSelection();

    });



    $(document).ready(function () {
        //外部js调用
        $("#frm").validate({
            rules: {
                name: {
                    required: true,
                    minlength: 4,
                    maxlength: 20
                },
                startTime: {
                    required: true,
                    minlength: 4,
                    maxlength: 20
                },
                wishTime: {
                    required: true,
                    minlength: 4,
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
                    url: "${ctx!}/plan/projectTache/changeSorted",
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
