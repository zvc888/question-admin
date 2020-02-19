# question-admin 刷题管理平台
[gitee地址](https://gitee.com/zvc888/question-admin.git)

1. [JAVA接口服务](https://github.com/zvc888/question-admin.git)
2. [后端管理页面](https://github.com/zvc888/question-admin-console.git)
3. [小程序](https://github.com/zvc888/question-wx.git)

## 本项目为前后端分离的Web应用后端程序，采用技术框架如下：
1. springboot v2.1.2.RELEASE
2. shiro
3. jwt
4. redis
5. mybatis-plus v3.1.2 

使用jwt采用token有效期内刷新机制更新Token。
## 项目已实现功能包括：

**一、系统管理**
1. 用户登录
2. 用户管理
3. 角色管理
4. 权限管理
5. 菜单管理
6. 登录日志
7. 系统日志

**二、运维管理**

1. 接口文档
2. 用户反馈

**三 、卷题管理**

1. 类别管理
2. 科目管理
3. 试卷管理
4. 题目管理
5. 轮播管理


## 开发环境&安装

**一、开发环境**
1. Mysql
2. Jdk8+
3. Redis

**二、初始化**
1. db目录下的sql文件执行
2. 文件系统使用的七牛存储（按照七牛官网文档去申请配置后有bucket，access-key，secret-key）
3. 微信小程序（appid，secret）

**三、启动**
1. 启动redis
2. 启动mysql
3. 运行AdminApplication类

![demo](https://s2.ax1x.com/2020/02/19/3VMVDf.png)

# 喜欢的帮忙star，谢谢啦

<img src="https://s2.ax1x.com/2020/02/19/3VMnUg.md.png" alt="打赏" width="450" height="450" align="center" />