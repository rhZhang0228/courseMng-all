# sms

## 1、运行环境

建议使用nvm。环境使用的Node.js

## 2、如何运行

1. 运行`npm install`，下载前端所需要的依赖。
2. 开发环境直接`npm run dev`，即可实现运行。

## 3、如何部署

1. 运行`npm run build`，将前端项目进行打包，打包完成后，项目根目录下会出现build文件夹。
2. 运行 [Dockerfile](Dockerfile) ，将前端项目构建成docker镜像。
3. 后端运行相应的docker-compose.yml即可完成部署。
