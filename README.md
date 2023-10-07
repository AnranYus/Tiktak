# 环境
Redis,MySQL,RabbitMQ,Nacos

若使用keystore则需在配置中心配置`jks.password` `jks.key-path` `jks.alias`字段

否则需修改`Common/config/CommonConfig`相关配置

# Build
```shell
gradle bootJar
```

产物在`build/target/`内

# Boot
```shell
bash boot.sh
```
or
```shell
java -jar build/target/xxx.jar
```

# TODO
- [x] 基本功能
- [x] 网关
- [x] 发现服务
- [x] 配置中心
- [x] 视频存储