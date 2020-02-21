#!/usr/bin/bash
export JAVA_HOME=/usr/local/jdk1.8
export JRE_HOME=/$JAVA_HOME/jre
export CLASSPATH=.:$JAVA_HOME/jre/lib/rt.jar
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin

#这里可替换为你自己的执行程序，其他代码无需更改

APP_NAME=./question-admin.jar
#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh start.sh [start|stop|restart|status]"
    exit 1
}

#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}'`
  #如果不存在返回1，存在返回0
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

#启动方法
start(){
        echo `java -version`
  is_exist
  if [ $? -eq 0 ]; then
    echo "${APP_NAME} is already running. pid=${pid}"
  else
    nohup java -Xms512M -Xmx512M  -jar  ${APP_NAME} --spring.profiles.active=pro >question-admin.out 2>&1 &