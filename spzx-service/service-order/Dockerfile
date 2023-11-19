FROM centos7-jdk17
MAINTAINER atguigu
EXPOSE 8514
ADD target/service-order-1.0-SNAPSHOT.jar /service-order-1.0-SNAPSHOT.jar

WORKDIR /
ENTRYPOINT ["java" , "-jar" , "service-order-1.0-SNAPSHOT.jar"]