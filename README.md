<div align=center>
<img width="150px;" src="http://mars-framework.com/img/logo-github3.png"/>
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

## Project Description

Martian is a Java development framework that does not require containers

No need for Tomcat, no Jboss, no Netty, or even Servlet. Based entirely on the JRE class library. The project lost weight again

## Usage example

[https://github.com/yuyenews/Mars-Example](https://github.com/yuyenews/Mars-Example)

## Program features
### 1. the declarative API
You only need to add a annotation to the parent interface of your service to provide an interface to the outside world. We also support the traditional Controller
```java
@MarsApi(refBean="The name of the bean to reference")
public interface TestService {

   `Return type` selectList(TestDTO testDTO);
}
```
### 2. Single table addition, deletion, modification, and select without SQL
```java
// Query a piece of data based on the primary key
@MarsGet(tableName = "userinfo",primaryKey = "id")
public abstract `Return type` selectById(int id);

// Insert a piece of data
@MarsUpdate(tableName = "userinfo",operType = OperType.INSERT)
public abstract int insert(`Entity object parameter`);

// Delete a piece of data based on the primary key
@MarsUpdate(tableName = "userinfo",operType = OperType.DELETE,primaryKey = "id")
public abstract int delete(int id);

// Modify a piece of data based on the primary key
@MarsUpdate(tableName = "userinfo",operType = OperType.UPDATE,primaryKey = "id")
public abstract int update(`Entity object parameter`);
```

### 3. Parameter verification requires only one annotation
Just add a annotation to the field of VO
```java
// Cannot be empty and is 2-3 digits long
@MarsDataCheck(notNull = true,maxLength = 3L,minLength = 2L, msg = "id不可为空且长度必须在2-3位之间")
private Integer id;

// Regular check
@MarsDataCheck(reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$",msg = "密码不可以为空且必须是6-12位数字字母组合")
private String password;
```

How does the front end get prompted?

Just request the API normally. If the verification fails, you will get such a json.
```json
{"error_code":1128,"error_info":"提示文字"}
```

### 4. Exception listener
Usually when we write code, we need to add try {} catch () {} to each Controller method, which can be used to return the json string normally when the exception

Spring has an ExecptionHandler to solve this problem, and Martion also provides a corresponding solution

The solution is to do nothing, and if something goes wrong, it will automatically return the following json string to the front end
```json
{"error_code":500,"error_info":"异常提示"}
```

### 5. One-line annotations to resolve distributed locks
Add RedisLock annotation on the method to be locked
```java
@RedisLock(key = "Define a key yourself")
public int insert(){
  return 1;
}
```