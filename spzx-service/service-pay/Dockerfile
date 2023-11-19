FROM centos7-jdk17
MAINTAINER atguigu
EXPOSE 8515
ADD target/service-pay-1.0-SNAPSHOT.jar /service-pay-1.0-SNAPSHOT.jar

WORKDIR /
ENTRYPOINT ["java" , "-jar" , "service-pay-1.0-SNAPSHOT.jar"]