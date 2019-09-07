<p  align="center"><img width="300px" src="https://github.com/yuyenews/Mars-java/blob/master/mars-images/logo-long.png?raw=true" /></p>

![](https://img.shields.io/badge/licenes-GPL-brightgreen.svg)
![](https://img.shields.io/badge/jdk-1.8+-brightgreen.svg)

<p>Js框架有React，Vue，AngularJS，但java框架只有一个Spring。 很多人说我正在制造轮子，但我不这么认为，所以...是时候挑战Spring了，我希望每个人都能参与进来，贡献代码，让java开发人员有另一种选择</p>

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

<h2>我不能做什么</h2>

<p>
    <ul>
        <li>只支持main方法启动，不可以打war包</li>
        <li>只能给前端返回json，二进制流，不支持转发和重定向</li>
    </ul>
</p>

<h2>帮助文档</h2>

[Document](http://mars-framework.com/doc.html)

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
            <td>configuration file</td>
            <td>startup method</td>
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
            <td>Main方法，War包+Tomcat</td>
        </tr>
    </tbody>
</table>