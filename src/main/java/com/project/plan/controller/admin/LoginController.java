package com.project.plan.controller.admin;

import com.project.plan.common.Constats;
import com.project.plan.common.utils.LDAPControl;
import com.project.plan.common.utils.TUser;
import com.project.plan.controller.BaseController;

import com.project.plan.entity.User;
import com.project.plan.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class LoginController extends BaseController {

	@Autowired
	private UserServiceImpl userService;

	@ApiIgnore
	@RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
	public String login() {

		return "admin/login";
	}

	@ApiOperation(value="获图书细信息", notes="根据url的id来获取详细信息")
	@ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
	@RequestMapping(value = { "/admin/login" }, method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,ModelMap model
			) {
		try {

			if(LDAPControl.getInstance().authorCheck(username,password)){//如ldap到与登录成功,让他以数据库里面设置的默认密码登录
				password = Constats.DEFAULT_USER_PWD;
				User user = userService.findByUserName(username);
				if(user==null){//如登录成功系统没有这个人的账号,ldap查询自动添加到 User列表
					TUser ldapUser =  LDAPControl.getInstance().getLadpUser(username);
					User newUser = TUser.parseLdapUserToUser(ldapUser);
					if(newUser!=null){
						userService.save(newUser);
					}
				}
			}


			 Subject subject = SecurityUtils.getSubject();
			 UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
			return redirect("/admin/index");
		} catch (AuthenticationException e) {
			model.put("message", e.getMessage());
		}
		return "admin/login";
	}

	@ApiOperation(value="退出登录", notes="shiro退出登录,shiro session清空数据")
	@RequestMapping(value = { "/admin/logout" }, method = RequestMethod.GET)
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		SecurityUtils.getSubject().getSession().removeAttribute(Constats.CURRENTUSER);
		return redirect("admin/login");
	}
	
}
