FROM mysql:8.0

# 把数据库脚本放进镜像
COPY schema.sql /docker-entrypoint-initdb.d/
