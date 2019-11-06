![](https://img.shields.io/badge/licenes-MIT-brightgreen.svg)
![](https://img.shields.io/badge/jdk-1.8+-brightgreen.svg)

## 其他项目

[Mars-start源码](https://github.com/yuyenews/Mars-start)
[Mars-cloud源码](https://github.com/yuyenews/Mars-cloud)

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

## 实体类的争议

本框架是强制要求，DAO用来接数据的实体类，字段名必须和表的字段名一致的，
这样虽然提高了耦合，但是减少了映射的配置，并且实际情况是，如果你用别的框架，
你改了数据库的字段名，你一样要去改配置映射的那部分代码，所以用映射来降低耦合并没起到什么作用

不过争议不再这一点上，而在于命名规范上，因为数据库的字段名通常是下划线分割单词，
跟java的变量名常用的驼峰有点出入，
首先可以确定的是JVM官方对变量名的命名规范是支持用下划线的，
并且常量都是这么做得，从没出现过风险。

所以我总结了以下观点

1. 是全英文命名
2. 可读性完全没影响
3. 不管是编译还是运行都不会存在风险
4. 只是DAO返回的实体类用这种命名，并非所有的变量都要遵守

所以，我们为什么非要一成不变的把自己关在旧笼子里？

如果实在无法接受，我们也提供了解决方案，那就是用Map，然后在DAO层手工转换成你的实体类。

## 官网

[http://mars-framework.com](http://mars-framework.com)

## 捐款赞助

[![ko-fi](https://www.ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/G2G517AIY)

[官网赞助](http://mars-framework.com/sponsor.html)