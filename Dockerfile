# 后端
FROM maven:3.5-jdk-8-alpine as builder

MAINTAINER zengchao<zc19981211gmail.com>

# 本地制作镜像
WORKDIR /app

# 复制文件到工作目录(Dockerfile 所在目录)
COPY pom.xml .
COPY src ./src

# 镜像构建的时候需要运行的命令
RUN mvn package -DskipTests

# 设置镜像的启动命令
CMD ["java", "-jar", "/app/target/link-partner-backend-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]