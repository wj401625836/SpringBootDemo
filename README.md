# 项目跟踪工具

>  为了跟踪项目开发处于哪个环节设计的跟踪系统。



------

# 环境

* jdk1.8.0_161 + Intellij IDEA 15.0.1  +Maven3.2.5
* springboot + freemarker + jpa 
  * springboot 1.5.9.RELEASE 
  * freemarker 
  * jpa  
  * shiro 1.2.4 
  * druid 1.1.6
  * swagger
  * spring-boot-maven-plugin 


# 产品设计

> 产品设计两张图



![Aaron Swartz](/images/product_01.png))

![Aaron Swartz](/images/product_02.png)



# 程序介绍

>  系统管理后台基本框架SPPanAdmin，包括用户管理，角色管理，资源链接管理模块，可以动态分配权限和角色。

* 使用springboot、springdata jpa、shiro等服务端技术，使用freemarker模版渲染页面。

* 系统中对springdata的查询条件Specification做了简单的封装。表单验证使用jQuery validate插件等等。

* 系统部署：
  1.使用mysql数据库，先建立一个空数据库project_plan，最好编码使用utf-8字符集，不然会乱码。

  2.把application.properties中的数据库连接信息修改成自己数据库的连接信息。

  3.修改spring.jpa.hibernate.ddl-auto为create，目的是让系统自动建表同时初始化相关集成数据。如果不需要自动初始化数据，可以删除resource目录下的import.sql文件。


* 系统启动后，访问：127.0.0.1/admin/会自动跳转到后台登录页面。
* 初始用户名和密码为：admin/111111。
* 演示图片：





# 程序设计

> 

## 表



## 功能

