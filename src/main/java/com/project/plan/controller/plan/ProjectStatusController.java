package com.project.plan.controller.plan;

import com.project.plan.common.JsonResult;
import com.project.plan.controller.BaseController;
import com.project.plan.entity.plan.ProjectStatus;
import com.project.plan.service.plan.*;
import com.project.plan.service.specification.SimpleSpecificationBuilder;
import com.project.plan.service.specification.SpecificationOperator;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;
import org.springframework.data.domain.Sort.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barry on 2018/5/15.
 */
@Controller
@RequestMapping("/plan/status")
public class ProjectStatusController extends BaseController {
    @Autowired
    private ProjectStatusServiceImpl statusService;
    @Autowired
    private ProjectTacheServiceImpl projectTacheService;


    @ApiIgnore
    @RequestMapping(value = { "","/", "/index" })
    public String index(ModelMap map) {
        return "plan/projectStatus/index";
    }

    @ApiOperation(value="分页获取项目状态列表", notes="可以更具传入的searchText根据项目状态名模糊搜索")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public Page<ProjectStatus> list() {
        SimpleSpecificationBuilder<ProjectStatus> builder = new SimpleSpecificationBuilder<ProjectStatus>();
        String searchText = request.getParameter("searchText");
        if(StringUtils.isNotBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }


        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order(Direction.ASC, "status");
        Order order2 = new Order(Direction.ASC, "sortIndex");
        orderList.add(order1);
        orderList.add(order2);
        Sort sort =new Sort(orderList);//根据 status 升序,再根据 sortIndex 升序
        PageRequest pageRequest = getPageRequest(sort);

        Page<ProjectStatus> page = statusService.findAll(builder.generateSpecification(),pageRequest);
        return page;
    }

    @ApiOperation(value="跳到添加项目状态页面", notes="项目状态增加和修改需要拿到项目列表")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @RequiresPermissions("plan:status:add")
    public String add(ModelMap map) {
        return "plan/projectStatus/form";
    }

    @ApiOperation(value="跳到修改项目状态页面", notes="项目状态增加和修改需要拿到项目列表")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        ProjectStatus status = statusService.find(id);

        map.put("status", status);
        return "plan/projectStatus/form";
    }

    //    @RequiresPermissions("plan:status:edit")
    @ApiOperation(value="修改或添加项目状态", notes="有id就是修改,没有id添加")
    @RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(ProjectStatus status, ModelMap map){
        try {
            //修改能不能
            statusService.saveOrUpdate(status);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    //    @RequiresPermissions("plan:status:delete")
    @ApiOperation(value="删除项目状态", notes="根据id删除项目状态,同时删除其下面的环节")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id,ModelMap map) {
        try {
            //判断业务能不能删除
            boolean flag = projectTacheService.isStatusUse(id);
            if(flag){
                throw new RuntimeException("有项目环节正在使用这个状态,不能删除");
            }
            statusService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @ApiOperation(value="跳到调整项目状态页面", notes="项目状态调整需要拿到所有状态")
    @RequestMapping(value = "/changeSort", method = RequestMethod.GET)
    public String changeSort(ModelMap map) {
        List<ProjectStatus> statusList = statusService.findAll();

        map.put("statusList",statusList);
        return "plan/projectStatus/changeSort";
    }
    @ApiOperation(value="调整项目状态页面排序", notes="项目状态排序")
    @RequestMapping(value = "/changeSorted", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult changeSorted(ModelMap map,Integer[] status1Ids,Integer[] status2Ids,Integer[] status3Ids) {
        try {
            System.out.println(status1Ids+"\t"+status2Ids +"\t"+status3Ids );
            statusService.changeSortIndex(status1Ids);
            statusService.changeSortIndex(status2Ids);
            statusService.changeSortIndex(status3Ids);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }


}
