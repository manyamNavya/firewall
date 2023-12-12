FROM docker.io/kalilinux/kali-rolling

COPY ./build/libs/app-0.0.1-SNAPSHOT.jar ~/firewall.jar

RUN apt-get update \
    && apt install -y openjdk-11-jdk \
    && apt-get update \
    && apt install -y iptables

CMD ["java", "-jar", "~/firewall.jar"]