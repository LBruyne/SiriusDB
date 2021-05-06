# ZooKeeper使用手册

## 用途说明

### ZooKeeper介绍

ZooKeeper是一个分布式的，开放源码的分布式应用程序协调服务，是Google的Chubby一个开源的实现，是Hadoop和Hbase的重要组件。它是一个为分布式应用提供一致性服务的软件，提供的功能包括：配置维护、域名服务、分布式同步、组服务等。

ZooKeeper的目标就是封装好复杂易出错的关键服务，将简单易用的接口和性能高效、功能稳定的系统提供给用户。

ZooKeeper包含一个简单的原语集，提供Java和C的接口。

官网：https://zookeeper.apache.org

### ZooKeeper使用目的

在本项目中引入ZooKeeper，是为了建立一个分布式系统的管理服务。通过ZooKeeper屏蔽掉分布式系统内管理的细节，获得事件监听的效果。

每个服务器（理论上）向ZooKeeper集群注册自己提供的服务，并且把自己的IP地址和服务端口等数据信息创建到具体的服务目录下。客户端向ZooKeeper集群监听自己关注的RPC服务，同时监听服务目录下的IP地址列表变化。

## 安装方法

不同平台下安装的方法是相似的，这里以MacOS下的安装流程进行示例。Windows和其他Linux版本下的安装方法可以参考相关网站实现。欢迎在这里添加其他平台下的安装方法。

### Mac下

#### 使用Homebrew安装

Homebrew是Mac下的包管理工具，类似于Linux下的apt和apt-get。

```shell
brew install zookeeper
```

安装后，在/usr/local/etc/zookeeper/目录下，已经有了缺省的配置文件。

之后使用下列命令启动或停止应用。

```shell
zkServer
zkServer status
zkServer start
zkServer restart
zkServer stop
```

使用一条命令就可完成安装流程。但是这样的安装方法亲测可能会出现版本的问题，导致之后启动应用失败。

#### 使用源码进行安装

从网络上应该可以找到指定版本的ZooKeeper压缩包或源码文件，将其安装到本地。

这里我将源码放在了/usr/local/etc目录下。之后打开或创建(创建一个zookeeper的配置文件zoo.cfg，可复制conf/zoo_sample.cfg作为配置文件)/conf/zoo.cfg文件，对dataDir的存放位置进行配置即可。

```shell
$ bin % ls
README.txt		zkEnv.cmd		zkTxnLogToolkit.cmd
zkCleanup.sh		zkEnv.sh		zkTxnLogToolkit.sh
zkCli.cmd		zkServer.cmd		zookeeper.out
zkCli.sh		zkServer.sh
```

可以看到这里有zkServer的脚本文件，那么使用
```shell
bash zkServer.sh start 
```
即可启动应用。

zoo.cfg配置文件说明如下：

```shell
 1 # The number of milliseconds of each tick
 2 # tickTime：CS通信心跳数
 3 # Zookeeper 服务器之间或客户端与服务器之间维持心跳的时间间隔，也就是每个 tickTime 时间就会发送一个心跳。tickTime以毫秒为单位。
 4 tickTime=2000
 5 
 6 # The number of ticks that the initial 
 7 # synchronization phase can take
 8 # initLimit：LF初始通信时限
 9 # 集群中的follower服务器(F)与leader服务器(L)之间初始连接时能容忍的最多心跳数（tickTime的数量）。
10 initLimit=5
11 
12 # The number of ticks that can pass between 
13 # sending a request and getting an acknowledgement
14 # syncLimit：LF同步通信时限
15 # 集群中的follower服务器与leader服务器之间请求和应答之间能容忍的最多心跳数（tickTime的数量）。
16 syncLimit=2
17 
18 # the directory where the snapshot is stored.
19 # do not use /tmp for storage, /tmp here is just 
20 # example sakes.
21 # dataDir：数据文件目录
22 # Zookeeper保存数据的目录，默认情况下，Zookeeper将写数据的日志文件也保存在这个目录里。
23 dataDir=/usr/local/etc/zookeeper/data
24 
25
26 # dataLogDir：日志文件目录
27 # Zookeeper保存日志文件的目录。
28 dataLogDir=/usr/local/etc/zookeeper/logs
30 # the port at which the clients will connect
31 # clientPort：客户端连接端口
32 # 客户端连接 Zookeeper 服务器的端口，Zookeeper 会监听这个端口，接受客户端的访问请求。
33 clientPort=2181
34 
35 # the maximum number of client connections.
36 # increase this if you need to handle more clients
37 # maxClientCnxns=60
38 #
39 # Be sure to read the maintenance section of the 
40 # administrator guide before turning on autopurge.
41 #
42 # http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
43 #
44 # The number of snapshots to retain in dataDir 保留数量3
45 autopurge.snapRetainCount=3
46 # Purge task interval in hours
47 # Set to "0" to disable auto purge feature 清理时间间隔1小时
48 autopurge.purgeInterval=1
```

### 启动失败原因

- 端口被占用：ps -ef | grep 2181
- zoo.cfg配置错误
- 防火墙
- ZooKeeper版本问题：安装指定版本的JDK

## 配置集群

ZooKeeper一共有三种运行模式，分别是单机模式（standalone）、集群模式和伪集群模式。

其中单机模式较好理解。在自己的主机上安装一个ZooKeeper客户端，然后运行，进入的就是单机模式。集群模式也很好理解，就是正常运行的情况下，多台主机上分别配置ZooKeeper客户端，然后进行通信即可。而伪集群模式，就是在自己的本机上运行多个ZooKeeper进程，分别对应不同的端口，假装集群的运行。

### 配置一个ZooKeeper Client

首先需要在zoo.cfg中添加配置项。这个配置项的书写格式比较特殊，规则如下：
```
server.N=YYY:A:B  
```
其中中N表示服务器编号，YYY表示服务器的IP地址，A为LF通信端口，表示该服务器与集群中的leader交换的信息的端口。B为选举端口，表示选举新leader时服务器间相互通信的端口（当leader挂掉时，其余服务器会相互通信，选择出新的leader）。一般来说，集群中每个服务器的A端口都是一样，每个服务器的B端口也是一样。

但是当所采用的为伪集群时，IP地址都一样，所以此时只能让A端口和B端口不一样。

之后在./data下创建一个myid文件，里面存放服务器的编号，如server.1对应的编号就是1

### 伪集群模式配置

伪集群模式就是在同一主机启动多个zookeeper并组成集群

- 在同一台主机上，通过复制得到三个ZooKeeper实例，分别命名为ZooKeeper-1、ZooKeeper-2、ZooKeeper-3
- 修改ZooKeeper-X中的zoo.cfg文件，需要修改以下内容：
    ```shell
    dataDir=/usr/local/etc/zookeeper-X/data
    dataLogDir=/usr/local/etc/zookeeper-X/logs
    clientPort=X181
  
    server.1=127.0.0.1:12888:13888
    server.2=127.0.0.1:14888:15888
    server.3=127.0.0.1:16888:17888
  
    # 这里的端口都是随便设置的，保证不冲突就可以
    ```
- 修改ZooKeeper-X中的myid文件，设置为该服务器的ID
  ```shell
  echo 'X' > data/myid
  ```
- 分别启动三个节点

### 集群模式配置

- 在多台主机上，分别安装ZooKeeper实例。
- 修改zoo.cfg文件，此时多台主机上的客户端端口、监听端口和选举端口可以设置为一样。
- 分别添加myid文件，内容为服务器的编号。

