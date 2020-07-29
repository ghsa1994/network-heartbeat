# network-heartbeat
<h2>简介</h2>
用来检查服务器、电脑网络状态和运行状态的软件。是不断发送心跳包来检查设备状态的项目。<br />
版本：3.0 <br />
运行平台：Windows（X86/X64）、Linux（X86/64/ARM/ARM64） <br />
运行环境：JDK1.8 <br />

network-heartbeat-server.jar:服务端，通常部署在保证稳定不会断网的机器<br />
network-heartbeat-client.jar:客户端，部署在我们要监控状态的机器<br />

在客户端断开网络之后，服务端会发送通知到管理员。

<h2>快速开始</h2>
<a href="https://github.com/ghsa1994/network-heartbeat/releases/">下载安装包</a><br/>
解压使用java -jar运行

<h2>通知设置</h2>
软件当前支持两种通知方式:<br/>
<a href="https://www.mailgun.com/">Mailgun</a><br/>
<a href="http://sc.ftqq.com/3.version">Server酱</a><br/>
具体使用方式请移步官方网站查看文档，或者<a href="http://www.baidu.com">百度</a>

<h2>Docker</h2>
服务端包中已经Dockerfile和docker-compose.yml文件<br/>
客户端的docker相关文件近期更新


