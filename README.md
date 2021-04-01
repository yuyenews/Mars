<div align=center>
<img width="200px;" src="http://mars-framework.com/img/logo-github.png"/>
</div>

<br/>

<div align=center>

<img src="https://img.shields.io/badge/licenes-MIT-brightgreen.svg"/>
<img src="https://img.shields.io/badge/jdk-11+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/maven-3.5.4+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/release-master-brightgreen.svg"/>

</div>

<br/>

<div align=center>

基于AIO的Web编程框架

</div>

## 项目简介

Martian 是一个基于AIO的JavaWeb编程框架，可以帮助你快速的开发后端服务。 设计理念是 极致精简，专注于前后端分离，只保留必要的功能，并且使用最简洁的方式来实现这些功能。

在 声明式API 和 DAO 的加持下，Controller没有了方法体，数据库操作变得很简洁，极大的降低了Controller和数据库操作的代码量，让用户专注在Service层

- 基于AIO，彻底脱离Tomcat，JBoss等容器
- 让Controller变成了一个interface，降低了开发的工作量
- 拥有其他web框架 拥有的大部分功能，比如AOP,IOC,声明式事务,异常监听等
- 拥有自主开发的 半ORM持久层框架，并天然的集成到了Martian中

## 项目生态

- 【分布式组件】[Martian-cloud](https://github.com/yuyenews/Martian-Cloud)
- 【网关组件】[Martian-gateway](https://github.com/yuyenews/Martian-gateway)
- 【更多组件】筹划中

## 官方资源

- 开发文档: [http://mars-framework.com/doc.html?tag=martian](http://mars-framework.com/doc.html?tag=martian)
- 视频教程: [https://www.bilibili.com/video/BV1dv4y1Z7Wt](https://www.bilibili.com/video/BV1dv4y1Z7Wt)
- 使用示例: [https://github.com/yuyenews/Mars-Example](https://github.com/yuyenews/Mars-Example)

## 运行环境

- 从3.3.x开始，最低支持JDK 11+
- 如果想用JDK8，可以自己下载源码在本地编译（随着版本的升级，会引入JDK11的特性，到那时将彻底无法在JDK11以下运行）
- 完美支持JDK8的最后一个版本是3.2.18，是NIO版本

## 项目特性
### 一、编写Http接口不需要方法体
只需要在你的interface上加上一个注解，即可对外提供一个http接口，并且我们还支持传统的Controller写法
```java
@MarsApi(refBean="要引用的bean的name")
public interface TestApi {

   返回类型 selectList(TestDTO testDTO);
}
```
### 二、单表增删改查无sql
```java
// 根据主键查询一条数据
@MarsGet(tableName = "表名称",primaryKey = "主键字段名")
public abstract 要返回的实体类 selectById(int id);

// 单表新增
@MarsUpdate(tableName = "表名称",operType = OperType.INSERT)
public abstract int insert(实体对象参数);

// 单表根据主键删除
@MarsUpdate(tableName = "表名称",operType = OperType.DELETE,primaryKey = "主键字段名")
public abstract int delete(int id);

// 单表根据主键修改
@MarsUpdate(tableName = "表名称",operType = OperType.UPDATE,primaryKey = "主键字段名")
public abstract int update(实体对象参数);
```

### 三、参数校验只需一个注解
在API接口的参数对象里的字段上加上一个注解即可（VO的字段上加注解）
```java
// 不可为空，且长度在2-3位
@MarsDataCheck(notNull = true,maxLength = 3L,minLength = 2L, msg = "id不可为空且长度必须在2-3位之间")
private Integer id;

// 正则校验
@MarsDataCheck(reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$",msg = "密码不可以为空且必须是6-12位数字字母组合")
private String password;
```

前端如何得到提示?

正常请求API就好了，如果校验不通过会得到这样一个json
```json
{"error_code":1128,"error_info":"提示文字"}
```

### 四、异常监听器
通常我们在写代码的时候，需要给每个Controller的方法加上try{}catch(){},用来在异常的时候，能够正常的返回 json串

spring是有一个叫ExecptionHandler 来解决这个问题，而Martion也提供了对应的解决方案

解决方案就是什么都不用管，如果出了异常，会自动给前端返回如下json串
```json
{"error_code":500,"error_info":"异常提示"}
```

### 五、一行注解，解决分布式锁
在要加锁的方法上添加RedisLock注解
```java
@RedisLock(key = "自己定义一个key")
public int insert(){
  return 1;
}
```
## 社区以及联系方式

- QQ群: 773291321
- 邮箱: yuyemail@163.com
- B站: [贝克街的天才](https://space.bilibili.com/41981562)
- 微博: [@Ye卡布奇诺](https://weibo.com/tcyuye?sudaref=mars-framework.com&is_all=1)
- 社区: [https://github.com/yuyenews/Martian/discussions](https://github.com/yuyenews/Martian/discussions)