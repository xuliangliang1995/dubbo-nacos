# Nacos（发现、配置、管理微服务）



## Nacos 的关键特性

- 服务发现和服务健康监测
- 动态配置服务
- 动态 DNS 服务
- 服务及其元数据管理



## Nacos 优势

- 简单易用
- 特性丰富
- 超高性能（AP）
- 超大容量
- 高可用



## Nacos 生态

- spring-cloud 生态
- dubbo 生态
- k8s 生态
- nodejs 生态



## Nacos 特性

- 服务发现
  - 服务发现
  - 权重管理
  - 打标管理
  - 优雅上下线
- 配置管理
  - 在线编辑
  - 历史版本
  - 一键回滚
  - 灰度发布
  - 推送轨迹
- DNS 服务



## Nacos 业务

- 服务发现
- 配置管理
  - 路由规则
  - 限流规则
  - 动态数据源
  - 预案
  - 开关
  - 动态 UI



## Nacos 安装

```shell
wget https://github.com/alibaba/nacos/releases/download/1.1.3/nacos-server-1.1.3.tar.gz
tar -zxvf nacos-server-1.1.3.tar.gz
mv nacos /usr/local/nacos
cd /usr/local/nacos/bin
# 单机模式运行
sh startup.sh -m standalone
```



## Nacos 三种部署模式

### 单机模式 —— 用于测试和单机试用

```shell
sh startup.sh -m standalone
```

- **Mysql** 支持

  1. 初始化数据库文件：```nacos-mysql.sql```

  2. 修改 ```conf/application.properties``` 文件，增加支持 ```mysql``` 数据源（目前只支持 ```mysql```）。

     ```shell
     spring.datasource.platform=mysql
     db.num=1
     db.url.0=jdbc:mysql://192.168.150.100:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
     db.user=xuliang
     db.password=***
     ```

### 集群模式 —— 用于生产环境，确保高可用

```
sh startup.sh
```

- 集群配置

1. 集群配置文件：```conf/cluster.conf```，配置 3 个或 3 个以上节点。

   ```shell
   192.168.150.101:8848
   192.168.150.102:8848
   192.168.150.103:8848
   ```

2. 配置 ```mysql``` 数据库（建议至少主备或高可用数据库）

### 多集群模式 —— 用于多数据中心场景

​	Nacos 支持 NameServer 路由请求模式，通过它您可以设计一个有用的映射规则来控制请求转发到相应的集群，在映射规则中您可以按命名空间或租户等分片请求......

​	[多网卡 IP 选择](https://nacos.io/zh-cn/docs/deployment.html)



## Nacos 控制台（[手册](https://nacos.io/zh-cn/docs/console-guide.html)）

[Nacos 控制台](http://console.nacos.io/nacos/index.html)主要旨在于增强对于服务列表，健康状态管理，服务治理，分布式配置管理等方面的管控能力，以便进一步帮助用户降低管理微服务应用架构的成本。



## Nacos 监控平台（[手册](https://nacos.io/zh-cn/docs/monitor-guide.html)）

Nacos 0.8.0 版本完善了监控系统，支持通过暴露 metrics 数据接入第三方监控系统监控 Nacos 运行状态，目前支持 prometheus、elastic search 和 influxdb，官网 [grafana 监控页面](http://monitor.nacos.io/)。



## 服务注册 & 发现和配置管理

```curl 是一种命令行工具，作用是发出网络请求，然后获取数据，显示在"标准输出"（stdout）上面。```

```curl 默认的 HTTP 动词是 GET，使用 `-X` 参数可以支持其他动词。```

- 服务注册

  ```shell
  curl -X POST 'http://127.0.0.1:8848/nacos/v1/ns/instance?serviceName=nacos.naming.serviceName&ip=20.18.7.10&port=8080'
  ```

- 服务发现

  ```shell
  curl -X GET 'http://127.0.0.1:8848/nacos/v1/ns/instance/list?serviceName=nacos.naming.serviceName'
  ```

- 发布配置

  ```shell
  curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test&content=HelloWorld"
  ```

- 获取配置

  ```shell
  curl -X GET "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos.cfg.dataId&group=test"
  ```

  

## Nacos - Spring

- **配置管理**

  1. 添加依赖

     ```java
     <dependency>
         <groupId>com.alibaba.nacos</groupId>
         <artifactId>nacos-spring-context</artifactId>
         <version>${latest.version}</version>
     </dependency>
     ```

  2. ```@EnableNacosConfig``` 启动 Nacos Spring 配置管理服务

  3. 使用 `@NacosPropertySource` 加载了 `dataId` 为 `example` 的配置源，并开启自动更新：

     ```java
     @Configuration
     @EnableNacosConfig(
         globalProperties = @NacosProperties(serverAddr = "127.0.0.1:8848")
     )
     @NacosPropertySource(dataId = "example", autoRefreshed = true)
     public class NacosConfiguration {}
     ```

  4. 通过 Nacos  的 ```@NacosValue``` 注解设置属性值。

     ```java
     @Controller
     @RequestMapping("config")
     public class ConfigController {
     
         @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
         private boolean useLocalCache;
     
         @RequestMapping(value = "/get", method = GET)
         @ResponseBody
         public boolean get() {
             return useLocalCache;
         }
     }
     ```

  5. 启动 Tomcat , 调用 ```curl http://localhost:8080/config/get``` 来获取配置信息。由于此时还未发布过配置，所以返回内容是 `false`。

  6. 通过调用 [Nacos Open API](https://nacos.io/zh-cn/docs/open-API.html) 向 Nacos Server 发布配置：dataId 为`example`，内容为 ```useLocalCache=true```

     ```shell
     curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=example&group=DEFAULT_GROUP&content=useLocalCache=true"
     ```

  7. 再次访问 `http://localhost:8080/config/get`，此时返回内容为`true`，说明程序中的`useLocalCache`值已经被动态更新了。

     

- **服务发现**

  1. 添加依赖。

     ```java
     <dependency>
         <groupId>com.alibaba.nacos</groupId>
         <artifactId>nacos-spring-context</artifactId>
         <version>${latest.version}</version>
     </dependency>
     ```

  2. ```@EnableNacosDiscovery``` 开启 Nacos Spring 服务发现功能。

     ```java
     @Configuration
     @EnableNacosDiscovery(
         globalProperties = @NacosProperties(serverAddr = "127.0.0.1:8848")
     )
     public class NacosConfiguration {}
     ```

  3. ```@NacosInjected``` 注入服务实例。

     ```java
     @Controller
     @RequestMapping("discovery")
     public class DiscoveryController {
     
         @NacosInjected
         private NamingService namingService;
     
         @RequestMapping(value = "/get", method = GET)
         @ResponseBody
         public List<Instance> get(@RequestParam String serviceName) throws NacosException {
             return namingService.getAllInstances(serviceName);
         }
     }
     ```

  4. 启动 Tomcat，调用 `curl http://localhost:8080/discovery/get?serviceName=example`，此时返回为空 JSON 数组`[]`。

  5. 通过调用 [Nacos Open API](https://nacos.io/zh-cn/docs/open-API.html) 向 Nacos server 注册一个名称为 `example` 服务。

     ```shell
     curl -X PUT 'http://127.0.0.1:8848/nacos/v1/ns/instance?serviceName=example&ip=127.0.0.1&port=8080'
     ```

  6. 再次访问 `curl http://localhost:8080/discovery/get?serviceName=example`



## Nacos SpringBoot

- **配置管理**

  1. 添加依赖

     ```java
     <dependency>
         <groupId>com.alibaba.boot</groupId>
         <artifactId>nacos-config-spring-boot-starter</artifactId>
         <version>${latest.version}</version>
     </dependency>
     ```

  2. 在 ```application.properties``` 中配置 Nacos Server 地址。

     ```java
     nacos.config.server-addr=127.0.0.1:8848
     ```

  3. 使用 ```@NacosPropertiesSource ``` 加载 `dataId` 为 `example` 的配置源，并开启自动更新：

     ```java
     @SpringBootApplication
     @NacosPropertySource(dataId = "example", autoRefreshed = true)
     public class NacosConfigApplication {
     
         public static void main(String[] args) {
             SpringApplication.run(NacosConfigApplication.class, args);
         }
     }
     ```

  4. 通过 Nacos 的 `@NacosValue` 注解设置属性值。

     ```java
     @Controller
     @RequestMapping("config")
     public class ConfigController {
     
         @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
         private boolean useLocalCache;
     
         @RequestMapping(value = "/get", method = GET)
         @ResponseBody
         public boolean get() {
             return useLocalCache;
         }
     }
     ```

     

- **服务发现**

  1. 添加依赖

     ```java
     <dependency>
         <groupId>com.alibaba.boot</groupId>
         <artifactId>nacos-discovery-spring-boot-starter</artifactId>
         <version>${latest.version}</version>
     </dependency>
     ```

  2. 在 `application.properties` 中配置 Nacos server 的地址：

     ```java
     nacos.discovery.server-addr=127.0.0.1:8848
     ```

  3. 使用 `@NacosInjected` 注入 Nacos 的 `NamingService` 实例：

     ```java
     @Controller
     @RequestMapping("discovery")
     public class DiscoveryController {
     
         @NacosInjected
         private NamingService namingService;
     
         @RequestMapping(value = "/get", method = GET)
         @ResponseBody
         public List<Instance> get(@RequestParam String serviceName) throws NacosException {
             return namingService.getAllInstances(serviceName);
         }
     }
     
     @SpringBootApplication
     public class NacosDiscoveryApplication {
     
         public static void main(String[] args) {
             SpringApplication.run(NacosDiscoveryApplication.class, args);
         }
     }
     ```



## Nacos Dubbo

- 注册中心

  1. 添加 Maven 依赖

     ```java
     <dependencies>
     	...
     	<!-- Dubbo Nacos registry dependency -->
         <dependency>
             <groupId>com.alibaba</groupId>
             <artifactId>dubbo-registry-nacos</artifactId>
             <version>0.0.1</version>
         </dependency>   
     
         <!-- Dubbo dependency -->
         <dependency>
             <groupId>com.alibaba</groupId>
             <artifactId>dubbo</artifactId>
             <version>2.6.5</version>
         </dependency>
     
         <!-- Alibaba Spring Context extension -->
         <dependency>
             <groupId>com.alibaba.spring</groupId>
             <artifactId>spring-context-support</artifactId>
             <version>1.0.2</version>
         </dependency>
         ...
     </dependencies>
     
     ```

  2. 在 ```application.properties``` 中配置 Nacos Server ：

     ```java
     dubbo.registry.address = nacos://10.20.153.10:8848
     
     ```

     

  

  

  

  

  

  

  
