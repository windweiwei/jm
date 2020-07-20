#!/bin/sh
JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.252.b09-2.el7_8.x86_64/jre
APP_HOME=$(cd "$(dirname "$0")"; cd ..; pwd)

APP_MAIN=com.jimang.RestApplication

APP_PID_FILE=$APP_HOME/tmp/rest.pid

JAVA_OPTS="-server -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$APP_HOME/tmp -XX:+UseConcMarkSweepGC -XX:MaxGCPauseMillis=500 -XX:NewSize=84M -XX:OldSize=168M -XX:CMSTriggerInterval=1800000"

PATH=$JAVA_HOME/bin:$PATH
export JAVA_HOME PATH APP_HOME

if [ ! -d $APP_HOME/tmp ];then
    mkdir $APP_HOME/tmp
fi

if [ ! -d $APP_HOME/logs ];then
    mkdir $APP_HOME/logs
fi

startup()
{
    if [ `ps x |grep -v grep|grep $APP_MAIN|wc -l` -eq 0 ];then
        if [ -f $APP_PID_FILE ];then
            rm $APP_PID_FILE
        fi
    fi
    if [ ! -f $APP_PID_FILE ];then
        nohup java $JAVA_OPTS -cp $APP_HOME/conf/:$APP_HOME/lib/*:. $APP_MAIN 2>>$APP_HOME/logs/rest.out 1>>$APP_HOME/logs/rest.out &
        echo $!>$APP_PID_FILE
        #判断服务是否正常启动成功
        APP_PID=`cat $APP_PID_FILE`
        if [ `ps -ef |grep $APP_PID |grep -v "grep"|wc -l` -eq 0 ]; then
            if [ -f $APP_PID_FILE ];then
                rm $APP_PID_FILE
            fi
            echo 'rest server start up failure.'
        else
             echo 'rest server start up successfully.'
        fi
    else
        echo 'rest server is already running.'
    fi
}

checkPidExist()
{
    pids=`ps x|grep -v grep|grep $APP_MAIN|awk '{print $1}'`
    r=0
    for id in $pids
    do
        if [ $id -eq `cat $APP_PID_FILE` ];then
        r=1
            break
    fi
    done
    return $r
}

shutdown()
{
    if [ -f $APP_PID_FILE ];then
        kill `cat $APP_PID_FILE`
        checkPidExist
        while [ $? -eq 1 ]
        do
            echo -n '.'
            sleep 1
            checkPidExist
        done
        echo '.'
        rm $APP_PID_FILE
        echo 'rest service shutdown successfully.'
    else
        echo 'rest service is not running.'
    fi
}

case $1 in
    start)
        startup
        ;;
    stop)
        shutdown
        ;;
    restart)
        shutdown
        startup
        ;;
esac