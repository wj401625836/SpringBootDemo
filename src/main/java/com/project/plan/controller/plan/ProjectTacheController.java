package com.project.plan.controller.plan;

import com.project.plan.common.JsonResult;
import com.project.plan.common.utils.TextUtil;
import com.project.plan.controller.BaseController;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barry on 2018/5/15.
 */
@Controller
@RequestMapping("/plan/projectTache")
public class ProjectTacheController extends BaseController {
    @Autowired
    private ProjectTacheServiceImpl projectTacheService;
    @Autowired
    private ProjectStatusServiceImpl statusService;
    @Autowired
    private TacheServiceImpl tacheService;


    @ApiIgnore
    @RequestMapping(value = { "","/", "/index" })
    public String index(ModelMap map) {
        return "plan/projectTache/index";
    }

    @ApiOperation(value="分页获取项目环节列表", notes="可以更具传入的searchText根据项目环节名模糊搜索")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseBody
    public Page<ProjectTache> list() {
        SimpleSpecificationBuilder<ProjectTache> builder = new SimpleSpecificationBuilder<ProjectTache>();
        String searchText = request.getParameter("searchText");
        if(StringUtils.isNotBlank(searchText)){
            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
        }

        List<Sort.Order> orderList = new ArrayList<>();
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "sortIndex");
        orderList.add(order2);
        Sort sort =new Sort(orderList);//根据 status 升序,再根据 sortIndex 升序
        PageRequest pageRequest = getPageRequest(sort);


        Page<ProjectTache> page = projectTacheService.findAll(builder.generateSpecification(),pageRequest);
//        for(ProjectTache m: page.getContent()){//查询一个功能环节下面的状态,没做好，不到前端展示
//            String haveStatus = m.getHaveStatus();
//            if(TextUtil.isNullOrEmpty(haveStatus)){
//                continue;
//            }
//            String[] haveStatusList =m.getHaveStatus()==null?new String[0]:m.getHaveStatus().split(",");
//            List<Integer> haveStatusListIds = new ArrayList<>();
//            for (int i=0;i<haveStatusList.length;i++) {
//                String statusId = haveStatusList[i];
//                if(TextUtil.isNullOrEmpty(statusId)){
//                    continue;
//                }
//                haveStatusListIds.add(Integer.valueOf(statusId));
//            }
//            List<Status> status = statusService.findList(haveStatusListIds);
//            StringBuffer sb = new StringBuffer();
//            for (Status s:status) {
//                sb.append(s.getName()+",");
//            }
//
//        }
        return page;
    }

    @ApiOperation(value="跳到添加项目环节页面", notes="项目环节增加和修改需要拿到项目列表")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @RequiresPermissions("plan:projectTache:add")
    public String add(ModelMap map) {
        Sort sort = new Sort(Sort.Direction.ASC, "sortIndex");
        List<ProjectTache> projectTacheList = projectTacheService.findList(sort);

        List<ProjectStatus> statusList = statusService.findAll();

        map.put("projectTacheList",projectTacheList);
        map.put("statusList",statusList);
        map.put("haveStatusList",new Integer[0]);//所拥有的状态,避免空指针
        return "plan/projectTache/form";
    }

    @ApiOperation(value="跳到修改项目环节页面", notes="项目环节增加和修改需要拿到项目列表")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        ProjectTache projectTache = projectTacheService.find(id);

        Sort sort = new Sort(Sort.Direction.ASC, "sortIndex");
        List<ProjectTache> projectTacheList = projectTacheService.findList(sort);

        List<ProjectStatus> statusList = statusService.findAll();
        String[] haveStatusList =projectTache.getHaveStatus()==null?new String[0]:projectTache.getHaveStatus().split(",");
        List<Integer> haveStatusListIds = new ArrayList<>();
        for (int i=0;i<haveStatusList.length;i++) {
            String statusId = haveStatusList[i];
            if(TextUtil.isNullOrEmpty(statusId)){
                continue;
            }
            haveStatusListIds.add(Integer.valueOf(statusId));
        }


        map.put("projectTacheList",projectTacheList);
        map.put("statusList",statusList);
        map.put("haveStatusList",haveStatusListIds);//

        map.put("projectTache", projectTache);
        return "plan/projectTache/form";
    }

    //    @RequiresPermissions("plan:projectTache:edit")
    @ApiOperation(value="修改或添加项目环节", notes="有id就是修改,没有id添加")
    @RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(ProjectTache projectTache, ModelMap map){
        try {
            if(projectTache.getSortIndex()==null||projectTache.getSortIndex()==0){//如果不知道是第几个就默认是第一个
                projectTache.setSortIndex(1);
            }
            //修改能不能
            projectTacheService.saveOrUpdate(projectTache);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    //    @RequiresPermissions("plan:projectTache:delete")
    @ApiOperation(value="删除项目环节", notes="根据id删除项目环节,同时删除其下面的环节")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id,ModelMap map) {
        try {
            //判断业务能不能删除
            SimpleSpecificationBuilder<Tache> builder = new SimpleSpecificationBuilder<Tache>();
            builder.add("projectTacheId", SpecificationOperator.Operator.eq.name(), id);
            long count = tacheService.count(builder.generateSpecification());
            if(count>0){
                throw new RuntimeException("有功能正在用这个项目环节,不能删除。");
            }
            projectTacheService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }
    @ApiOperation(value="跳到调整环节状态页面", notes="项目环节调整需要拿到所有状态")
    @RequestMapping(value = "/changeSort", method = RequestMethod.GET)
    public String changeSort(ModelMap map) {
        List<ProjectTache> projectTacheList = projectTacheService.findAll();

        map.put("projectTacheList",projectTacheList);
        return "plan/projectTache/changeSort";
    }

    @ApiOperation(value="调整项目环节页面排序", notes="项目环节排序")
    @RequestMapping(value = "/changeSorted", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult changeSorted(ModelMap map,Integer[] projectTache1Ids,Integer[] projectTache2Ids,Integer[] projectTache3Ids) {
        try {
            System.out.println(projectTache1Ids+"\t"+projectTache2Ids +"\t"+projectTache3Ids );
            // projectTache1Ids 所有产品下的环节, projectTache2Ids 开发下的环节 projectTache3Ids 测试下的环节
            projectTacheService.changeSortIndex(projectTache1Ids);
            projectTacheService.changeSortIndex(projectTache2Ids);
            projectTacheService.changeSortIndex(projectTache3Ids);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

}
