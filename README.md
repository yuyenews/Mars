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

Declarative API programming (DAP) framework

</div>

<div align="center">

[Mars-start Source (launcher)](https://github.com/yuyenews/Mars-start)，
[Mars-cloud Source (distributed middleware)](https://github.com/yuyenews/Mars-cloud)

</div>

## Declarative API programming (DAP)

According to the traditional development method, if you want to develop a back-end interface, you need to divide into the following three steps

1. Create controller
2. Create a service
3. Create dao (and even create xml to store sql)

However, when we write an interface, the focus should be on business logic, which means that our focus should be on the second step, but usually the repeated workload of the first and third steps suffocates us, so I Arranged a bit, formulated a new development method, this new set of development methods is called a declarative API

1. Write business logic
2. Declare an API to the front end
3. Associate API with business logic

So we play like this

## Write business logic

interface
```java
public interface TestService {

    Object/*Data type to return*/ selectList(TestDTO testDTO);
}
```
Implementation class
```java
@MarsBean("testService")
public class TestServiceImpl implements TestService{

    Object/*Data type to return*/ selectList(TestDTO testDTO){
        
        // Writing business logic
		
        return data;//Just return directly, it will automatically become json
    }
}
```
## Add two annotations to the super interface of Service

```java
@MarsApi
public interface TestService {

    @MarsReference(beanName = "testService")
    Object/*Data type to return*/ selectList(TestDTO testDTO);
}
```

At the core of this set of ideas is to treat the back end as a separate entity and completely separate it from the front end.Back-end write back-end business logic. If the front end needs data, then we declare an interface

## What to do next

Seeing this, everyone will definitely have questions, how to call the api on the front end, and how to operate the database on the back end? This requires you to move your fingers and check out my official website

## What else can i do

First, the declarative API is a change to the front-end and back-end interaction methods, eliminating the need for a Controller.

In fact, this style is very common in microservices, such as Dubbo's api,
Since the microservice interface can use interface to provide external services, we can also use it on the http interface.

In addition to the declarative API, we also provide the following features

1. Single table operation and fixed sql operation, only one line of annotation is required
2. Paging only needs to call one method, without any third-party dependencies
3. Distributed lock requires only one line of annotations
3. No sqlMapper.xml like Mybatis
4. Support AOP, IOC, declarative transactions

## Official website

[http://mars-framework.com](http://mars-framework.com)

## Donation sponsorship

[![ko-fi](https://www.ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/G2G517AIY)

[Go to the official website to sponsor](http://mars-framework.com/sponsor.html)