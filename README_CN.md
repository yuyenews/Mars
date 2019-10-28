<p  align="center"><img width="300px" src="https://github.com/yuyenews/Mars-java/blob/master/mars-images/logo-long.png?raw=true" /></p>

![](https://img.shields.io/badge/licenes-MIT-brightgreen.svg)
![](https://img.shields.io/badge/jdk-1.8+-brightgreen.svg)

<p>Js框架有React，Vue，AngularJS，但java框架只有一个Spring。 很多人说我正在制造轮子，但我不这么认为，所以...是时候挑战Spring了，我希望每个人都能参与进来，贡献代码，让java开发人员有另一种选择</p>

<h2>其他子项目</h2>

<p>
    <ul>
        <li>Mars-cloud: &nbsp;<a href="https://gitee.com/SherlockHolmnes/Mars-cloud">https://gitee.com/SherlockHolmnes/Mars-cloud</a></li>
        <li>Mars-start: &nbsp;<a href="https://gitee.com/SherlockHolmnes/Mars-start">https://gitee.com/SherlockHolmnes/Mars-start</a></li>
    </ul>
</p>

<h2>我能做什么</h2>

<p>
    <ul>
        <li>搭建简单，开箱即用</li>
        <li>使用netty做的http服务</li>
        <li>使用JWT做的会话管理</li>
        <li>支持AOP,IOC,MVC,Mybatis,事务管理</li>
        <li>通过 Mars-cloud 可以分布式部署</li>
        <li>通过 Mars-config 可以远程配置【迭代中】</li>
    </ul>
</p>

<h2>只需要一个jar包</h2>

````
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>mars-start-pure</artifactId>
    <version>最新版，可看文档</version>
</dependency>
````

<h2>一个配置文件</h2>

````
#配置端口号（默认8080）
port: 8088

#配置持久层
jdbc:
  #配置数据源，必须是阿里巴巴的 druid数据源
  dataSource:
      name: dataSource
      url: jdbc:mysql://10.211.55.5:3306/test?serverTimezone=GMT%2B8
      username: root
      password: rootroot
      driverClassName: com.mysql.cj.jdbc.Driver
````

<h2>然后从main方法启动</h2>

````
public class Start {
    public static void main(String[] args){
        StartMars.start(Start.class);
    }
}
````

<h2>除此之外再无任何配置文件</h2>
<p>
    <ul>
        <li>很多框架宣称自己没配置文件，其实是把配置放在了java类里面，而Mars-java只有一个yml，比java类更加灵活，更省代码</li>
        <li>Controller，Bean，DAO，单表操作都可以使用纯注解完成，而且及其简洁</li>
    </ul>
</p>

<h2>帮助文档</h2>

[Document](http://mars-framework.com)

<h2>扩展包</h2>

<p>支持redis连接</p>

<p>封装了 Email, MD5, AES 和 其他工具类 并 集成了hutool</p>

[Extension package](https://github.com/yuyenews/Mars-extends)

<h2>简单对比</h2>

<table>
    <tbody>
        <tr class="firstRow">
            <td>名称</td>
            <td>AOP</td>
            <td>IOC</td>
            <td>MVC</td>
            <td>持久层</td>
            <td>配置文件</td>
            <td>启动方式</td>
        </tr>
        <tr>
            <td>Mars-java</td>
            <td>OK</td>
            <td>OK</td>
            <td>OK</td>
            <td>支持mybatis，并有自己的JDBC</td>
            <td>只有一个，并支持远程配置</td>
            <td>Main方法</td>
        </tr>
        <tr>
            <td>Springboot</td>
            <td>OK</td>
            <td>OK</td>
            <td>OK</td>
            <td>支持大部分主流框架</td>
            <td>只有一个，并支持远程配置</td>
            <td>Main方法 或者 War包+Tomcat</td>
        </tr>
    </tbody>
</table>
