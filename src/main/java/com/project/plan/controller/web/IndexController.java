package com.project.plan.controller.web;

import java.util.List;

import com.project.plan.controller.BaseController;
import com.project.plan.controller.plan.ModuleController;
import com.project.plan.dao.plan.IProjectStatusDao;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.ProjectStatus;
import com.project.plan.entity.plan.Tache;

import com.project.plan.service.plan.ModuleServiceImpl;
import com.project.plan.service.plan.ProjectServiceImpl;
import com.project.plan.service.plan.TacheServiceImpl;
import com.project.plan.service.specification.SimpleSpecificationBuilder;
import com.project.plan.service.specification.SpecificationOperator;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j
@Controller
public class IndexController extends BaseController {


	@Autowired
	private ModuleServiceImpl moduleService;
	@Autowired
	private ProjectServiceImpl projectService;
	@Autowired
	private TacheServiceImpl tacheService;

	@Autowired
	private IProjectStatusDao statusDao;
	/**前index页面*/
	@RequestMapping(value={"/","/index"})
	public String index(ModelMap map){
//		return redirect("/plan/module/index");

		SimpleSpecificationBuilder<Module> builder = new SimpleSpecificationBuilder<Module>();
		builder.add("status", SpecificationOperator.Operator.notEqual.name(), Module.STAT_DEFAULT);//新创建和已上线都不到首页显示
		builder.add("status", SpecificationOperator.Operator.notEqual.name(), Module.STAT_SUCCESS);


		Sort sort = new Sort(Sort.Direction.DESC, "id");
		PageRequest pageRequest = new PageRequest(0, 20, sort);//只显示最新20个功能

		Page<Module> page = moduleService.findAllWithProject(builder.generateSpecification(),pageRequest);
		List<ProjectStatus> statusList = statusDao.findAll();
		for(Module m: page.getContent()){//查询一个模块下面的描述,具体哪些人哪些功能已经上线，哪些功能没有还做,放到createComments 里面,
			List<Tache> taches = tacheService.findAllByModuleIdWithUser(m.getId());

			String createComments = ModuleController.findModuleComments(taches,statusList, ProjectStatus.STAT_NEW,true);       //未归档了环节描述
			String updateComments = ModuleController.findModuleComments(taches,statusList, ProjectStatus.STAT_SUCCESS,true);   //已经归档了环节描述

			m.setCreateCommentStr(createComments);
			m.setUpdateCommentStr(updateComments);
		}

		map.put("page",page);
		return "index";
	}


	/**前index页面*/
	@RequestMapping(value={"/errorhaha"})
	public String errorhaha(){
		if(true){
			throw new NumberFormatException("hahaha");
		}
		return "sfafsf";
	}
	/**前index页面*/
	@RequestMapping(value={"/errorhaha2"},method = RequestMethod.POST)
	public String errorhaha2(){
		if(true){
			throw new NumberFormatException("hahaha");
		}
		return "sfafsf";
	}
	/**前index页面*/
	@RequestMapping(value={"/errorhaha4"},method = RequestMethod.GET)
	@ResponseBody
	public String errorhaha4(){
		if(true){
			throw new NumberFormatException("hahaha222rest");
		}
		return "sfafsf";
	}
	/**前index页面*/
	@RequestMapping(value={"/errorhaha5"},method = RequestMethod.GET)
	@ResponseBody
	public String errorhaha5(){
		boolean flag = true;
		System.out.println(flag);
		if(flag){
			throw new NumberFormatException("hahaha222rest");
		}
		return "sfafsf";
	}

	/**前index页面*/
	@RequiresPermissions("plan:module:edit")
	@RequestMapping(value={"/errorhaha3"},method = RequestMethod.GET)
	public String errorhaha3(){
		if(true){
			throw new NumberFormatException("hahaha");
		}
		return "sfafsf";
	}
}
