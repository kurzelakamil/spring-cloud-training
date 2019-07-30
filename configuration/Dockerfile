FROM openjdk:11.0.3-slim-stretch
RUN apt-get update && apt-get install -y netcat
ADD target/configuration-server.jar /
ADD run.sh /
RUN chmod +x run.sh
CMD /bin/sh run.sh
