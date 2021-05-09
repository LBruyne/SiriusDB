# thrift 使用手册

## thrift介绍

RPC框架：简单的说，RPC就是从一台机器（客户端）上通过参数传递的方式调用另一台机器（服务器）上的一个函数或方法（可以统称为服务）并得到返回的结果。

用户通过Thrift的IDL（接口定义语言）来描述接口函数及数据类型，然后通过Thrift的编译环境生成各种语言类型的接口文件，用户可以根据自己的需要采用不同的语言开发客户端代码和服务器端代码。

使用thrift框架，可以屏蔽传输等一大堆中间事务，我们只需要：
1. 定义（约定）一个接口(.thrift)文件，然后用thrift生成Java类。这个类由客户端和服务端共同拥有并使用。
2. 服务端根据文件中定义的接口，做出具体的业务实现。
3. 客户端连接服务端，调用文件中的客户端的方法，拿到结果。

## 安装方法

我们统一在Java环境下进行编写。

各种环境下安装方法见[thrift官网](https://thrift.apache.org/docs/install/)。

例如在Mac下只需要：

```shell
brew install thrift
```

就可以了。

之后在Maven环境中添加thrift对应依赖，并且在工程中添加thrift生成工具即可。

## 数据类型

### 基本类型：
- bool：布尔值，true 或 false，对应 Java 的 boolean
- byte：8 位有符号整数，对应 Java 的 byte
- i16：16 位有符号整数，对应 Java 的 short
- i32：32 位有符号整数，对应 Java 的 int
- i64：64 位有符号整数，对应 Java 的 long
- double：64 位浮点数，对应 Java 的 double
- string：utf-8编码的字符串，对应 Java 的 String

### 结构体类型：
- struct：定义公共的对象，类似于 C 语言中的结构体定义，在 Java 中是一个JavaBean

### 集合类型：
- list：对应 Java 的 ArrayList
- set：对应 Java 的 HashSet
- map：对应 Java 的 HashMap

### 异常类型（基本用不到）：
- exception：对应 Java 的 Exception

### 服务类型：
- service：对应服务的类

### 命名空间：
- thrift的命名空间相当于Java中的package，主要目的是组织代码。
    ```thrift
    namespace java com.xxx.thrift
    ```
  
### 文件包含
- 文件包含
  Thrift也支持文件包含，相当于C/C++中的include，Java中的import。
  ```thrift
  include "xxx.thrift"
  ```
