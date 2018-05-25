package com.project.plan.controller.admin.system;

import java.util.List;

import com.project.plan.controller.BaseController;
import com.project.plan.service.IResourceService;
import com.project.plan.vo.ZtreeView;
import com.project.plan.common.JsonResult;
import com.project.plan.entity.Resource;
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
@RequestMapping("/admin/resource")
public class ResourceController extends BaseController {
	@Autowired
	private IResourceService resourceService;


	@RequestMapping(value = "/tree/{resourceId}",method = RequestMethod.GET)
	@ResponseBody
	public List<ZtreeView> tree(@PathVariable Integer resourceId){
		List<ZtreeView> list = resourceService.tree(resourceId);
		return list;
	}

	@ApiIgnore
	@RequestMapping(value = { "","/", "/index" })
	public String index() {
		return "admin/resource/index";
	}

	@ApiOperation(value = "权限列表",notes = "分页获取,可用searchText查询")
	@RequestMapping(value = "/list",method = RequestMethod.GET)
	@ResponseBody
	public Page<Resource> list() {
		SimpleSpecificationBuilder<Resource> builder = new SimpleSpecificationBuilder<Resource>();
		String searchText = request.getParameter("searchText");
		if(StringUtils.isNotBlank(searchText)){
			builder.add("name", Operator.likeAll.name(), searchText);
		}
		Page<Resource> page = resourceService.findAll(builder.generateSpecification(),getPageRequest());
		return page;
	}

	@ApiOperation(value="跳到添加用户页面", notes="用户增加和修改是一个页面")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		List<Resource> list = resourceService.findAll();
		map.put("list", list);
		return "admin/resource/form";
	}

	@ApiOperation(value="跳到修改权限页面", notes="用户增加和修改是一个页面")
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		Resource resource = resourceService.find(id);
		map.put("resource", resource);
		
		List<Resource> list = resourceService.findAll();
		map.put("list", list);
		return "admin/resource/form";
	}

	@ApiOperation(value="修改或添加权限", notes="有id就是修改,没有id添加")
	@RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(Resource resource,ModelMap map){
		try {
			resourceService.saveOrUpdate(resource);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@ApiOperation(value="删除权限", notes="根据用户id删除权限")
	@ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer",paramType = "path")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id,ModelMap map) {
		try {
			resourceService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
}
