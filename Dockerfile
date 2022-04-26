FROM nekoimi/openjdk:11-alpine3.10

LABEL maintainer="nekoimi <nekoimime@gmail.com>"

RUN apk add -U tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && apk del tzdata

ENV JAVA_OPS -server -Xms128m -Xmx1024m -XX:CompressedClassSpaceSize=128m -XX:MetaspaceSize=200m -XX:MaxMetaspaceSize=200m

WORKDIR app

ARG JAR_FILE=target/app.jar

COPY ${JAR_FILE} app.jar

VOLUME /app/logs

EXPOSE 8080

CMD java ${JAVA_OPS} -Djava.security.egd=file:/dev/./urandom -jar app.jar