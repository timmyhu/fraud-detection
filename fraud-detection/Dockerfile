# 基础镜像
FROM eclipse-temurin:17-jre-jammy

# 维护者信息
LABEL maintainer="33457178@qq.com"

# 设置工作目录
WORKDIR /app

RUN mkdir -p /app/logs && chmod 777 /app/logs

# 将构建的jar包复制到容器中
COPY target/fraud-detection-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口(与application.properties中一致)
EXPOSE 8080

# 启动命令(输出重定向到日志文件)
ENTRYPOINT java -jar app.jar > /app/logs/console.log 2>&1
