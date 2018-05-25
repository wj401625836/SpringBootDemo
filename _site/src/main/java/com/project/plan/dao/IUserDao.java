package com.project.plan.dao;

import com.project.plan.entity.User;
import com.project.plan.dao.support.IBaseDao;

import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends IBaseDao<User, Integer> {

	User findByUserName(String username);

}
