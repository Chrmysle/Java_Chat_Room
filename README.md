# Java Chat Room

一个基于 **Spring Boot + WebSocket** 的 Java 聊天室项目，支持多人在线聊天、私聊和群聊。项目结构清晰，便于后续功能扩展。

---

## 项目简介

- **后端**：Spring Boot + WebSocket  
- **前端**：HTML + JavaScript  
- **功能**：
  - 用户登录与注册（非本人实现）
  - 实时群聊、私聊功能
  - 支持多人同时在线

---

## 功能模块说明

### 1. 聊天模块（本人实现）

- **后端**：
  - `ChatWebSocketServer.java`：处理 WebSocket 连接，消息广播与私聊逻辑
  - `WebSocketConfig.java`：WebSocket 配置类，注册 `/chat` 路径
  - `ChatApplication.java`：Spring Boot 启动类
  - `Message.java`：消息实体类，包含发送者、接收者、内容、类型、时间

- **前端**：
  - `index.html`：聊天室页面
  - `chat.js`：聊天逻辑，连接 WebSocket，处理消息发送和接收

### 2. 登录注册模块（非本人实现）

- **功能**：
  - 用户注册、登录验证
  - 登录成功后进入聊天室

---

## 项目目录结构

```text
Java_Chat_Room/
├─ src/
│  ├─ main/
│  │  ├─ java/com/chatroom/      
│  │  │  ├─ ChatWebSocketServer.java
│  │  │  ├─ WebSocketConfig.java
│  │  │  ├─ ChatApplication.java
│  │  │  ├─ Message.java
│  │  │  ├─ UserController.java       # 登录注册
│  │  │  ├─ UserService.java          # 登录注册
│  │  │  └─ User.java                 # 登录注册
│  │  └─ resources/
│  │     └─ static/
│  │        ├─ index.html             # 聊天页面
│  │        ├─ chat.js                # 聊天逻辑
│  │        ├─ login.html             # 登录页面
│  │        └─ register.html          # 注册页面
└─ pom.xml                             # Maven 项目配置
```
## 运行方法

1. **克隆或下载项目**

2. **使用 Maven 构建：**

```bash
mvn clean install
```
3. **启动Spring Boot：**

```bash
java -jar target/Java_Chat_Room-1.0-SNAPSHOT.jar
```
## 前端访问

- 本机访问：`http://localhost:8080/index.html`
- 局域网访问：`http://<你的局域网IP>:8080/index.html`
- WebSocket 地址：`ws://<你的IP>:8080/chat?username=<用户名>`

---

## 注意事项

### WebSocket 配置
- 允许跨域访问：`setAllowedOrigins("*")`

### Jackson 序列化 LocalDateTime
- 已添加依赖 `jackson-datatype-jsr310`，否则发送消息时会报错

### 登录注册模块
- 目前是演示用途，安全性有限
- 如果要修改，需要查看 `UserController` 与 `UserService`

---

## 扩展说明

以后添加新功能时，请遵循以下规则：

### 前端
- 页面放在 `src/main/resources/static/`
- JS 脚本单独放 `.js` 文件，保持命名与功能对应

### 后端
- 新功能类放在 `com.chatroom` 下，尽量按功能划分包，例如：
  - `com.chatroom.user`：用户相关功能
  - `com.chatroom.chat`：聊天相关功能

### WebSocket
- 新消息类型或功能需在 `ChatWebSocketServer.java` 中增加处理逻辑

### 依赖
- 使用 Maven 管理依赖，修改 `pom.xml` 后需执行 `mvn clean install`

### 模块标注
- 如果是你未开发的功能，请在 README 中明确标注“非本人实现”
