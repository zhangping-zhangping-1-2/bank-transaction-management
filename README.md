# HSBC 银行交易管理系统

## 项目概述
一、项目概述
本项目是一个专为 HSBC 银行打造的交易管理系统，基于 Spring Boot 框架和 H2 数据库构建。系统提供了交易的创建、查询、更新和删除等核心功能，
采用 RESTful API 设计风格，具备数据验证、异常处理和缓存机制，确保系统的稳定性、可靠性和高性能。同时，项目进行了全面的单元测试和压力测试，
并且支持 Docker 容器化部署，方便在不同环境中灵活运行。

## 项目结构
- `src/main/java`：Java 源代码目录
    - `com.hsbc.banktransactionmanagement.controller`：控制器层，处理 HTTP 请求
    - `com.hsbc.banktransactionmanagement.dao`：数据访问层，与数据库交互
    - `com.hsbc.banktransactionmanagement.entity`：实体类，对应数据库表
    - `com.hsbc.banktransactionmanagement.service`：服务层，实现业务逻辑
    - `com.hsbc.banktransactionmanagement.common`：通用工具类，如统一返回值和全局异常处理
- `src/main/resources`：资源文件目录
    - `application.properties`：应用配置文件
    - `static`：静态资源目录，如 HTML、CSS 和 JavaScript 文件
- `src/test/java`：测试代码目录
    - `com.hsbc.banktransactionmanagement.service`：服务层测试类
    - `com.hsbc.banktransactionmanagement.controller`：控制器层测试类
- `Dockerfile`：用于构建 Docker 镜像
- `jmeter - test - plan.jmx`：JMeter 压力测试计划

## 外部库说明
- **Spring Boot**：用于快速搭建项目，提供自动配置和依赖管理。
- **Spring Data JPA**：简化数据库操作，提供基本的 CRUD 功能。
- **H2 Database**：作为内存数据库，方便开发和测试。
- **Lombok**：减少样板代码，自动生成 getter、setter 等方法。
- **Spring Boot Validation**：用于请求参数的验证。
- **Spring Cache**：实现缓存机制，提高系统性能。
- **JUnit Jupiter**：进行单元测试。
- **Mockito**：在单元测试中进行模拟对象。
- **jQuery**：在前端页面中实现交互逻辑。
- **JMeter**：进行压力测试。

## 运行项目
### 本地运行
1. 启动本地springboot
2. 本地访问前端页面：
打开浏览器，访问 http://localhost:8080
###   Docker 运行
1. 构建 Docker 镜像：
```sh
docker build -t hsbc - bank - transaction - management.
```
2. 运行 Docker 容器：
```sh
   docker run -p 8080:8080 hsbc - bank - transaction - management
```
## 架构设计方案
###分层架构
*采用经典的 MVC（Model - View - Controller）分层架构思想，将系统划分为控制器层（Controller）、服务层（Service）、数据访问层（DAO）和实体层（Entity），各层职责明确，提高了代码的可维护性和可扩展性。
### 数据验证与异常处理
*数据验证：使用 Spring Boot Validation 对请求参数进行验证，确保输入数据的合法性，避免无效数据进入系统。例如，对交易金额字段进行验证，确保其大于等于 0.01。
*异常处理：在系统中统一处理异常，使用全局异常处理器捕获并处理各种异常，将友好的错误信息返回给前端，提高系统的健壮性。
### 缓存机制
*引入 Spring Cache 实现缓存机制，对频繁查询的数据进行缓存，减少数据库的访问次数，提高系统的性能。例如，对交易记录的分页查询结果进行缓存，当再次查询相同条件的数据时，直接从缓存中获取。
### 前端设计
*前端页面使用 HTML、CSS 和 JavaScript 构建，结合 jQuery 实现交互逻辑。页面布局简洁美观，操作方便，为用户提供良好的使用体验。
### API设计
* 接口形式：遵循RESTful API设计原则

### 銀行交易管理API接口说明
1. 新增交易记录
   请求方式：POST
   请求路径：/api/transactions
   请求头：
   Content-Type: application/json
   请求体：
    ```json
    {
      "transactionType": "Transfer",
      "fromAccount": "123456",
      "toAccount": "654321",
      "amount": 1000.00,
      "description": "Sample transfer",
      "code": "12345"
    }
    ```
-transactionType：交易类型，如 "Transfer"（转账）、"Withdrawal"（取款）等。
-fromAccount：转出账户。
-toAccount：转入账户。
-amount：交易金额。
-description：交易描述。
-code：交易代码
 **响应内容**  返回添加成功的交易记录的`Result`对象，或业务异常时返回错误信息。

2. 根据 ID 删除交易记录
   请求方式：DELETE
   请求路径：/api/transactions/{id}
   路径参数：
   id：需要删除的交易记录的 ID。
   **响应内容**：返回删除成功的交易记录的`Result`对象，或业务异常时返回错误信息
3. 根据 ID 更新交易记录
   请求方式：PUT
   请求路径：/api/transactions/{id}
   请求头：
   Content-Type: application/json
   请求体：
```json
{
    "transactionType": "Withdrawal",
    "fromAccount": "654321",
    "toAccount": "123456",
    "amount": 500.00,
    "description": "Sample withdrawal",
    "code": "54321"
}
```
-transactionType：更新后的交易类型。
-fromAccount：更新后的转出账户。
-toAccount：更新后的转入账户。
-amount：更新后的交易金额。
-description：更新后的交易描述。
-code：更新后的交易代码。
**响应内容：** 返回修改成功的交易记录的`Result`对象，或业务异常时返回错误信息

4. 获取所有交易记录
   请求方式：GET
   请求路径：/api/transactions
   **响应内容：** 返回查询成功的交易记录的`Result`对象，或业务异常时返回错误信息

5. 分页获取交易记录
   请求方式：GET
   请求路径：/api/transactions/page
   查询参数：
   page：页码，从 0 开始。
   size：每页记录数。
   transactionType（可选）：交易类型，用于筛选。
   **响应内容：** 返回分页查询成功的交易记录的`Result`对象，或业务异常时返回错误信息

#### 参数校验
    引用spring-boot-starter-validation进行参数校验，参数校验不通过将统一返回异常信息。
    实现了后端部分参数校验、前端参数校验

#### 异常处理
    自定义异常CustomException，自定义异常信息、标准化返回错误信息格式，方便前端统一处理后端接口信息。
    并实现创建重复交易或删除不存在的交易等场景实现错误处理

## 性能设计
### 索引设计
  ```java
    @Table(indexes = {@Index(name = "idx_code", columnList = "code", unique = true)//唯一滴
        , @Index(name = "idx_type", columnList = "transactionType")})
    public class Transaction {
    }
  ```
* 索引idx_code 作用：根据code唯一性，避免交易重复提交
* 索引idx_type 作用：根据交易类型transactionType搜索进行分页查询

#### 缓存SpringCache
对频繁查询的数据进行缓存，减少数据库的访问次数，从而提高系统的性能。
例如，对交易记录的分页查询结果进行缓存，当再次查询相同条件的数据时，
直接从缓存中获取，避免了重复的数据库查询操作
**缓存使用方式：**
1. 开启缓存支持
   TransactionCacheConfig.java：使用 @Configuration 和 @EnableCaching 注解开启缓存，并配置了缓存管理器。
2. 配置缓存管理器
   在 TransactionCacheConfig.java 中定义了一个 CacheManager 的 Bean，使用 ConcurrentMapCacheManager 作为缓存管理器，并指定缓存名称为 transactions：
   @Cacheable：用于标记方法，调用时先检查缓存，有则直接返回，无则执行方法体逻辑并将结果存入缓存。
   @CacheEvict：用于清除缓存，方法调用时会根据指定的缓存名称和键清除缓存数据。
3. 缓存类型配置为simple

## 全面测试
### 测试覆盖率：
    引用JUnit和Mockito进行单元测试
    单元测试具体见单测代码，做到方法覆盖率100%和成功率100%

* 对service层方法：覆盖率100%和成功率100%
  ![image](/image/service_test.png)
* controller层接口：覆盖率100%和成功率100%
  ![image](/image/controller_test.png)

### 压力测试




## 页面功能展示
*前端整体页面
  ![image](/image/all_page.png)

* 新增交易页面 前端、后端参数校验错误提示
  ![image](/image/front_page_add.png)

  ![image](/image/front_pag_validation.png)

  ![image](/image/backend_parameter_verification.png)

* 修改交易页面
  ![img.png](/image/front_page_edit.png)

* 分页查询，支持交易类型筛选
  ![img.png](/image/type_page.png)


## 总结
*本项目实现了简单的银行系统交易管理应用程序的基本功能。
*核心功能：创建交易、删除交易、修改交易、列出所有交易并支持根据交易类型分页查询
*实现重复交易、删除不存在交易的错误处理
*系统经过全面测试，包括单元测试和压力测试，以确保其健壮性和可靠性。
*MVC分层架构设计思想，并遵循RESTful API设计原则
*核心操作通过缓存保证性能。
*实现了参数校验和全局异常处理功能
*通过Docker进行容器化部署，支持线上IP访问。

## 后续优化与扩展
## 性能优化
1. 数据库层面
 *索引优化、数据库连接池优化、数据库查询优化：
2. 缓存层面
 *缓存策略优化、多级缓存
## 安全性优化
*数据安全：数据加密、数据脱敏、用户认证和授权、接口安全
## 可维护性优化 、日志和监控优化 

