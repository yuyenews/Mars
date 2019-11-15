<div align=center>
<img width="150px" src="https://github.com/yuyenews/resource/blob/master/mars/logo-small.png?raw=true"/>
</div>

<br/>

<div align=center>

<img src="https://img.shields.io/badge/licenes-MIT-brightgreen.svg"/>
<img src="https://img.shields.io/badge/jdk-1.8+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/maven-3.5.4+-brightgreen.svg"/>
<img src="https://img.shields.io/badge/release-master-brightgreen.svg"/>
<img src="https://img.shields.io/badge/release-3.0.5-brightgreen.svg"/>

</div>

<br/>

<div align=center>

声明式API编程(DAP) 框架

</div>

<div align="center">

[Mars-start源码(启动器)](https://github.com/yuyenews/Mars-start)，
[Mars-cloud源码(分布式中间件)](https://github.com/yuyenews/Mars-cloud)

</div>

## 面向声明式API编程(DAP)

按照传统的开发方式，如果要开发一个后端接口，需要分为以下三步

1. 创建controller
2. 创建service
3. 创建dao（甚至会创建存放sql的xml）

但是，我们编写一个接口，重点应该是放在业务逻辑上的，也就是说 我们的关注点应该在第二步，但是通常第一步和第三步的重复工作量让我们感到窒息，所以我梳理了一下，制定了一个新玩法，这套新玩法就叫声明式API

1. 写业务逻辑
2. 声明一个API给前端
3. 将API与业务逻辑关联

所以我们是这样玩的

## 编写业务逻辑

```
@MarsBean("testService")
public class TestService {

	要返回的数据类型 selectListForName(参数1, 参数2, 参数3, 参数4){
		// 第一步 根据testDTO里的参数从xx表查询需要的数据
		// 第二步 根据查出来的数据，去操作xx2表
		// 第三步 对前两步的结果汇总，进行xxx操作
		
		return 数据（直接返回即可，会自动变成json）；
	}
}
```
## 声明一个API接口

```
@MarsApi
public interface TestApi {

    Object selectList(TestVO testVO);
}
```

## 将api与业务逻辑关联

```
@MarsApi
public interface TestApi {
		
	@MarsReference(beanName = "testService",refName = "selectListForName")
    Object selectList(TestVO testVO);
}
```

对，你没看错，API的方法和他引用的那个方法，参数列表可以不一样，
框架会自动把API的参数的值赋值到引用的那个方法的参数列表

引用的方法甚至可以无参，参数目前只支持自定义对象和Map

这套思想的核心是，把后端看作是一个独立个体，并不是为服务前端而存在的，
后端就写后端的业务逻辑好了，如果前端需要数据，那我们就开个门给他

## 这么做的好处，还可以散藕

- 通过更换MarsReference的配置，可以关联到不同的业务逻辑
- 如果前端不需要这个接口了，直接无脑删就好了，因为这只是一个抽象方法
- 后端专注业务逻辑就好了，不需要考虑跟前端互动，前端需要的时候开个门就好了

## 接下来怎么做

看到这里，大家肯定会有疑问，前端要怎么调用api，后端怎么操作数据库？ 这个就需要你们动动手指，去我的官网一探究竟

## 还可以做什么

首先声明式API是对前后端交互方式的一次变革，省去了Controller。

其实这种风格在微服务里很常见，比如Dubbo的api，
既然微服务接口可以用interface来提供对外的服务，那么我们也可以把它用到http接口上吧。

很多java初学者在刚接触分层架构的时候，肯定有过疑问，Service是干嘛的，
因为在某些场景下，这层跟DAO是很像的，而以后，他们再也不会有这种疑问了

除了声明式API，我们还提供以下功能

1. 单表操作和固定sql操作，仅需一行注解
2. 分页仅需调用一个方法，没有任何第三方依赖
3. 没有像Mybatis一样的sqlMapper.xml
4. 支持AOP,IOC,声明式事务
5. 搭建仅需三步，并提供官方中文文档，再也不需要满大街的搜Spring的机翻文档或者个人博客了

## 官网

[http://mars-framework.com](http://mars-framework.com)

## 捐款赞助

[![ko-fi](https://www.ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/G2G517AIY)

[官网赞助](http://mars-framework.com/sponsor.html)