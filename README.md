<p align="center"><img width="300px" src="https://github.com/yuyenews/Mars-java/blob/master/mars-images/logo-long.png?raw=true" /></p>

![](https://img.shields.io/badge/licenes-GPL-brightgreen.svg)
![](https://img.shields.io/badge/jdk-1.8+-brightgreen.svg)

<p>
    The Js framework has React, Vue, AngularJS, but the java framework has only one Spring. Many people say that I am making wheels, but I don’t think so, so... It’s time to overthrow the Spring, I hope everyone can get involved, contributing code, together let java developers have another choice
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