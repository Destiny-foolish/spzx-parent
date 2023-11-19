FROM centos7-jdk17
MAINTAINER atguigu
EXPOSE 8513
ADD target/service-cart-1.0-SNAPSHOT.jar /service-cart-1.0-SNAPSHOT.jar

WORKDIR /
ENTRYPOINT ["java" , "-jar" , "service-cart-1.0-SNAPSHOT.jar"]