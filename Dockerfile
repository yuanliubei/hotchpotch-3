FROM openjdk:17-jdk-alpine
VOLUME /tmp

ENV TZ 'Asia/Shanghai'
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY ./build/libs/*.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Duser.timezone=GMT+8","-jar", "/app.jar"]
