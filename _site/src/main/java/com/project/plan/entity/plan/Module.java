package com.project.plan.entity.plan;

import com.alibaba.fastjson.annotation.JSONField;
import com.project.plan.entity.support.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Barry on 2018/4/20.
 * 功能模块
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_module")
public class Module extends AbstractEntity{

    public static final int STAT_DEFAULT = 0;//正常执行中
    public static final int STAT_UNUSUAL = 1;//异常停止
    public static final int STAT_SUCCESS = 2;//已上线


    @NotNull(message="模块名称不能为空")
    @Column(name="name",length=500)
    private String name;

    @NotNull(message="启动时间不能为空")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="start_time",length=500)
    protected Date startTime;

    @NotNull(message="期望上线时间不能为空")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="wish_time",length=500)
    protected Date wishTime;


    @NotNull(message="模块状态不能为空")
    @Column(name="stat",length=2,nullable = false)
    private Integer status ;


    /*@NotNull(message="模块状态不能为空")
    @Column(name="project_id",length=20,nullable = false)
    private Integer projectId ;
    @NonNull
    @Transient// 不是表字段
    private Project project;*/

    //项目 //多对多没配好 用两个属性代替 :projectId project
    @ManyToOne(cascade = { CascadeType.REFRESH },fetch = FetchType.EAGER)
//  @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.EAGER,optional=false)
    @JoinColumn(name = "project_id")
    private Project project;


    //创建描述字符串,由计算出的未归档描述带数据到页面展示
    @NonNull
    @Transient
    protected String createCommentStr;
    //修改描述字符串,由计算出的已经归档描述带数据到页面展示
    @NonNull
    @Transient
    protected String updateCommentStr;


}
