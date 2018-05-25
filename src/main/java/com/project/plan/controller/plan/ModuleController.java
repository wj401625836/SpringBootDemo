package com.project.plan.controller.plan;

import com.project.plan.common.JsonResult;
import com.project.plan.controller.BaseController;
import com.project.plan.dao.plan.IProjectStatusDao;
import com.project.plan.entity.plan.*;
import com.project.plan.service.plan.*;
import com.project.plan.service.specification.SimpleSpecificationBuilder;
import com.project.plan.service.specification.SpecificationOperator;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    @Autowired
    private ProjectTacheServiceImpl projectTacheService;
    @Autowired
    private IProjectStatusDao statusDao;

    @ApiIgnore
    @RequestMapping(value = { "","/", "/index" })
    public String index(ModelMap map) {
//        Map<String,Long> typeMap = tacheService.selectTypeMap();
//        map.put("typeMap",typeMap);
        return "plan/module/index";
    }

    @ApiOperation(value="分页获取模块列表", notes="可以更具传入的searchText根据模块名模糊搜索")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
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
        List<ProjectStatus> statusList = statusDao.findAll();
        for(Module m: page.getContent()){//查询一个模块下面的描述,具体哪些人哪些功能已经上线，哪些功能没有还做,放到createComments 里面,
            List<Tache> taches = tacheService.findAllByModuleIdWithUser(m.getId());

            //String createComments = findModuleComments(taches,Tache.STAT_NEW,false);       //未归档了环节描述
            //String updateComments = findModuleComments(taches,Tache.STAT_SUCCESS,false);   //已经归档了环节描述
            String createComments = findModuleComments(taches,statusList, ProjectStatus.STAT_NEW,false);       //未归档了环节描述
            String updateComments = findModuleComments(taches,statusList, ProjectStatus.STAT_SUCCESS,false);   //已经归档了环节描述


            m.setCreateCommentStr(createComments);
            m.setUpdateCommentStr(updateComments);
        }
        return page;
    }

    /**
     * 查询这个项目下面的描述
     * @param withComment 需不需要描述,如果不需要直接返回环节名称
     */
    public static String findModuleComments(List<Tache> taches, List<ProjectStatus> statusList, int status, boolean withComment) {

        StringBuffer sb = new StringBuffer();
        for (int i =0 ; i< taches.size() ; i++) {
            Tache t = taches.get(i);
            ProjectStatus tStatus = new ProjectStatus();
            tStatus.setId(0);
            tStatus.setName("未知");
            tStatus.setStatus(-1);

            String statusStr = "未知";
            for (ProjectStatus s:statusList) {
                if(s.getId().equals(t.getStatus())){
                    statusStr = s.getName();
                    tStatus = s ;
                    break;
                }
            }


            String colorStr = null;
            switch (t.getStatus()){
                case ProjectStatus.STAT_NEW:colorStr="#23c6c8";break;//蓝色
                case ProjectStatus.STAT_DOING:colorStr="#27c24c";break;//绿色
                case ProjectStatus.STAT_SUCCESS:colorStr="green";break; //
                default: colorStr="red";break;
            }
            //区分是要拿 已经完成了的描述还是没有完成的描述 ,
            if(ProjectStatus.STAT_SUCCESS==status && status != tStatus.getStatus()){//拿已经归档的描述，但是该环节没归档 ,continue
                continue;
            }else if(ProjectStatus.STAT_SUCCESS != tStatus.getStatus() && ProjectStatus.STAT_SUCCESS == tStatus.getStatus()){ //拿没有归档的描述，但是该环节已经归档 ,continue
                continue;
            }
            String comment = "“ <label class='control-label' style='color:green; '>"+t.getSimpleName()+"</label>”；" ;
            if(!withComment){//不需要 状态等描述
                if(true){//一个名称换一行
                    sb.append(comment+" <br/>");
                    continue;
                }
                int lastIndex = sb.lastIndexOf("<br/>");
                if((sb.lastIndexOf("<br/>") == -1 && sb.length()>100) || lastIndex>0&&sb.substring(lastIndex,sb.length()).length()>100) {//大于100个字就换行 : (没有换行标签&&字数大于指定值)||(有换行&& 大于定值)
                    comment += " <br/>";
                }
                sb.append(comment);
                continue;
            }else{
                if(ProjectStatus.STAT_SUCCESS != tStatus.getStatus()){//归档只有一种状态,已经归档了的就不用显示状态了吧
                    comment += " 状态为“<label class='control-label' style='color:"+colorStr+"; '>"+statusStr+"</label>”,";
                }
                if(t.getUser()!=null){
                    comment += "由“<label class='control-label' style='color:green; '>"+t.getUser().getNickName()+"</label>”处理。";
                }
                if(i%1==0){
                    comment += " <br/>";
                }else {
                    comment += " &nbsp;&nbsp;&nbsp;";
                }
                sb.append(comment);
            }
        }
        return sb.toString();
    }
    /*public static String findModuleComments(List<Tache> taches,int status,boolean withComment) {

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
            String comment = "“ <label class='control-label' style='color:green; '>"+t.getSimpleName()+"</label>”；" ;
            if(!withComment){//不需要 状态等描述
                if(true){//一个名称换一行
                    sb.append(comment+" <br/>");
                    continue;
                }
                int lastIndex = sb.lastIndexOf("<br/>");
//                String sub=sb.substring(lastIndex,sb.length());
//                System.out.println(sub+"   _ "+sub.length());
                if((sb.lastIndexOf("<br/>") == -1 && sb.length()>100) || lastIndex>0&&sb.substring(lastIndex,sb.length()).length()>100) {//大于100个字就换行 : (没有换行标签&&字数大于指定值)||(有换行&& 大于定值)
                    comment += " <br/>";
                }
                sb.append(comment);
                continue;
            }

            if(Tache.STAT_SUCCESS != status){//归档只有一种状态,已经归档了的就不用显示状态了吧
                comment += " 状态为“<label class='control-label' style='color:"+colorStr+"; '>"+statusStr+"</label>”,";
            }

            if(t.getUser()!=null){
                comment += "由“<label class='control-label' style='color:green; '>"+t.getUser().getNickName()+"</label>”处理。";
            }
            if(i%1==0){
                comment += " <br/>";
            }else {
                comment += " &nbsp;&nbsp;&nbsp;";
            }
            sb.append(comment);
        }
        return sb.toString();
    }*/

    @ApiOperation(value="跳到添加模块页面", notes="模块增加和修改需要拿到项目列表")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @RequiresPermissions("plan:module:add")
    public String add(ModelMap map) {
        List<Project> projectList = projectService.findAll();

        SimpleSpecificationBuilder<ProjectTache> builder = new SimpleSpecificationBuilder<ProjectTache>();
        builder.add("status",SpecificationOperator.Operator.eq.name(),ProjectTache.STAT_DEFAULT);
        List<ProjectTache> projectTacheList = projectTacheService.findList(builder.generateSpecification());

        map.put("projectList",projectList);
        map.put("projectTacheList",projectTacheList);
        return "plan/module/form";
    }

    @ApiOperation(value="跳到修改模块页面", notes="模块增加和修改需要拿到项目列表")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        Module module = moduleService.find(id);
        List<Project> projectList = projectService.findAll();


        SimpleSpecificationBuilder<ProjectTache> builder = new SimpleSpecificationBuilder<>();
        builder.add("status",SpecificationOperator.Operator.eq.name(),ProjectTache.STAT_DEFAULT);
        List<ProjectTache> projectTacheList = projectTacheService.findList(builder.generateSpecification());


        map.put("module", module);
        map.put("projectList",projectList);
        map.put("projectTacheList",projectTacheList);

        return "plan/module/form";
    }

//    @RequiresPermissions("plan:module:edit")
    @ApiOperation(value="修改或添加模块", notes="有id就是修改,没有id添加")
    @RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(Module module,Integer[]haveTacheIds, ModelMap map){
        try {
            if(module.getId()==null&&haveTacheIds.length<=0){
                throw new RuntimeException("新建功能必须为其设定有哪些环节！");
            }
            moduleService.saveOrUpdate(module,haveTacheIds);
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
