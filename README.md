# SiriusDB

一个简单的分布式数据库系统，用于浙江大学大规模软件课程设计。

## 快速开始

项目使用Maven进行管理，可以在安装Maven后快捷地运行。

首先在主机上安装ZooKeeper客户端并完成相应配置。

之后分别在不同的主机上运行client和master模块，并添加规定说的region模块，即可运行应用。

## 模块说明
* SiriusDB-client: Client客户端相关功能的实现
* SiriusDB-common: 项目公用类、接口、注解、异常等数据模型和通用方法的定义
* SiriusDB-master: Master服务器相关功能的实现
* SiriusDB-rpc: 模块间、主机间RPC交互的接的口定义、接口交互DTO(Data Transfer Object)的定义
* SiriusDB-region: Region服务器相关功能实现

## 版本管理


## 代码要求

只列出了比较重要的几点。具体要求可以见[Java开发手册]()

### 变量、方法、文件命名

```java
public class ServiceImpl implements IService {
    private static CatalogManager catalogManager;
}
```

- 一般变量一律使用驼峰命名法
- 类、枚举、接口等名称一律全部大写单词首字母
- 不允许出现魔术值，所有常量需要用static final修饰，且命名时需要全部字母大写，单词之间以下划线分隔

### 类、接口、枚举等数据模型定义的注释

和这个类似即可，不一定需要写明author之类的。能分得清哪些是谁写的就好，比如在Class上面注明作者，这样就知道整个类是一个人完成的。

```java
/**
 * @Description: 用途描述
 * @author: 作者
 * @date: 创建时间/
 */
```

### 方法定义注释

```java
/**
 * @Description: 用途描述
 * @author: 作者
 */
```

### 异常处理

对于业务逻辑中有可能出现Exception的代码片段，应该用try catch进行包围，不推荐使用throws作用于方法签名上

### 代码提交规范（重要）

- 本部分开发到一半，还没有开发完或者需要补充逻辑的地方，请用TODO标注
- 不要把生成的二进制文件（target）提交到代码仓库！！！
- 标注好commit的内容，主要为本次提交的内容和TODO的事项
- 开发过程中代码请提交到dev-common分支上
