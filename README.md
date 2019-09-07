<p align="center"><img width="300px" src="https://github.com/yuyenews/Mars-java/blob/master/mars-images/logo-long.png?raw=true" /></p>

![](https://img.shields.io/badge/licenes-GPL-brightgreen.svg)
![](https://img.shields.io/badge/jdk-1.8+-brightgreen.svg)

<p>
    Mars-java is a javaWeb development framework that does not require a container. It uses netty for http service management, supports AOP, IOC, MVC, and integrates Mybatis as a persistence layer. In addition, it provides Mars-config to support remote configuration. Mars-extends to support more features and tools
</p>

<h2>What can I do</h2>

<p>
    &nbsp;
    - http service using netty
    <br/>
    &nbsp;
    - Session management using JWT
    <br/>
    &nbsp;
    - Support AOP, IOC, MVC, Mybatis, transaction management
    <br/>
    &nbsp;
    - Distributed deployment via Mars-cloud
    <br/>
    &nbsp;
    - Remote configuration via Mars-config [iteration]
</p>

<h2>What can't I do</h2>

<p>
    &nbsp;
    - Only support the main method to start, can not play the war package
    <br/>
    &nbsp;
    - Can only return json, binary stream to the front end, does not support forwarding and redirection
</p>

<h2>Document</h2>

[Document](http://mars-framework.com/doc.html)

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