# Build

```shell
gradle bootJar
```

# Run
```shell
bash run.sh
```
or
```shell
java -jar [module]/build/libs/[jar-name].jar
```

# TODO
- [x] 基本功能
- [x] 网关
- [x] 发现服务
- [x] 配置中心
- [x] 视频存储
- [ ] 数据一致性(TODO: 上mq)
- [ ] 细节处理(频繁访问、查后更新)