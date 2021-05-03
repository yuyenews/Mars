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

## 项目简介

Martian框架是 Magician项目的一个超集，
整合了Magician的大部分组件，并进行了少量的二次封装，
使其能够更加快捷的 搭建一个后端服务

## 运行环境

- 最低支持JDK 11+

## 搭建步骤

### 一、导入依赖
```xml
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>Martian</artifactId>
    <version>最新版</version>
</dependency>

```
### 二、创建配置类
```java
public class DemoConfig extends MartianConfig {

    @Override
    public int port() {
        return super.port();
    }
}
```

### 三、启动服务
```java
@MartianScan(scanPackage = {"com.demo.controller"})
public class Start {

    public static void main(String[] args) {
        StartMartian.start(Start.class, new DemoConfig());
    }
}
```

## 使用示例

[https://github.com/yuyenews/Martian-newExample](https://github.com/yuyenews/Martian-newExample)

## 社区以及联系方式

- QQ群: 773291321
- 邮箱: yuyemail@163.com
- B站: [贝克街的天才](https://space.bilibili.com/41981562)
- 微博: [@Ye卡布奇诺](https://weibo.com/tcyuye?sudaref=mars-framework.com&is_all=1)
- 社区: [https://github.com/yuyenews/Martian/discussions](https://github.com/yuyenews/Martian/discussions)