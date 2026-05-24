# 博客论坛系统

基于 Java SpringCloud 微服务架构的高颜值博客论坛系统。

## 技术栈

**后端：** Java 8、SpringBoot 2.3、SpringCloud Hoxton.SR12、MyBatis、MySQL 8.0、Redis 7

**前端：** Vue 2 + Element UI + Axios

**部署：** Docker Compose

## 项目结构

```
blog-forum/
├── backend/
│   ├── pom.xml                    # 父 POM
│   ├── Dockerfile                 # 统一镜像构建
│   ├── common/                    # 公共模块 (Result 统一返回)
│   ├── gateway-service/           # 网关服务 (端口 8080)
│   ├── user-service/              # 用户服务 (端口 8081)
│   ├── post-service/              # 帖子服务 (端口 8082)
│   ├── comment-service/           # 评论服务 (端口 8083)
│   ├── like-service/              # 点赞服务 (端口 8084)
│   └── sql/schema.sql             # 建表 SQL + 测试数据
├── frontend/                      # Vue + Element UI 前端
│   ├── Dockerfile
│   ├── nginx.conf
│   └── src/
│       ├── api/                   # API 接口封装
│       ├── views/                 # 页面组件
│       ├── components/            # 公共组件
│       ├── router/                # 路由配置
│       └── utils/request.js       # Axios 请求封装
├── docker-compose.yml             # 一键部署
└── README.md
```

## 微服务架构

```
                        ┌─────────────┐
                        │   Frontend   │  :3000
                        └──────┬──────┘
                               │ /api/*
                        ┌──────▼──────┐
                        │   Gateway    │  :8080
                        └──┬──┬──┬───┘
              ┌─────────────┼──┼──┼──────────────┐
              │             │  │  │              │
         ┌────▼───┐   ┌────▼──▼──▼────┐   ┌─────▼────┐
         │  User  │   │ Post/Comment  │   │   Like   │
         │:8081   │   │  :8082 / :8083│   │  :8084   │
         └───┬────┘   └───────┬───────┘   └─────┬────┘
             │                │                  │
         ┌───▼────────────────▼──────────────────▼───┐
         │            MySQL + Redis                  │
         └───────────────────────────────────────────┘
```

## 本地启动（Windows 开发环境）

### 前置条件

- JDK 8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0（本地运行，端口 3306）
- Redis 7（本地运行，端口 6379）

### 步骤

```bash
# 1. 创建数据库并导入表结构
mysql -u root -p < backend/sql/schema.sql

# 2. 编译后端模块
cd backend
mvn clean package -DskipTests

# 3. 按顺序启动后端服务
# 终端1: 用户服务
java -jar user-service/target/user-service-1.0.0.jar
# 终端2: 帖子服务
java -jar post-service/target/post-service-1.0.0.jar
# 终端3: 评论服务
java -jar comment-service/target/comment-service-1.0.0.jar
# 终端4: 点赞服务
java -jar like-service/target/like-service-1.0.0.jar
# 终端5: 网关服务
java -jar gateway-service/target/gateway-service-1.0.0.jar

# 4. 启动前端
cd frontend
npm install
npm run dev
# 访问 http://localhost:3000
```

**测试账号：** admin / 123456 （或 alice / 123456、bob / 123456）

## Docker 部署（Ubuntu 生产环境）

### 前置条件

- Docker 20+
- Docker Compose 2+

### 步骤

```bash
# 1. 将整个项目上传到 Ubuntu 服务器
# 2. 一键启动
docker-compose up -d --build

# 3. 查看状态
docker-compose ps

# 4. 访问
# 前端: http://服务器IP:3000
# 网关: http://服务器IP:8080

# 5. 停止
docker-compose down
```

> 数据库 SQL 首次启动自动初始化，Redis 数据持久化到 Docker 卷中。

## API 接口

所有请求通过网关统一转发，基础路径 `/api`

### 用户服务
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/user/register | 注册 |
| POST | /api/user/login | 登录 |
| GET | /api/user/info/{id} | 用户信息 |
| PUT | /api/user/update | 更新资料 |

### 帖子服务
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/post/list | 帖子列表 |
| GET | /api/post/{id} | 帖子详情 |
| POST | /api/post/create | 发布帖子 |
| PUT | /api/post/update | 更新帖子 |
| DELETE | /api/post/{id} | 删除帖子 |

### 评论服务
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/comment/post/{postId} | 获取评论 |
| POST | /api/comment/add | 添加评论 |
| DELETE | /api/comment/{id} | 删除评论 |

### 点赞服务
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/like/do | 点赞 |
| POST | /api/like/undo | 取消点赞 |
| GET | /api/like/count/{postId} | 点赞数 |
| GET | /api/like/status | 点赞状态 |
