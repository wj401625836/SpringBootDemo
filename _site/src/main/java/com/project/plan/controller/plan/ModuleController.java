package com.project.plan.controller.plan;

import com.project.plan.common.JsonResult;
import com.project.plan.controller.BaseController;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.Project;
import com.project.plan.entity.plan.Tache;
import com.project.plan.service.plan.ModuleServiceImpl;
import com.project.plan.service.plan.PlanServiceImpl;
import com.project.plan.service.plan.ProjectServiceImpl;
import com.project.plan.service.plan.TacheServiceImpl;
import com.project.plan.service.specification.SimpleSpecificationBuilder;
import com.project.plan.service.specification.SpecificationOperator;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * Created by Barry on 2018/4/20.
 */
@Controller
@RequestMapping("/plan/module")
public class ModuleController extends BaseController {
    @Autowired
    private PlanServiceImpl planService;
    @Autowired
    private ModuleServiceImpl moduleService;
    @Autowired
    private ProjectServiceImpl projectService;
    @Autowired
    private TacheServiceImpl tacheService;

    @ApiIgnore
    @RequestMapping(value = { "","/", "/index" })
    public String index() {
        return "plan/module/index";
    }

    @ApiOperation(value="分页获取模块列表", notes="可以更具传入的searchText根据模块名模糊搜索")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public Page<Module> list() {
        SimpleSpecificationBuilder<Module> builder = new SimpleSpecificationBuilder<Module>();
        String searchText = request.getParameter("searchText");
        if(StringUtils.isNotBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
//        builder.generateSpecification()
//      builder

        Page<Module> page = moduleService.findAllWithProject(builder.generateSpecification(),getPageRequest());
        for(Module m: page.getContent()){//查询一个模块下面的描述,具体哪些人哪些功能已经上线，哪些功能没有还做,放到createComments 里面,
            List<Tache> taches = tacheService.findAllByModuleIdWithUser(m.getId());

            String createComments = findModuleComments(taches,Tache.STAT_NEW);       //未归档了环节描述
            String updateComments = findModuleComments(taches,Tache.STAT_SUCCESS);   //已经归档了环节描述

            m.setCreateCommentStr(createComments);
            m.setUpdateCommentStr(updateComments);
        }
        return page;
    }
    /**
     * 查询这个项目下面的描述
     */
    public String findModuleComments(List<Tache> taches,int status) {

        StringBuffer sb = new StringBuffer();
        for (int i =0 ; i< taches.size() ; i++) {
            Tache t = taches.get(i);

            String statusStr = Tache.TACHE_STATUS(t.getStatus());
            String colorStr = null;
            switch (t.getStatus()){
                case Tache.STAT_NEW:colorStr="#23c6c8";break;//蓝色
                case Tache.STAT_DEBUG:colorStr="#7266ba";break;//橙色
                case Tache.STAT_TESTING:colorStr="#27c24c";break;//绿色
                case Tache.STAT_SUCCESS:colorStr="green";break; //
                default: colorStr="red";break;
            }
            //区分是要拿 已经完成了的描述还是没有完成的描述 ,
            if(Tache.STAT_SUCCESS==status && status != t.getStatus()){//拿已经归档的描述，但是该环节没归档 ,continue
                continue;
            }else if(Tache.STAT_SUCCESS != status && Tache.STAT_SUCCESS == t.getStatus()){ //拿没有归档的描述，但是该环节已经归档 ,continue
                continue;
            }
            String comment = "“ <label class='control-label' style='color:green; '>"+t.getName()+"”</label>；" ;
            if(Tache.STAT_SUCCESS != status){//归档只有一种状态,已经归档了的就不用显示状态了吧
                comment += " 状态为“<label class='control-label' style='color:"+colorStr+"; '>"+statusStr+" ”</label>,";
            }

            if(t.getUser()!=null){
                comment += "由“<label class='control-label' style='color:green; '>"+t.getUser().getNickName()+"</label>”处理。";
            }
            if(i%2==0){
                comment += " </br>'";
            }else {
                comment += " &nbsp;&nbsp;&nbsp;'";
            }
            sb.append(comment);
        }
        return sb.toString();
    }

    @ApiOperation(value="跳到添加模块页面", notes="模块增加和修改需要拿到项目列表")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        List<Project> projectList = projectService.findAll();
        map.put("projectList",projectList);
        return "plan/module/form";
    }

    @ApiOperation(value="跳到修改模块页面", notes="模块增加和修改需要拿到项目列表")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        Module module = moduleService.find(id);
        List<Project> projectList = projectService.findAll();
        map.put("module", module);
        map.put("projectList",projectList);
        List<Module> list = moduleService.findAll();
        map.put("list", list);
        return "plan/module/form";
    }

//    @RequiresPermissions("plan:module:edit")
    @ApiOperation(value="修改或添加模块", notes="有id就是修改,没有id添加")
    @RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(Module module, ModelMap map){
        try {
            moduleService.saveOrUpdate(module);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

//    @RequiresPermissions("plan:module:delete")
    @ApiOperation(value="删除模块", notes="根据id删除模块,同时删除其下面的环节")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id,ModelMap map) {
        try {
            moduleService.deleteModule(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

}
