package com.project.plan.entity.plan;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.project.plan.entity.User;
import com.project.plan.entity.support.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by Barry on 2018/4/20.
 * 环节
 *
 * tacheIndex 和moduleId 唯一约束
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_tache")
public class Tache  extends AbstractEntity {



//    public static final int STAT_NEW = 0;//新创建
//    public static final int STAT_DEBUG = 1;//执行中
//    public static final int STAT_TESTING = 2;//测试中
//    public static final int STAT_SUCCESS = 3;//归档完成

//    public static final String TACHE_STATUS(int stat){
//        String statStr = null;
//        switch (stat){
//            case STAT_NEW:statStr="新创建";break;
//            case STAT_DEBUG:statStr="执行中";break;
//            case STAT_TESTING:statStr="测试中";break;
//            case STAT_SUCCESS:statStr="归档完成";break;
//            default: statStr="未知";break;
//        }
//        return statStr;
//    }
    @NotNull(message="所继承项目环节id不能为空")
    @Column(name="project_tache_id",length=2,nullable = false)
    private Integer projectTacheId ;

    @NotNull(message="环节名称不能为空")
    @Column(name="name",length=500)
    private String name;

    @NotNull(message="环节名称不能为空")
    @Column(name="simple_name",length=500)// 根据 project_tache_id 表中冗余而来,此冗余为了避免过多的查询
    private String simpleName;

    @NotNull(message="环节序号不能为空")
    @Column(name="tache_index",length=2,nullable = false)
    private Integer tacheIndex ;

//    @NotNull(message="计划开始时间不能为空")
    @JSONField(format = "yyyy-MM-dd")
    @Column(name="plan_begin_time",length=500)
    protected Date planBeginTime;

//    @NotNull(message="计划结束时间不能为空")
    @JSONField(format = "yyyy-MM-dd")
    @Column(name="plan_end_time",length=500)
    protected Date planEndTime;

    //实际开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="real_begin_time",length=500)
    protected Date realBeginTime;
    //实际结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="real_end_time",length=500)
    protected Date realEndTime;

    @NotNull(message="环节状态不能为空")
    @Column(name="stat",length=2,nullable = false)
    private Integer status ;

    //归档时间
    @Deprecated //归档时间不用了， 它就是最后一个实际结束时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="archive_time",length=500)
    protected Date archiveTime;

    //@NotNull(message="责任人不能为空")
//    @Column(name="user_id",length=500)
//    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)//责任人
    @JoinColumn(name = "user_id")
    private User user;

    //组员,由责任人代领组员一起做这个活儿,不想复杂的多设计User-Tache 多对多的表啦,里面业务不多，主要是展示,就以json字符串来保存吧! eg: [{"id":2,"nikeName":"小华","id":23,"nikeName":"小明"}]
    @Column(name="group_users",length=2000)
    private String groupUsers;



    //模块
    @NotNull(message="模块不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;

//    @NotNull(message="模块id不能为空")
//    @Column(name="module_id",nullable = false)
//    private Integer moduleId ;
//    @NonNull
//    @Transient
//    private Module module;//模块,jpa到数据库没有column

    @OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REMOVE },mappedBy ="tacheId")
    //这里配置关系，并且确定关系维护端和被维护端。mappBy表示关系被维护端，只有关系端有权去更新外键。这里还有注意OneToMany默认的加载方式是赖加载。当看到设置关系中最后一个单词是Many，那么该加载默认为懒加载
    private Set<Openate> openates;


    @Transient//状态值由每次查询出来，不冗余到数据库
    private String statusName;


    public Tache(Tache tache,User user){
        this(tache,null,user);
    }

    public Tache(Tache tache,Module module,User user){
        this.user = user;
        this.module = module;
        if(tache!=null) {
            this.id = tache.getId();
            this.createTime = tache.getCreateTime();
            this.updateTime = tache.getUpdateTime();

            this.name = tache.getName();
            this.status = tache.getStatus();
            this.tacheIndex = tache.getTacheIndex();
            this.planBeginTime = tache.getPlanBeginTime();
            this.planEndTime = tache.getPlanEndTime();
            this.realBeginTime = tache.getRealBeginTime();
            this.realEndTime = tache.getRealEndTime();
            this.archiveTime = tache.getArchiveTime();

            this.groupUsers = tache.getGroupUsers();
        }
    }

    @Transient
    @NonNull
    public  List<Map<String,String>> getGroupUserList(){
        List<Map<String,String>>  list =new ArrayList<>();
        if(groupUsers!=null&&!"".equalsIgnoreCase(groupUsers)){
            return ( List<Map<String,String>>) JSONUtils.parse(groupUsers);
        }
        return list;
    }
    public static void main(String[] args){
        Map<String,String> tom =new HashMap<>();
        tom.put("id","2");
        tom.put("name","tom");

        Map<String,String> jack =new HashMap<>();
        jack.put("id","3");
        jack.put("name","jack");

        List<Object> users = new ArrayList<>();
        users.add(tom);
        users.add(jack);

        String str= JSONUtils.toJSONString(users);
        System.out.println("str: "+str);
        System.out.println();

        List<Map<String,String>> list = ( List<Map<String,String>>) JSONUtils.parse(str);

        for (int i = 0 ;i<list.size();i++) {
            Map<String,String> map = list.get(i);
            String id= map.get("id");
            String name = map.get("name");
            System.out.println("id: "+id+" name: "+name);
        }
        System.out.println("list: "+list);
    }
}
