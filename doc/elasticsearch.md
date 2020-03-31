# elasticsearch
## 添加中文分词器ik 
`https://github.com/medcl/elasticsearch-analysis-ik`
## 图形化界面
https://github.com/mobz/elasticsearch-head
* elasticsearch需要配置跨域
```
http.cors.enabled: true
http.cors.allow-origin: "*"
```
* 启动elasticsearch-head
```
npm全局安装grunt-cli
npm install -g grunt-cli
项目安装依赖
npm install
运行
grunt server
访问localhost:9100
```
## 集群配置
```
# 节点1的配置信息：
# 集群名称，保证唯一
cluster.name: my‐elasticsearch
# 节点名称，必须不一样
node.name: node‐1
# 必须为本机的ip地址
network.host: 127.0.0.1
# 服务端口号，在同一机器下必须不一样
http.port: 9200
# 集群间通信端口号，在同一机器下必须不一样
transport.tcp.port: 9300
# 设置集群自动发现机器ip集合
discovery.zen.ping.unicast.hosts: ["127.0.0.1:9300","127.0.0.1:9301","127.0.0.1:9302"]
```