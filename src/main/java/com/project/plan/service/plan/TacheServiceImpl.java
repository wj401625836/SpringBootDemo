package com.project.plan.service.plan;

import com.alibaba.druid.support.json.JSONUtils;
import com.project.plan.common.Constats;
import com.project.plan.common.utils.DateUtil;
import com.project.plan.common.utils.TextUtil;
import com.project.plan.dao.IUserDao;
import com.project.plan.dao.plan.*;
import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.User;
import com.project.plan.entity.plan.*;
import com.project.plan.service.support.impl.BaseServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Barry on 2018/4/20.
 */
@Service
public class TacheServiceImpl extends BaseServiceImpl<Tache,Integer> {
    @Autowired
    private ITacheDao tacheDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IOpenateDao openateDao;

    @Autowired
    private ITacheUserDao tacheUserDao;

    @Autowired
    private IProjectTacheDao projectTacheDao;

    @Autowired
    private IProjectStatusDao statusDao;

    @Override
    public IBaseDao<Tache, Integer> getBaseDao() {
        return this.tacheDao;
    }


    public void saveOrUpdate(Tache tache) {
        if(tache.getId() != null){
            Date sysDate = new Date();

            Tache dbTache = super.find(tache.getId());

//            Module m = new Module();//环节修改模块,不让修改
//            m.setId(tache.getModule().getId());
//            dbTache.setModule(m);
//            if (dbTache.getStatus()==null&&Tache.STAT_SUCCESS == tache.getStatus() ){//直接改成归档
//                dbTache.setArchiveTime(sysDate);
//            }else if(dbTache.getStatus()==null && !dbTache.getStatus().equals(tache.getStatus()) &&Tache.STAT_SUCCESS == tache.getStatus() ){//其他状态改成已归档
//                dbTache.setArchiveTime(sysDate);
//            }

            dbTache.setPlanBeginTime(tache.getPlanBeginTime());
            dbTache.setPlanEndTime(tache.getPlanEndTime());
            dbTache.setRealBeginTime(tache.getRealBeginTime());
            dbTache.setRealEndTime(tache.getRealEndTime());
            dbTache.setStatus(tache.getStatus());
            dbTache.setArchiveTime(tache.getArchiveTime());

            dbTache.setUpdateComment(tache.getUpdateComment());
            dbTache.setUpdateTime(sysDate);
            dbTache.setGroupUsers(tache.getGroupUsers());

            if(tache.getUser()!=null&&tache.getUser().getId()!=null){
                User u = new User();//修改负责人
                u.setId(tache.getUser().getId());
                dbTache.setUser(u);
            }else{
                dbTache.setUser(null);
            }

            super.update(dbTache);
        }else{
            tache.setCreateTime(new Date());
            tache.setUpdateTime(new Date());
            save(tache);
        }
    }


    /**
     * 每个模块都有12个环节,添加模块为其添加12个环节记录
     * ,这里是用的是配好的12个环节的做法,弃用,后面要改用更具数据库表中动态配置的来做
     * <br/>
     * 弃用,弃用,弃用
     * @param module
     * @see #saveTache(Module, Integer[])
     */
//    @Deprecated
//    public void saveTache(Module module) {
//        List<Tache> tacheList = new ArrayList<>();
//
//        Date date = new Date();
//        for(int i=0 ; i<Constats.TACHE_INDEX_NAMES.length;i++ ){
//            Tache t = new Tache();
//            t.setTacheIndex(i+1);
//            t.setName(Constats.TACHE_INDEX_NAMES[i]);
//            t.setStatus(Tache.STAT_NEW);
//            t.setModule(module);
//
//            t.setPlanBeginTime(module.getStartTime());//环节的计划开始时间和计划结束时间先默认功能模块的计划时间,后续给其修改
//            t.setPlanEndTime(module.getWishTime());
//
//            t.setCreateTime(date);
//
//            tacheList.add(t);
//        }
//        tacheDao.save(tacheList);
//    }
    public void saveTache(Module module, Integer[] haveTacheIds) {
        List<Tache> tacheList = new ArrayList<>();

        List<ProjectTache> projectTacheList = projectTacheDao.findAll(Arrays.asList(haveTacheIds));
//        tacheDao.findAll(haveTacheIds);
        Date date = new Date();
        for(int i=0 ; i<projectTacheList.size();i++ ){
            ProjectTache pt = projectTacheList.get(i);

            Tache t = new Tache();
            t.setTacheIndex(i+1);
            t.setProjectTacheId(pt.getId());
            t.setName(pt.getName());
            t.setSimpleName(pt.getSimpleName());
//            t.setStatus(Tache.STAT_NEW);//换成取第一个状态
            String[] haveStatusList =pt.getHaveStatus()==null?new String[0]:pt.getHaveStatus().split(",");
            if(haveStatusList!=null&&haveStatusList.length>0){
                String statusStr = haveStatusList[0];//取第一个状态
                if(!TextUtil.isNullOrEmpty(statusStr)){
                    t.setStatus(Integer.valueOf(statusStr));
                }
            }else{
                t.setStatus(0);//设置为未知
            }

            t.setModule(module);

            t.setPlanBeginTime(module.getStartTime());//环节的计划开始时间和计划结束时间先默认功能模块的计划时间,后续给其修改
            t.setPlanEndTime(module.getWishTime());

            t.setCreateTime(date);

            tacheList.add(t);
        }
        tacheDao.save(tacheList);
    }

    @Transactional
    public void editTache(Tache tache,List<Integer> groupUserIds, HttpServletRequest request) {

        Date sysDate = new Date();
        if(tache.getId()!=null){//是修改添加修改了什么的记录
            Tache dbTache = tacheDao.findOne(tache.getId());
            //环节中的修改需要记录谁什么时候编辑了什么做了哪些改动,并将第一个修改的人做为这个环节的责任人
            Openate log =new Openate();
            StringBuffer requestUrl = request.getRequestURL();
            String remoteAddr = request.getRemoteAddr();
            log.setIp(remoteAddr);
            log.setUrl(requestUrl.toString());
            log.setCreateTime(sysDate);
            log.setStatus(Openate.STATUS_WARN);
            log.setDuration(0L);

            List<User> groupUsers = groupUserIds.size()==0?new ArrayList<>():userDao.findByIds(groupUserIds);




            String createComment = compateUpdateComment(dbTache,tache,groupUsers);
            log.setCreateComment(createComment);

            List<Object> users = new ArrayList<>();
            for (User u:groupUsers) {
                Map<String,String> userMap = new HashMap<>();
                userMap.put("id",u.getId()+"");
                userMap.put("nickName",u.getNickName());

                users.add(userMap);
            }
            String str= JSONUtils.toJSONString(users);
            tache.setGroupUsers(str);//设置小组成员


//                Tache t = new Tache();
//                t.setId(tache.getId());
//                log.setTache(m);
            log.setTacheId(tache.getId());

            User loginUser = (User) SecurityUtils.getSubject().getSession()
                    .getAttribute(Constats.CURRENTUSER);
            log.setUserId(loginUser.getId());
            openateDao.save(log);
        }
        saveOrUpdate(tache);
    }


    /**
     * 计算 将 tache修改成 dbTache修改了哪些内容,并将修改内容记录下来
     * @param dbTache
     * @param tache
     * @return
     */
    private String compateUpdateComment(Tache dbTache, Tache tache,List<User> userList) {
        StringBuffer sb = new StringBuffer();
        if(dbTache==null||tache==null){
            throw new RuntimeException("修改的环节或要修改的环节为空!!!");
        }


        //System.out.println("懒加载 user对象 "+dbTache.getUser().getId());
        Map<Integer,String> addGroupUser = new HashMap<>();//新加的小组成员
        Map<Integer,String> removeGroupUser = new HashMap<>();//去掉的小组成员
        List<Map<String, String>> groupList = dbTache.getGroupUserList();
        Map<Integer,User> dbUsers = new HashMap<>();
        Map<Integer,User> groupUsers = new HashMap<>();
        for (Map<String, String> map:groupList) {
            String idStr = map.get("id");
            Integer id  = Integer.valueOf(idStr);
            String name = map.get("nickName");
            User u = new User();
            u.setId(id);
            u.setNickName(name);

            dbUsers.put(u.getId(),u);
        }
        for (User u:userList) {
            groupUsers.put(u.getId(),u);
        }

        Map<Integer,User> allUsers = new HashMap<>();
        allUsers.putAll(dbUsers);
        allUsers.putAll(groupUsers);

        for (Integer id:allUsers.keySet()) {
            boolean dbflag = dbUsers.containsKey(id);
            boolean newflag = groupUsers.containsKey(id);
            User user = allUsers.get(id);
            if(dbflag&&newflag){//修改之前有，修改之后也有,他不变
                continue;
            }else if(dbflag==true && newflag ==false ){//他已经不再这个小组
                removeGroupUser.put(user.getId(),user.getNickName());
            }else if(dbflag == false && newflag == true){//他新加入这个小组
                addGroupUser.put(user.getId(),user.getNickName());
            }
        }
        if(!addGroupUser.isEmpty()){
            sb.append(" 添加小组成员: ");
            for (Integer id: addGroupUser.keySet()) {
                String nickName = addGroupUser.get(id);
                sb.append(nickName+"、");
            }
            sb.append("。");
        }
        if(!removeGroupUser.isEmpty()){
            sb.append(" 移除小组成员: ");
            for (Integer id: removeGroupUser.keySet()) {
                String nickName = removeGroupUser.get(id);
                sb.append(nickName+"、");
            }
            sb.append("。");
        }




        Date sysDate = Calendar.getInstance().getTime();
        if(dbTache.getUser()==null&&tache.getUser()==null){//没有选择负责人,默认修改人

        }else if(dbTache.getUser()==null
                &&tache.getUser()!=null&&tache.getUser().getId()!=null){//添加负责人
            User u = userDao.findOne(tache.getUser().getId());
            sb.append(" 添加责任人为 “"+u.getNickName()+"”");
            tache.setRealBeginTime(sysDate);
            tache.setRealEndTime(null);

            TacheUser tu = new TacheUser(dbTache.getName()+"_"+u.getNickName(),dbTache,u,sysDate,null);//记录新的负责人记录
            tu.setCreateTime(sysDate);
            tacheUserDao.save(tu);
        }else if(dbTache.getUser() != null&&dbTache.getUser().getId()!=null
                &&(tache.getUser() == null||tache.getUser().getId()==null)){//修改负责人为空
            sb.append(" 将责任人由“"+dbTache.getUser().getNickName()+"” 改为空");
            tache.setRealBeginTime(sysDate);
            tache.setRealEndTime(null);

            updateLastEndTime(dbTache.getId(),dbTache.getUser().getId(), sysDate);

        }else if(dbTache.getUser()!=null&&tache.getUser() !=null &&
                dbTache.getUser().getId()!=null&&tache.getUser().getId()!=null&&
                !dbTache.getUser().getId().equals(tache.getUser().getId())){
            User u = userDao.findOne(tache.getUser().getId());
            sb.append(" 将责任人由“"+dbTache.getUser().getNickName()+"” 改为 “"+u.getNickName()+"”");
            tache.setRealBeginTime(sysDate);
            tache.setRealEndTime(null);

            updateLastEndTime(dbTache.getId(),dbTache.getUser().getId(), sysDate);

            TacheUser tu2 = new TacheUser(dbTache.getName()+"_"+u.getNickName(),dbTache,u,sysDate,null);//记录新的负责人记录
            tu2.setCreateTime(sysDate);
            tacheUserDao.save(tu2);
        }else{//没有修改不记录
            System.out.println("dbTache: "+dbTache+"  ---> "+tache);
        }

       /* if(dbTache.getStatus().intValue()!=tache.getStatus().intValue()){//修改环节状态
            String dbStatus = Tache.TACHE_STATUS(dbTache.getStatus());
            String status = Tache.TACHE_STATUS(tache.getStatus());
            sb.append(" 将状态由“"+dbStatus+"”改成“"+status+"”");

            if(Tache.STAT_SUCCESS==tache.getStatus()&&Tache.STAT_SUCCESS!=dbTache.getStatus()){//状态是已归档,并且之前不是已归档,
                tache.setArchiveTime(sysDate);
                tache.setRealEndTime(sysDate);
                updateLastEndTime(dbTache.getId(),dbTache.getUser().getId(), sysDate);
            }
        }*/
        if(dbTache.getStatus().intValue()!=tache.getStatus().intValue()){//修改环节状态
            ProjectStatus dbStatus = statusDao.findOne(dbTache.getStatus());
            ProjectStatus newStatus = statusDao.findOne(tache.getStatus());
            if(dbStatus == null){
                dbStatus = new ProjectStatus();
                dbStatus.setId(0);
                dbStatus.setName("未知");
            }
            if(newStatus == null){
                newStatus = new ProjectStatus();
                newStatus.setId(0);
                newStatus.setName("未知");
            }

            sb.append(" 将状态由“"+dbStatus.getName()+"”改成“"+newStatus.getName()+"”");

            if(ProjectStatus.STAT_SUCCESS==newStatus.getStatus()&& ProjectStatus.STAT_SUCCESS!=dbStatus.getStatus()){//状态是已归档,并且之前不是已归档,
//                tache.setArchiveTime(sysDate);
                tache.setRealEndTime(sysDate);
                updateLastEndTime(dbTache.getId(),dbTache.getUser().getId(), sysDate);
            }
        }

        if(TextUtil.isNullOrEmpty(dbTache.getUpdateComment())
                &&!TextUtil.isNullOrEmpty(tache.getUpdateComment())){
            //原来没有修改描述,新加修改描述
            sb.append(" 添加修改描述为:“"+tache.getUpdateComment()+"”");
        }else if(!TextUtil.isNullOrEmpty(dbTache.getUpdateComment())
                &&TextUtil.isNullOrEmpty(tache.getUpdateComment())){
            sb.append(" 删除为:“"+tache.getUpdateComment()+"”的修改描述");
        }else if(!TextUtil.isNullOrEmpty(dbTache.getUpdateComment())
                &&!TextUtil.isNullOrEmpty(tache.getUpdateComment())
                &&!dbTache.getUpdateComment().trim().equalsIgnoreCase(tache.getUpdateComment())){//修改备注
            sb.append(" 将备注从 “"+dbTache.getUpdateComment()+"”修改为“"+tache.getUpdateComment()+"”");
        }else{}//还有一种空修改成空,不处理

        String appenStr = " - ";
        String dbPlanTimeStr =  DateUtil.format(dbTache.getPlanBeginTime(),DateUtil.DEF_DATE_PATTERN,"")+appenStr+ DateUtil.format(dbTache.getPlanEndTime(),DateUtil.DEF_DATE_PATTERN,"");
        String planTimeStr =  DateUtil.format(tache.getPlanBeginTime(),DateUtil.DEF_DATE_PATTERN,"")+appenStr+ DateUtil.format(tache.getPlanEndTime(),DateUtil.DEF_DATE_PATTERN,"");
        if(appenStr.equals(dbPlanTimeStr)&&!appenStr.equals(planTimeStr)){//添加
            sb.append(" 添加计划时间为:“"+planTimeStr+"”！");
        }else if(!appenStr.equals(dbPlanTimeStr)&&appenStr.equals(planTimeStr)){//删除
            sb.append(" 删除为“"+dbPlanTimeStr+"”的计划时间！");
        }else if(!dbPlanTimeStr.equalsIgnoreCase(planTimeStr)){
            sb.append(" 将计划时间从 “"+dbPlanTimeStr+"”修改为“"+planTimeStr+"”");
        }else{}//还有一种空修改成空,不处理

        String dbRealTimeStr =  DateUtil.format(dbTache.getRealBeginTime(),DateUtil.DEF_DATE_PATTERN,"")+appenStr+ DateUtil.format(dbTache.getRealEndTime(),DateUtil.DEF_DATE_PATTERN,"");
        String realTimeStr =  DateUtil.format(tache.getRealBeginTime(),DateUtil.DEF_DATE_PATTERN,"")+appenStr+ DateUtil.format(tache.getRealEndTime(),DateUtil.DEF_DATE_PATTERN,"");
        if(appenStr.equals(dbRealTimeStr)&&!appenStr.equals(realTimeStr)){//添加
            sb.append(" 添加实际时间为:“"+planTimeStr+"”！");
        }else if(!appenStr.equals(dbRealTimeStr)&&appenStr.equals(realTimeStr)){//删除
            sb.append(" 删除为“"+dbRealTimeStr+"”的实际时间！");
        }else if(!dbRealTimeStr.equalsIgnoreCase(realTimeStr)){
            sb.append(" 将实际时间从 “"+dbRealTimeStr+"”修改为“"+realTimeStr+"”");
        }else{}//还有一种空修改成空,不处理
        return sb.toString();
    }

    /**
     * 修改这个环节下这个人的最后一次操作的结束时间为指定时间
     * @param tacheId
     * @param userId
     * @param sysDate
     */
    private void updateLastEndTime(Integer tacheId,Integer userId, Date sysDate) {
        List<TacheUser> tuList= tacheUserDao.findTacheLastUser(tacheId,userId);//将之前那个记录结束掉
        if(tuList!=null&&tuList.size()>0){
            TacheUser tu = tuList.get(0);
            tu.setEndTime(sysDate);
            tu.setUpdateTime(sysDate);
            tacheUserDao.save(tu);
        }
    }

    //未上线模块功能分布列表
//    public Map<String, Long> selectTypeMap() {
//        Map<String,Long> typeMap = new LinkedHashMap<>();
//        long count = tacheDao.count();
//        typeMap.put("全部",count);
//        Integer[] statusDoing = new Integer[]{Tache.STAT_DEBUG,Tache.STAT_TESTING};//正在执行
//        Integer[] statusSuccess = new Integer[]{Tache.STAT_SUCCESS};//正在已经归档
//
//        long count1 = tacheDao.countByIndex(statusDoing,Constats.getTacheIndexByType(Constats.TACHE_TYPE_NAMES[0]));//这里的数字是 Constats.TACHE_INDEX_NAMES 里面的下标
//        typeMap.put(Constats.TACHE_TYPE_NAMES[0],count1);
//
//        long count2 = tacheDao.countByIndex(statusDoing,Constats.getTacheIndexByType(Constats.TACHE_TYPE_NAMES[1]));
//        typeMap.put(Constats.TACHE_TYPE_NAMES[1],count2);
//
//        long count3 = tacheDao.countByIndex(statusDoing,Constats.getTacheIndexByType(Constats.TACHE_TYPE_NAMES[2]));
//        typeMap.put(Constats.TACHE_TYPE_NAMES[2],count3);
//
//        long count4 = tacheDao.countByIndex(statusDoing,Constats.getTacheIndexByType(Constats.TACHE_TYPE_NAMES[3]));
//        typeMap.put(Constats.TACHE_TYPE_NAMES[3],count4);
//
//        long count5 = tacheDao.countByIndex(statusDoing,Constats.getTacheIndexByType(Constats.TACHE_TYPE_NAMES[4]));
//        typeMap.put(Constats.TACHE_TYPE_NAMES[4],count5);
//
//        long count6 = tacheDao.countByIndex(statusDoing,Constats.getTacheIndexByType(Constats.TACHE_TYPE_NAMES[5]));
//        typeMap.put(Constats.TACHE_TYPE_NAMES[5],count6);
//
//        long count7 = tacheDao.countByIndex(statusSuccess,Constats.getTacheIndexByType(Constats.TACHE_TYPE_NAMES[6]));//12个环节中已经归档的
//        typeMap.put(Constats.TACHE_TYPE_NAMES[6],count7);
//
//        return typeMap;
//    }

    public List<Tache> findAllByModuleIdWithUser(Integer moduleId) {

        return tacheDao.findAllByModuleIdWithUser(moduleId);

//        PageRequest pageRequest = new PageRequest(0, 100);//一个模块下面不会有100个环节的,查询出来的数据大于100算我错
//        Page<Tache> page = tacheDao.findAll(new Specification<Tache>() {
//            @Override
//            public Predicate toPredicate(Root<Tache> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> whereList = new ArrayList<>();
//                if (moduleId != null && moduleId > 0) {
//                    Join<Module, Tache> join = root.join("module", JoinType.INNER);
//                    Path<Integer> id = join.get("id");
//                    Predicate p = cb.equal(id, moduleId);
//                    whereList.add(p);
//                }
//                return query.where(whereList.toArray(new Predicate[whereList.size()])).getRestriction();
//            }
//        },pageRequest);
//       return page.getContent();
    }

    public void saveOneTest(Integer mid) {
        Tache dbTache = new Tache();

        Date sysDate = new Date();


        dbTache.setArchiveTime(sysDate);
        dbTache.setPlanBeginTime(sysDate);
        dbTache.setPlanEndTime(sysDate);
        dbTache.setRealBeginTime(sysDate);
        dbTache.setRealEndTime(sysDate);
        dbTache.setStatus(0);
        dbTache.setArchiveTime(sysDate);
        dbTache.setCreateTime(sysDate);

        dbTache.setUpdateComment("sdfsf");
        dbTache.setUpdateTime(sysDate);

        Module m = new Module();
        m.setId(mid+10);
        dbTache.setModule(m);

        try{

            tacheDao.save(dbTache);
        }catch (Exception e){
            System.out.println(" 里面catch ");
        }


    }


}
