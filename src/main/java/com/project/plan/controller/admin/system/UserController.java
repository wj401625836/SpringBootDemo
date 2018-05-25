package com.project.plan.controller.admin.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.project.plan.common.JsonResult;
import com.project.plan.controller.BaseController;
import com.project.plan.entity.Role;
import com.project.plan.entity.User;
import com.project.plan.service.IRoleService;
import com.project.plan.service.IUserService;
import com.project.plan.service.specification.SimpleSpecificationBuilder;
import com.project.plan.service.specification.SpecificationOperator.Operator;

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

@Controller
@RequestMapping("/admin/user")
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;
	@Autowired
	private IRoleService roleService;

	@ApiIgnore //swagger2忽略这个接口生成文档
	@RequestMapping(value = { "","/", "/index" })
	public String index() {
		return "admin/user/index";
	}

	@ApiOperation(value="分页获取用户列表", notes="可以更具传入的searchText到用户表中模糊搜索")
	@RequestMapping(value = { "/list" },method = RequestMethod.POST)
	@ResponseBody
	public Page<User> list() {
		SimpleSpecificationBuilder<User> builder = new SimpleSpecificationBuilder<User>();
		String searchText = request.getParameter("searchText");
		if(StringUtils.isNotBlank(searchText)){
			builder.add("nickName", Operator.likeAll.name(), searchText);
		}
		Page<User> page = userService.findAll(builder.generateSpecification(), getPageRequest());
		return page;
	}

	@ApiOperation(value="跳到添加用户页面", notes="增加和修改是一个页面")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		return "admin/user/form";
	}


	@ApiOperation(value="跳到修改用户页面", notes="增加和修改是一个页面")
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		User user = userService.find(id);
		map.put("user", user);
		return "admin/user/form";
	}

	@ApiOperation(value="修改或添加用户", notes="user的属性,有id就是修改,没有id添加")
	@RequestMapping(value= {"/edit"} ,method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(User user, ModelMap map){
		try {
			userService.saveOrUpdate(user);
		} catch (Exception e) {
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
			userService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
	@ApiOperation(value="跳到给用户赋予权限页面", notes="跳到给用户赋予权限页面,获取用户,权限,所拥有的权限数据")
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
	@RequestMapping(value = "/grant/{id}", method = RequestMethod.GET)
	public String grant(@PathVariable Integer id, ModelMap map) {
		User user = userService.find(id);
		map.put("user", user);
		
		Set<Role> set = user.getRoles();
		List<Integer> roleIds = new ArrayList<Integer>();
		for (Role role : set) {
			roleIds.add(role.getId());
		}
		map.put("roleIds", roleIds);
		
		List<Role> roles = roleService.findAll();
		map.put("roles", roles);
		return "admin/user/grant";
	}

	@ApiOperation(value="给用户赋予权限", notes="重新设置用户,权限,所拥有的权限数据")
	@ResponseBody
	@RequestMapping(value = "/grant/{id}", method = RequestMethod.POST)
	public JsonResult grant(@PathVariable Integer id,String[] roleIds, ModelMap map) {
		try {
			userService.grant(id,roleIds);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
}
