package com.project.plan.controller.plan;

import com.project.plan.common.JsonResult;
import com.project.plan.controller.BaseController;
import com.project.plan.entity.plan.Project;
import com.project.plan.service.plan.PlanServiceImpl;
import com.project.plan.service.plan.ProjectServiceImpl;
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

/**
 * Created by Barry on 2018/4/20.
 */
@Controller
@RequestMapping("/plan/project")
public class ProjectController  extends BaseController {
    @Autowired
    private PlanServiceImpl planService;
    @Autowired
    private ProjectServiceImpl projectService;

    @ApiIgnore
    @RequestMapping(value = { "","/", "/index" })
    public String index() {
        return "plan/project/index";
    }

    @ApiOperation(value="获取项目列表", notes="获取项目列表")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public Page<Project> list() {
        SimpleSpecificationBuilder<Project> builder = new SimpleSpecificationBuilder<Project>();
        String searchText = request.getParameter("searchText");
        if(StringUtils.isNotBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }
        Page<Project> page = projectService.findAll(builder.generateSpecification(),getPageRequest());
        return page;
    }

    @ApiOperation(value="跳到添加项目页面", notes="增加和修改是一个页面")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        List<Project> list = projectService.findAll();
        map.put("list", list);
        return "plan/project/form";
    }

    @ApiOperation(value="跳到修改项目页面", notes="增加和修改是一个页面")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        Project project = projectService.find(id);
        map.put("project", project);

        List<Project> list = projectService.findAll();
        map.put("list", list);
        return "plan/project/form";
    }

    @ApiOperation(value="修改或添加项目", notes="user的属性,有id就是修改,没有id添加")
    @RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(Project project, ModelMap map){
        try {
            projectService.saveOrUpdate(project);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @ApiOperation(value="删除用户", notes="根据用户id删除用户")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id,ModelMap map) {
        try {
            projectService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
}
