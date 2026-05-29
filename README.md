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

---

## CI/CD 自动化部署

通过 GitHub Actions 实现：**代码推送 → 自动构建 Docker 镜像 → 推送阿里云镜像仓库 → 自动部署到服务器**

### 工作流程

```
你 git push 到 main 分支
         │
         ▼
   ┌─ GitHub Actions ─────────────────────┐
   │                                      │
   │  Job 1: build-and-push               │
   │   ├── 检出代码                       │
   │   ├── 登录阿里云容器镜像服务          │
   │   ├── 构建并推送 5 个后端镜像         │
   │   │   (user / post / comment / like / gateway)│
   │   └── 构建并推送前端镜像 (frontend)   │
   │                                      │
   │  Job 2: deploy (依赖 Job 1 完成)     │
   │   ├── SSH 连接 Ubuntu 服务器          │
   │   ├── docker compose pull             │
   │   ├── docker compose down             │
   │   └── docker compose up -d           │
   └──────────────────────────────────────┘
         │
         ▼
   部署完成 ✅
```

### 前置条件

- 代码已托管到 GitHub
- 阿里云容器镜像服务已有命名空间 `cyb_pull` 及镜像仓库 `blog_docker`
- Ubuntu 服务器已安装 Docker + Docker Compose

### 阿里云容器镜像服务准备

1. 登录 https://cr.console.aliyun.com
2. 创建命名空间（如 `cyb_pull`）
3. 创建镜像仓库 `blog_docker`（选择"本地仓库"类型，无需绑定代码源）
4. 在左侧"访问凭证"中获取**专用用户名**和**专用密码**（用于 `docker login`）

### GitHub Secrets 配置

进入 GitHub 仓库 → **Settings → Secrets and variables → Actions → New repository secret**，添加以下 6 个密钥：

| Secret 名称 | 说明 |
|---|---|
| `ALIYUN_REGISTRY_USER` | 阿里云容器镜像服务的专用用户名（非阿里云账号） |
| `ALIYUN_REGISTRY_PASSWORD` | 阿里云容器镜像服务的专用密码 |
| `SERVER_HOST` | Ubuntu 服务器 IP 地址 |
| `SERVER_USER` | Ubuntu SSH 用户名（如 `root` 或 `ubuntu`） |
| `SERVER_SSH_KEY` | 服务器的 SSH 私钥，服务器上执行 `cat ~/.ssh/id_rsa` 获取 |

### 服务器目录准备

确保 Ubuntu 服务器上存在以下文件结构：

```
/home/ubuntu/blog-forum/
├── docker-compose.yml      # 使用 image: 引用的生产配置（已提交到仓库）
└── backend/
    └── sql/
        └── schema.sql      # 首次初始化数据库用
```

### 首次手动部署

```bash
# SSH 登录服务器
ssh root@your_server_ip

# 创建目录并拉取配置文件
mkdir -p /home/ubuntu/blog-forum/backend/sql
cd /home/ubuntu/blog-forum

# 方式一：从 GitHub 拉取（推荐）
git clone https://github.com/你的用户名/blog-forum.git .

# 方式二：手动上传 docker-compose.yml 和 schema.sql

# 启动服务
docker compose pull
docker compose up -d

# 查看状态
docker compose ps
```

### 触发自动部署

推送代码到 GitHub main 分支即自动触发：

```bash
git add .
git commit -m "feat: 新功能"
git push origin main
```

**查看流水线状态：** GitHub 仓库 → **Actions** 标签页

### 首次部署与数据持久化

- 首次启动 MySQL 时，`schema.sql` 自动初始化数据库表结构和测试数据
- MySQL 和 Redis 数据存储在 Docker 命名卷中（`mysql-data`、`redis-data`），`docker compose down` 不会丢失数据
- 如需重置数据库，执行 `docker compose down -v`（删除卷）再启动

### 常见问题

**Q: 流水线运行失败，提示登录阿里云 Registry 失败？**

检查 GitHub Secrets 中的 `ALIYUN_REGISTRY_USER` 和 `ALIYUN_REGISTRY_PASSWORD` 是否正确。注意这是**容器镜像服务的专用密码**，不是阿里云登录密码。在阿里云控制台 → 容器镜像服务 → 访问凭证中设置。

**Q: 服务器上 docker compose pull 很慢？**

首次拉取镜像可能需要一些时间。之后只有变更的镜像会增量拉取。也可以考虑在非高峰时段部署。

**Q: 推送后服务器没有更新？**

检查 GitHub Actions 是否运行成功，以及服务器上 `docker compose ps` 确认所有容器是否正常运行。查看日志：`docker compose logs -f`。
