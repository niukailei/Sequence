FROM dinfo.cn/alpine/jdk8:1.0.0
VOLUME /tmp
ADD sequence-service-1.0.0.jar app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
