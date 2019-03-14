<h1 align="center">Java Web development framework that does not require a container</h1>

<p align="center"><img width="400px" src="https://images.gitee.com/uploads/images/2019/0314/211513_19b0cee2_2331383.png" /></p>

<br/>

<h2>Introduction to the framework</h2>

<p>First of all, thanks to mybatis, fastjson, cglib, pagehelper, druid, jwt, jyaml, netty, hutool. Because of the integration of these open source projects, my framework can be developed smoothly.</p>

<p>Mars-java is a java development framework that mimics springboot. It supports functions similar to springboot: AOP, IOC, MVC also integrates mybatis as a persistence layer. Unlike springboot it is:</p>

<p>
    &nbsp;&nbsp;
    1. This framework uses netty as the http service
    <br/>
    &nbsp;&nbsp;
    2. Session management with JWT
    <br/>
    &nbsp;&nbsp;
    3. Only support the main method to start, can not play the war package
    <br/>
    &nbsp;&nbsp;
    4. Controller can only return json, does not support forwarding and redirection
</p>

<h2>Document</h2>

[Document](http://goge-framework.com/doc.html)

<h2>Extension package</h2>

<p>Support redis connection</p>

<p>Encapsulated mail delivery, MD5, AES and other tools class</p>

[Extension package](https://github.com/yuyenews/Mars-extends)

<h2>Simple contrast</h2>

<table>
    <tbody>
        <tr class="firstRow">
            <td>name</td>
            <td>AOP</td>
            <td>IOC</td>
            <td>MVC</td>
            <td>mybatis</td>
            <td>configuration file</td>
            <td>startup method</td>
        </tr>
        <tr>
            <td>Mars-java</td>
            <td>support</td>
            <td>support</td>
            <td>support</td>
            <td>Direct integration</td>
            <td>Only need one</td>
            <td>main method</td>
        </tr>
        <tr>
            <td>Springboot</td>
            <td>support</td>
            <td>support</td>
            <td>support</td>
            <td>Can be integration</td>
            <td>Only need one</td>
            <td>main method，war+tomcat</td>
        </tr>
    </tbody>
</table>

<h2>Contact</h2>

<p>If you have any questions, you can add my QQ group：773291321</p>

<p><img src="https://images.gitee.com/uploads/images/2019/0314/230940_795215de_2331383.png"/></p>

