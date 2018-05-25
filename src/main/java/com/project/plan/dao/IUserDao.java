package com.project.plan.dao;

import com.project.plan.entity.User;
import com.project.plan.dao.support.IBaseDao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

	User findByUserName(String username);

	@Query("from User where lastLoginTime is not null ")
	List<User> findAllLoginedUser();

	@Query("from User where id in(:ids)")
	List<User> findByIds(@Param("ids") List<Integer> ids);
}
