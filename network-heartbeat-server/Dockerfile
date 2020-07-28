FROM onenbcoder/jdk:8-alpine-amd64

MAINTAINER wyy


#开始复制文件
RUN mkdir /usr/local/network-heartbeat-server
COPY target/network-heartbeat-server-3.0-SNAPSHOT-jar-with-dependencies.jar /usr/local/network-heartbeat-server/network-heartbeat-server.jar
COPY config.properties /usr/local/network-heartbeat-server/

#启动服务端

CMD cd /usr/local/network-heartbeat-server/ && java -jar /usr/local/network-heartbeat-server/network-heartbeat-server.jar

EXPOSE 1888