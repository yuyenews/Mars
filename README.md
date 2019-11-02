![](https://img.shields.io/badge/licenes-MIT-brightgreen.svg)
![](https://img.shields.io/badge/jdk-1.8+-brightgreen.svg)

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

## 官网

[http://mars-framework.com](http://mars-framework.com)

## 捐款赞助

[爱帮帮](http://www.likebb.cn/crowdfund/detail/6358?showTitle=true)
