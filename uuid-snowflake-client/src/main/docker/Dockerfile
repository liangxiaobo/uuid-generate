FROM java
VOLUME /tmp
ADD uuid-server-0.0.1-SNAPSHOT.jar app.jar
#RUN bash -c 'touch /app.jar'
RUN echo "Asia/Shanghai" > /etc/timezone
RUN dpkg-reconfigure -f noninteractive tzdata

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","/app.jar"]
#EXPOSE 8761