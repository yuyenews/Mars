<div align=center>
<img width="150px;" src="https://github.com/yuyenews/resource/raw/master/mars/logo-small.png?raw=true">
</div>

<br/>

<div align=center>

<img src="https://img.shields.io/badge/licenes-MIT-brightgreen.svg"/>
<img src="https://img.shields.io/badge/jdk-1.8+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/maven-3.5.4+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/release-master-brightgreen.svg"/>

</div>

<br/>

<div align=center>

声明式API编程 (DAP) 框架

</div>

<div align="center">

[Mars-start Source (launcher)](https://github.com/yuyenews/Mars-start)，
[Mars-cloud Source (distributed middleware)](https://github.com/yuyenews/Mars-cloud)

</div>

## 声明式API编程（DAP）

根据传统的开发方法，如果要开发后端接口，则需要分为以下三个步骤

1. 创建控制器
2. 创建服务
3. 创建dao（甚至创建xml来存储sql）

但是，当我们编写接口时，重点应该放在业务逻辑上，这意味着我们的重点应该放在第二步上，但是通常第一步和第三步的重复工作量使我们感到窒息，因此我安排了一下，制定了一个新的开发方法，这套新的开发方法称为声明性API

1. 编写业务逻辑
2. 向前端声明API
3. 将API与业务逻辑相关联

所以我们这样玩

## 编写业务逻辑

接口
```java
public interface TestService {

    Object/*Data type to return*/ selectList(TestDTO testDTO);
}
```
实现类
```java
@MarsBean("testService")
public class TestServiceImpl implements TestService{

    Object/*Data type to return*/ selectList(TestDTO testDTO){
        
        // Writing business logic
		
        return data;//Just return directly, it will automatically become json
    }
}
```
## 在Service的父接口中添加两个注释

```java
@MarsApi
public interface TestService {

    @MarsReference(beanName = "testService")
    Object/*Data type to return*/ selectList(TestDTO testDTO);
}
```

这套思路的核心是将后端视为一个单独的实体，并将其与前端完全分开。后端编写后端业务逻辑。如果前端需要数据，那么我们声明一个接口

## 接下来做什么

看到这一点，每个人肯定会有疑问，如何在前端调用api，以及如何在后端操作数据库？这需要您动动手指并查看我的官方网站

## 我还可以做些什么

首先，声明性API是对前端和后端交互方法的更改，从而消除了对Controller的需求。

实际上，这种样式在微服务（例如Dubbo的api）中非常普遍。由于微服务接口可以使用接口来提供外部服务，因此我们也可以在http接口上使用它。

除了声明性API，我们还提供以下功能

1. 单表操作和固定sql操作，仅需要一行注释
2. 分页仅需调用一种方法，而无需任何第三方依赖项
3. 分布式锁仅需要一行注释
4. 没有像Mybatis这样的sqlMapper.xml
5. 支持AOP，IOC，声明式交易

## 官方网站

[http://mars-framework.com](http://mars-framework.com)

## 捐赠赞助

[![ko-fi](https://www.ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/G2G517AIY)

[转到官方网站赞助](http://mars-framework.com/sponsor.html)