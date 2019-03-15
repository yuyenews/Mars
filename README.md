<h1>
    <p align="center"><img width="400px" src="https://github.com/yuyenews/Mars-java/blob/master/mars-logos/logo-long.png?raw=true" /></p>
    <h5 align="center">
        一个不需要容器的JavaWeb开发框架
    </h5>
</h1>
<br/>

<h2>我能做什么</h2>

<p>
    &nbsp;
    - 使用netty做的http服务
    <br/>
    &nbsp;
    - 使用JWT做的会话管理
    <br/>
    &nbsp;
    - 支持AOP,IOC,MVC,Mybatis,事务管理
    <br/>
    &nbsp;
    - 通过 Mars-config 可以远程配置【开发中】
    <br/>
    &nbsp;
    - 通过 Mars-cloud 可以分布式部署【开发中】
</p>

<h2>我不能做什么</h2>

<p>
    &nbsp;
    - 只支持main方法启动，不可以打war包
    <br/>
    &nbsp;
    - 只能给前端返回json，二进制流，不支持转发和重定向
</p>

<h2>帮助文档</h2>

[Document](http://goge-framework.com/doc.html)

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
            <td>ok</td>
            <td>ok</td>
            <td>ok</td>
            <td>目前支持mybatis，后面会支持jpa</td>
            <td>只有一个，并只支持远程配置</td>
            <td>Main方法</td>
        </tr>
        <tr>
            <td>Springboot</td>
            <td>ok</td>
            <td>ok</td>
            <td>ok</td>
            <td>支持大部分主流框架</td>
            <td>只有一个，并只支持远程配置</td>
            <td>Main方法，War包+Tomcat</td>
        </tr>
    </tbody>
</table>

<h2>联系我</h2>

<p>如果你对此框架感兴趣，可以加我的QQ群：773291321</p>

<p><img src="https://images.gitee.com/uploads/images/2019/0314/230940_795215de_2331383.png"/></p>

