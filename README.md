<p align="center"><img width="300px" src="https://github.com/yuyenews/Mars-java/blob/master/mars-images/logo-long.png?raw=true" /></p>

![](https://img.shields.io/badge/licenes-GPL-brightgreen.svg)
![](https://img.shields.io/badge/jdk-1.8+-brightgreen.svg)

<p>
    The Js framework has React, Vue, AngularJS, but the Java framework has only one Spring. Many people say that I am making wheels, but I don't think so, so... it's time to challenge Spring, I hope everyone can get involved, contribute code, and give Java developers another option.
</p>

<h2>Other project</h2>

<p>
    <ul>
        <li>Mars-cloud: &nbsp;<a href="https://github.com/yuyenews/Mars-cloud">https://github.com/yuyenews/Mars-cloud</a></li>
        <li>Mars-start: &nbsp;<a href="https://github.com/yuyenews/Mars-start">https://github.com/yuyenews/Mars-start</a></li>
    </ul>
</p>

<h2>What can I do</h2>

<p>
    <ul>
        <li>Simple to build, out of the box</li>
        <li>http service using netty</li>
        <li>Session management using JWT</li>
        <li>Support AOP, IOC, MVC, Mybatis, transaction management</li>
        <li>Distributed deployment via Mars-cloud</li>
        <li>Remote configuration via Mars-config [iteration]</li>
    </ul>
</p>

<h2>What can't I do</h2>

<p>
    <ul>
        <li>Only support the main method to start, can not play the war package</li>
        <li>Can only return json, binary stream to the front end, does not support forwarding and redirection</li>
    </ul>
</p>

<h2>Only need one jar package</h2>

````
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>mars-start-pure</artifactId>
    <version>[The latest version, you can see the document]</version>
</dependency>
````

<h2>A configuration file</h2>

````
port: 8088

jdbc:
  #Configure the data source, it must be Alibaba's druid data source
  dataSource:
      name: dataSource
      url: jdbc:mysql://10.211.55.5:3306/test?serverTimezone=GMT%2B8
      username: root
      password: rootroot
      driverClassName: com.mysql.cj.jdbc.Driver
````

<h2>Then start from the main method</h2>

````
public class Start {
    public static void main(String[] args){
        StartMars.start(Start.class);
    }
}
````

<h2>No other configuration files other than this</h2>
<p>
    <ul>
        <li>Many frameworks claim that they don't have a configuration file. In fact, they put the configuration in the Java class, and Mars-java has only one yml, which is more flexible and more code-saving than the Java class.</li>
        <li>Controller, Bean, DAO, single table operations can be completed with pure annotations, and it is simple</li>
    </ul>
</p>

<h3>-----Upgrade the Mars-java project to the Mars-cloud project and its simplicity-----</h3>

<h2>Just need to change one start</h2>

````
<dependency>
    <groupId>com.github.yuyenews</groupId>
    <artifactId>mars-cloud-start</artifactId>
    <version>[The latest version, you can see the document]</version>
</dependency>
````

<h2>Add 5 rows configuration</h2>

````
cloud:
  # Service name, the name of the load balancing cluster of the same service must be the same, and must be unique between different clusters.
  name: cloud-client1
  sessionTimeout: 10000
  # Whether as a gateway
  gateWay: yes
  timeOut: 10000
  # Zookeeper address, multiple addresses are separated by commas, 
  # multiple addresses, must be double quotes, otherwise parsing yml files will be wrong
  register: 10.211.55.9:2180
````

<p>Very easy to learn</p>

<h2>Document</h2>

[Document](http://mars-framework.com)

<h2>Expansion pack</h2>

<p>Support redis connection</p>

<p>Encapsulates Email, MD5, AES and other tool classes and integrates hutool</p>

[Extension package](https://github.com/yuyenews/Mars-extends)

<h2>Simple comparison</h2>

<table>
    <tbody>
        <tr class="firstRow">
            <td>name</td>
            <td>AOP</td>
            <td>IOC</td>
            <td>MVC</td>
            <td>ORM</td>
            <td>configuration file</td>
            <td>startup method</td>
        </tr>
        <tr>
            <td>Mars-java</td>
            <td>OK</td>
            <td>OK</td>
            <td>OK</td>
            <td>Mybatis，JDBCTemplete</td>
            <td>Only one and supports remote configuration</td>
            <td>Main Method</td>
        </tr>
        <tr>
            <td>Springboot</td>
            <td>OK</td>
            <td>OK</td>
            <td>OK</td>
            <td>Support most mainstream frameworks</td>
            <td>Only one and supports remote configuration</td>
            <td>Main Method，War+Tomcat</td>
        </tr>
    </tbody>
</table>