package com.project.plan.entity.plan;

import com.alibaba.fastjson.annotation.JSONField;
import com.project.plan.entity.User;
import com.project.plan.entity.support.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Barry on 2018/5/9.
 * 记录这个环节由谁做坐了多久，(有可能一个环节有多个人做的情况)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
@Entity
@Table(name = "t_tache_user")
public class TacheUser extends AbstractEntity {

    @NotNull(message="名称不能为空")
    @Column(name="name",length=500)
    private String name;

    @NotNull(message="环节不能为空")
    @ManyToOne(fetch = FetchType.LAZY)//环节
    @JoinColumn(name = "tache_id")
    private Tache tache;

    @NotNull(message="操作人不能为空")
    @ManyToOne(fetch = FetchType.LAZY)//责任人
    @JoinColumn(name = "user_id")
    private User user;

    //实际开始时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="begin_time",length=500)
    private Date beginTime;

    //实际开始时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name="end_time",length=500)
    private Date endTime;


}
