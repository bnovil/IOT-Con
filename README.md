# IOT-Con
IoT devices commuciation
物联网设备通信框架，采用Netty实现非阻塞通信，使用ProtoBuf作为数据格式。

系统架构如下图
![image](https://github.com/bnovil/IOT-Con/blob/master/img/IOT.png)<br> 

Gate处理与设备间的通信，根据消息类型将数据转发给Auth或Gate, Auth处理设备注册、认证，Logic处理
不同设备之间通信的消息。具体处理业务交由worker进行

启动项目
依次启动Logic, Auth, Gate

目前还有很多不完善的地方，未来计划采用消息队列(ActiveMQ或RabbitMQ)实现Gate, Auth, Logic之间的解耦
