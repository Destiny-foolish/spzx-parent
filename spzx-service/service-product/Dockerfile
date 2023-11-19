FROM centos7-jdk17
MAINTAINER atguigu
EXPOSE 8511
ADD target/service-product-1.0-SNAPSHOT.jar /service-product-1.0-SNAPSHOT.jar

WORKDIR /
ENTRYPOINT ["java" , "-jar" , "service-product-1.0-SNAPSHOT.jar"]