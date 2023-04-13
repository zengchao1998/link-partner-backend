# 后端
FROM openjdk:8-jdk-alpine

MAINTAINER zengchao<zc19981211gmail.com>

# 本地制作镜像
WORKDIR /app

# 复制文件到工作目录(Dockerfile 所在目录)
COPY  link-partner-backend-0.0.1-SNAPSHOT.jar ./target


# 设置镜像的启动命令
CMD ["java", "-jar", "/app/target/link-partner-backend-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]