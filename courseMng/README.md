# [courseMng]() 

## 1、运行环境和技术栈

jdk8 + Maven + MySQL +Redis + MinIO + Docker

## 2、如何运行和部署

1. maven打包代码`mvn clean package -DskipTests` 
2. 运行 [Dockerfile](Dockerfile) 文件（注意修改配置文件中application.yml配置的MySQL、Redis、MinIO等的地址），同时前端也进行打包并且运行Dockerfile将项目构建为docker镜像
3. 运行 [docker-compose.yml](docker-compose.yml) ，可以通过idea快速运行
4. 将项目文件下 [all.sql](all.sql) 导入docker容器中的MySQL容器即可

## 3、如何访问

前端+后端是部署在docker中的，同时，前端是部署在nginx里的。所以，只需要浏览器直接输入运行docker的服务器/虚拟机的ip即可。

