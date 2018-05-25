package com.project.plan.controller.web;

import java.lang.invoke.MethodType;
import java.util.List;

import com.project.plan.controller.BaseController;
import com.project.plan.service.IUserService;
import com.project.plan.entity.User;

import com.project.plan.service.plan.PlanServiceImpl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController extends BaseController {


	private Logger logger = LoggerFactory.getLogger(getClass());


	/**前index页面*/
	@RequestMapping(value={"/","/index"})
	public String index(){

		return redirect("/plan/module/index");
		//return "index";
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
