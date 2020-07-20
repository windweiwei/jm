set JAVA_HOME=D:\java\jdk1.8
set APP_HOME=E:\svnc\newrest
set APP_MAIN=com.manniu.Application
set JAVA_OPTS=-server -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=%APP_HOME%/tmp -XX:+UseConcMarkSweepGC -XX:MaxGCPauseMillis=500
java %JAVA_OPTS% -cp %APP_HOME%\conf\;%APP_HOME%\lib\*;. %APP_MAIN%