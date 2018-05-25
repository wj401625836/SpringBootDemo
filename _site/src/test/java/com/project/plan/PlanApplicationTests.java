package com.project.plan;

import com.project.plan.common.utils.LDAPControl;
import com.project.plan.common.utils.TUser;
import com.project.plan.common.utils.TextUtil;
import com.project.plan.entity.User;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.Tache;
import com.project.plan.service.impl.UserServiceImpl;
import com.project.plan.service.plan.ModuleServiceImpl;
import com.project.plan.service.plan.TacheServiceImpl;
import com.project.plan.service.specification.SimpleSpecificationBuilder;
import com.project.plan.service.specification.SpecificationOperator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.*;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanApplicationTests {

	@Autowired
    private UserServiceImpl userService;

	@Autowired
	private ModuleServiceImpl moduleService;

	@Autowired
	private TacheServiceImpl tacheService;
	@Test
	public void contextLoads() {
		System.out.println("xxxxxxxxxxxxxx load test ........");
	}


	//测试同步公司ldap域下的用户插入到系统  tb_user 表中
	@Test
	public void syncLdapUsers(){
		System.out.println("getAllusers:");
		List<TUser> users = LDAPControl.getInstance().getAllLadpUser();

		List<User> userList = new ArrayList<>();
		for(int i=0;i<users.size();i++){
			TUser ldUser = users.get(i);
			System.out.println(ldUser);
			User sysUser = TUser.parseLdapUserToUser(ldUser);
			userList.add(sysUser);
		}
		System.out.print("----插入用户-----"+userService);
		userService.save(userList);

	}

	@Test
	public void testSelect(){
		PageRequest pageRequest =new PageRequest(0,10);
		SimpleSpecificationBuilder<Module> builder = new SimpleSpecificationBuilder<Module>();

		Page<Module> page =  moduleService.findAllWithProject(builder.generateSpecification(),pageRequest);

		System.out.println("-------------------------");
		for (Module m: page.getContent()) {
			System.out.println(m);
		}


	}
	public static void main(String[] args){
		long a = 107374182401L;//100*1024*1024*1024 =107374182400L
		long b= a/1024/1024/1024;
		System.out.println("b="+b);
		double h= ((double)a)/1024/1024/1024;
		System.out.println("h="+h);
	}

	@Test
	public void test2(){
		Map<String,Long> map = tacheService.selectTypeMap();
		for (Map.Entry<String, Long> entry : map.entrySet()) {
			System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
		}

	}
	@Test
	public  void test3(){
		final Integer moduleId = null;
		final String name = "";
		PageRequest pageRequest = new PageRequest(0, 500);

//		SimpleSpecificationBuilder<Tache> builder = new SimpleSpecificationBuilder<Tache>();
//		String searchText = null;
//		if(StringUtils.isNotBlank(searchText)){
//			builder.add("name", SpecificationOperator.Operator.likeAll.name(), searchText);
////            builder.add("name", SpecificationOperator.Join.values(), searchText);
//		}
//		if(moduleId != null&& moduleId > 0){//只查询这个module下的 tache
//			builder.add("moduleId",SpecificationOperator.Join.and.name(),moduleId);
//		}

//		Page<Tache> page = tacheService.findAll(builder.generateSpecification(),pageRequest);
//		System.out.println("page.getSize():   "+page.getSize());
		Page<Tache> page2 = tacheService.findAll(new Specification<Tache>() {
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
				return query.where(whereList.toArray(new Predicate[whereList.size()])).getRestriction();
			}
		}, pageRequest);
//		page = tacheService.findPage();
		System.out.println("page.getContent.size():   "+page2.getContent().size());

//		for (int i=0;i<page2.getContent().size();i++) {
//			Tache t = page2.getContent().get(i);
//			System.out.println(i+" --- "+t);
//		}
	}

	//测试事务  第一个service 调用第二个service 第二个报错怎么处理
	@Test
	public void testTrans(){
		moduleService.saveOneTest();


	}
}
