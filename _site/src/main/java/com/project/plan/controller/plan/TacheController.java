package com.project.plan.controller.plan;

import com.project.plan.common.Constats;
import com.project.plan.common.JsonResult;
import com.project.plan.common.utils.DateUtil;
import com.project.plan.common.utils.HttpUtil;
import com.project.plan.common.utils.TextUtil;
import com.project.plan.controller.BaseController;
import com.project.plan.entity.User;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.Openate;
import com.project.plan.entity.plan.Tache;
import com.project.plan.service.impl.UserServiceImpl;
import com.project.plan.service.plan.ModuleServiceImpl;
import com.project.plan.service.plan.OpenateServiceImpl;
import com.project.plan.service.plan.PlanServiceImpl;
import com.project.plan.service.plan.TacheServiceImpl;
import com.project.plan.service.specification.SimpleSpecificationBuilder;
import com.project.plan.service.specification.SpecificationOperator;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by Barry on 2018/4/20.
 */
@Controller
@RequestMapping("/plan/tache")
public class TacheController extends BaseController {
    @Autowired
    private PlanServiceImpl planService;
    @Autowired
    private TacheServiceImpl tacheService;
    @Autowired
    private OpenateServiceImpl openateService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ModuleServiceImpl moduleService;



    @ApiIgnore
    @RequestMapping(value = { "","/", "/index" })
    public String index(ModelMap map,String searchTypeName) {
//        Map<String,Long> typeMap = tacheService.selectTypeMap();
//        map.put("typeMap",typeMap);
        map.put("searchTypeName",searchTypeName);//用来做进这个页面后 ajax查询 list 数据的条件
        return "plan/tache/index";
    }

    @ApiOperation(value="分页获取环节列表", notes="可以更具传入的searchText到环节表中模糊搜索")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public Page<Tache> list(final Integer moduleId, final String searchTypeName) {
        String searchText = request.getParameter("searchText");

//        SimpleSpecificationBuilder<Tache> builder = new SimpleSpecificationBuilder<Tache>();
//        if(StringUtils.isNotBlank(searchText)){
//            builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
////            builder.add("name", SpecificationOperator.Join.values(), searchText);
//        }
//        if(moduleId != null&& moduleId > 0){//只查询这个module下的 tache
//            builder.add("moduleId",SpecificationOperator.Join.and.name(),moduleId);
//        }
//        Page<Tache> page = tacheService.findAll(builder.generateSpecification(),getPageRequest());

        final String name = searchText;
        Page<Tache> page = tacheService.findAll(new Specification<Tache>() {
            @Override
            public Predicate toPredicate(Root<Tache> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> whereList = new ArrayList<>();

                if(moduleId!=null&&moduleId>0){
                    Join<Module, Tache> join = root.join("module", JoinType.INNER);
                    Path<Integer> id = join.get("id");
                    Predicate p = cb.equal(id, moduleId);
                    whereList.add(p);
                }
                if(!TextUtil.isNullOrEmpty(name)){
                    Predicate p = cb.like(root.get("name").as(String.class), "%"+name+"%");
                    whereList.add(p);
                }
                if(!TextUtil.isNullOrEmpty(searchTypeName)){//只有从模块详情查询有多少个环节才会进来
//                    Constats.TACHE_TYPE_NAMES.length;
//                    Integer[] statusDoing = new Integer[]{Tache.STAT_DEBUG,Tache.STAT_TESTING};//正在执行
//                    Integer[] statusSuccess = new Integer[]{Tache.STAT_SUCCESS};//正在已经归档
                    if("全部".equalsIgnoreCase(searchTypeName)){
                        System.out.println("查询全部------");
                    }else if(Constats.TACHE_TYPE_NAMES[6].equals(searchTypeName)){
                        Predicate p = cb.equal(root.get("status"), Tache.STAT_SUCCESS);
                        whereList.add(p);

                        appenWhere(root, cb, whereList);
                    }else{ //正在执行
                        List<Predicate> orPredicates = new ArrayList();
                        Predicate p1 = cb.equal(root.get("status"), Tache.STAT_DEBUG);
                        orPredicates.add(cb.or(p1));
                        Predicate p2 = cb.equal((Path)root.get("status"), Tache.STAT_TESTING);
                        orPredicates.add(cb.or(p2));

                        Predicate orStatus = cb.or(p1,p2);
                        whereList.add(orStatus);

                        appenWhere(root, cb, whereList);
                    }





                }
                return query.where(whereList.toArray(new Predicate[whereList.size()])).getRestriction();
            }
            /**追加筛选条件*/
            private void appenWhere(Root<Tache> root, CriteriaBuilder cb, List<Predicate> whereList) {
                Integer[] indexs = Constats.getTacheIndexByType(searchTypeName);
                List<Predicate> orPredicates = new ArrayList();
                for(int i =0;i<indexs.length;i++){
                    Predicate p1 = cb.equal(root.get("tacheIndex"), indexs[i]+"");
                    orPredicates.add(cb.or(p1));
                }
                if(orPredicates.size()>0){
                    Predicate orIndex = cb.or(orPredicates.toArray(new Predicate[orPredicates.size()]));
                    whereList.add(orIndex);
                }
            }
        }, getPageRequest());

        return page;
    }
    @ApiOperation(value="跳到添加环节页面", notes="增加和修改是一个页面")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap map) {
        List<User> userList = userService.findAll();
        map.put("userList", userList);
        return "plan/tache/form";
    }

    @ApiOperation(value="跳到修改用户页面", notes="增加和修改是一个页面")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Integer id, ModelMap map) {
        Tache tache = tacheService.find(id);
        map.put("tache", tache);
        List<User> userList = userService.getBaseDao().findAll();
        map.put("userList", userList);

        return "plan/tache/form";
    }

    @ApiOperation(value="修改或添加环节", notes="有id就是修改,没有id添加")
    @RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult edit(Tache tache, ModelMap map,Integer moduleId){
        try {
            tacheService.editTache(tache,request);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @ApiOperation(value="删除用户", notes="根据用户id删除用户")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult delete(@PathVariable Integer id,ModelMap map) {
        try {
            tacheService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    /** 某环节环节备注记录列表 */
    @ApiOperation(value="环节记录列表", notes="记录此环节中所有做过的操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tacheId", value = "环节ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "Integer",defaultValue = "0")
    })
    @RequestMapping(value = "/recordlist",method = RequestMethod.GET)
    public String recordlist(ModelMap map ,Integer tacheId,@RequestParam(value="type",defaultValue="0")Integer type) {
        Tache tache = tacheService.getBaseDao().findOne(tacheId);
        List<Openate> openateList = openateService.findByTacheId(tacheId);

        map.put("tache",tache);
        map.put("openateList",openateList);
        map.put("type",type);//客户端请求过来的路径类型, 0默认是点击记录按钮进来的(给他显示添加记录),1是来查看记录的,不给他添加记录
        return "plan/tache/openateRecord";
    }


    /** 添加环节备注记录 */
    @ApiOperation(value="添加操作记录", notes="添加环节操作记录")
    @RequestMapping(value= {"/openateEdit"}, method = RequestMethod.POST)
    @ResponseBody
    public JsonResult openateEdit(Openate openate, ModelMap map,Integer moduleId){
        try {
            Date sysDate = new Date();
            User loginUser = (User) SecurityUtils.getSubject().getSession()
                    .getAttribute(Constats.CURRENTUSER);
            openate.setUserId(loginUser.getId());
            openate.setCreateTime(sysDate);
            openate.setDuration(0L);
            openate.setIp(HttpUtil.getClientIP(request));
            openate.setUrl(request.getRequestURL().toString());
            openate.setStatus(Openate.STATUS_WARN);

            openateService.save(openate);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.failure(e.getMessage());
        }
        return JsonResult.success();
    }

    @ApiOperation(value="获取一个模块的详情", notes="根据模块id获取这个模块和这个模块下的环节")
    @ApiImplicitParam(name = "moduleId", value = "模块id", required = true, dataType = "Integer")
    @RequestMapping(value= {"/oneModuleDetail"},method = RequestMethod.GET)
    public String oneModuleDetail(ModelMap map ,Integer moduleId){
        Module module = moduleService.find(moduleId);
        Map<String,Long> typeMap = tacheService.selectTypeMap();
        map.put("typeMap",typeMap);
        map.put("module",module);
        return "plan/module/moduleDetail";
    }
}
