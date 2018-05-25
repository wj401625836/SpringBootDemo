package com.project.plan.service.impl;

import com.project.plan.common.utils.MD5Utils;
import com.project.plan.dao.IUserDao;
import com.project.plan.dao.IVisitDao;
import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.Role;
import com.project.plan.entity.User;
import com.project.plan.entity.Visit;
import com.project.plan.service.IRoleService;
import com.project.plan.service.IUserService;
import com.project.plan.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 访问记录表
 * </p>
 */
@Service
public class VisitServiceImpl extends BaseServiceImpl<Visit, Integer>  {

	@Autowired
	private IVisitDao visitDao;
	
	@Override
	public IBaseDao<Visit, Integer> getBaseDao() {
		return this.visitDao;
	}


}
