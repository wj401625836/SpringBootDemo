package com.project.plan.controller.admin;

import com.project.plan.common.Constats;
import com.project.plan.controller.BaseController;
import com.project.plan.entity.Resource;
import com.project.plan.entity.plan.Module;
import com.project.plan.service.IResourceService;
import com.project.plan.service.plan.ModuleServiceImpl;
import com.project.plan.service.plan.TacheServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Controller
public class AdminIndexController extends BaseController {
	@Autowired
	private TacheServiceImpl tacheService;
	@Autowired
	private ModuleServiceImpl moduleService;
	@Autowired
	private IResourceService resourceService;

	/**后台index页面*/
	@ApiOperation(value="后台主界面", notes="登录成功进入后台主界面")
	@RequestMapping(value ={"/admin/","/admin/index","/admin"},method = RequestMethod.GET)
	public String index(){

		List<Resource> resourceList = (List<Resource>) request.getSession().getServletContext().getAttribute(Constats.APPLICATION_RESOURCES);
		if(resourceList==null||resourceList.isEmpty()){
			resourceList = resourceService.findAll();//启动和重新启动都查一下全局权限
			request.getSession().getServletContext().setAttribute(Constats.APPLICATION_RESOURCES,resourceList);
		}
		return "admin/index";
	}
	@ApiOperation(value="后台欢迎界面", notes="登录成功进入后台主界面后页面回立即请求这个页面,先给内容重定向到别的页面")
	@RequestMapping(value = {"/admin/welcome"},method = RequestMethod.GET)
	public String welcome(ModelMap map,Integer moduleId){
		return  redirect("/plan/module/index");
	}


}
