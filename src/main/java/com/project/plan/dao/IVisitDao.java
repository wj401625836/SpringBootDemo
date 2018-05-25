package com.project.plan.dao;

import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.User;
import com.project.plan.entity.Visit;
import org.springframework.stereotype.Repository;

/**
 * 记录所有访问
 */
@Repository
public interface IVisitDao extends IBaseDao<Visit, Integer> {


}
