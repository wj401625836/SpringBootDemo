<!DOCTYPE html>
<html>

<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <title> - 标签墙</title>
    <meta name="keywords" content="">
    <meta name="description" content="">

    <link rel="shortcut icon" href="favicon.ico"> <link href="${ctx!}/assets/css/bootstrap.min.css?v=3.3.6" rel="stylesheet">
    <link href="${ctx!}/assets/css/font-awesome.css?v=4.4.0" rel="stylesheet">

    <link href="${ctx!}/assets/css/animate.css" rel="stylesheet">
    <link href="${ctx!}/assets/css/style.css?v=4.1.0" rel="stylesheet">
    <style>
        /* 覆盖外部样式,重新调整便签大小 */
        ul.notes li div{
            height: 380px;
            width: 360px;
        }


    </style>
</head>

<body class="gray-bg">
<div class="row">
    <div class="col-sm-12">
        <div class="wrapper wrapper-content animated fadeInUp">
            <#if page.content?? && (page.content?size > 0) >
                <ul class="notes">
                    <#--<li>
                        <div>
                            <small>2014年10月24日(星期五) 下午5:31</small>
                            <h4>HTML5 文档类型</h4>
                            <p>Bootstrap 使用到的某些 HTML 元素和 CSS 属性需要将页面设置为 HTML5 文档类型。在你项目中的每个页面都要参照下面的格式进行设置。</p>
                            <a href="pin_board.html#"><i class="fa fa-trash-o "></i></a>
                        </div>
                    </li>-->
                    <#list page.content as module >
                        <li>
                            <div>
                                <small><b>
                                    <#switch module.status>
                                        <#case 0><label class='control-label' style='color:green; '>待启动</label><#break>
                                        <#case 1><label class='control-label' style='color:#23c6c8; '>进行中</label> <#break>
                                        <#case 2><label class='control-label' style='color:red; '>暂停</label><#break>
                                        <#case 3><label class='control-label' style='color:green; '>待上线</label> <#break>
                                        <#case 4><label class='control-label' style='color:green; '>已上线</label> <#break>
                                        <#default>未知状态
                                    </#switch>
                                </b>
                                    &nbsp;&nbsp;&nbsp;&nbsp;

                                ${(module.startTime?string('yyyy-MM-dd'))!''}  - ${(module.wishTime?string('yyyy/MM/dd'))!''}</small>
                                <h4>${module.name }  &nbsp;  </h4>
                                <p>${module.createCommentStr}</p>
                                <a href="${ctx!}/plan/tache/oneModuleDetail?moduleId=${module.id }"><i class="glyphicon glyphicon-search">详情</i></a>
                            </div>
                        </li>
                    </#list>
                </ul>
            <#else>
                <div class="middle-box text-center animated fadeInDown">
                    <h2>无任务</h2>
                    <h3 class="font-bold"> 还没有正在做的功能！</h3>

                    <div class="error-desc">
                        可能是状态是新创建和已上线(这两个状态到这里不显示).
                        <button class="btn btn-success " type="button" onclick="location.href='${ctx!}/plan/module/index'">&nbsp;查看任务列表</button>
                        <br/><br/><br/>
                        <@shiro.hasPermission name="plan:module:add">
                            点击【立即创建】按钮添加新的功能
                            <button class="btn btn-success " type="button" onclick="add();"><i class="fa fa-plus"></i>&nbsp;添加</button>
                        </@shiro.hasPermission>
                    </div>
                </div>
            </#if>
        </div>
    </div>
</div>

<!-- 全局js -->
<script src="${ctx!}/assets/js/jquery.min.js?v=2.1.4"></script>
<script src="${ctx!}/assets/js/bootstrap.min.js?v=3.3.6"></script>



<!-- 自定义js -->
<script src="${ctx!}/assets/js/content.js?v=1.0.0"></script>

<script src="${ctx!}/assets/js/plugins/layer/layer.min.js"></script>

<script >
    var rootUrl = "${ctx!}/plan/module";

    function add(){
        layer.open({
            type: 2,
            title: '功能添加',
            shadeClose: true,
            shade: false,
            area: ['80%', '90%'],
            content: rootUrl+'/add',
            end: function(index){
                $('#table_list').bootstrapTable("refresh");
            }
        });
    }
</script>


</body>

</html>