1. JDK 1.6或更高

2. 准备用户文件，每一行包含一对用户名和口令值，使用,分隔，例如
test01,password

3. 设置测试环境参数, 修改jar中的config.properties文件, 修改后请替换回 jar包中

LDAP.url=ldap://10.5.82.21:389
LDAP.user=cn=root
LDAP.password=passw0rd
# 用户数据文件的路径
mock.user.file=c:/temp/users.csv
# 压力测试并发线程数
max.threads=50

4. 启动测试程序:
   java -jar ldap-benchmark-tool.jar
          输出内容如下：
   PerformanceMonitor [Thu May 03 14:13:41 CST 2012] [total success=127333, total failure=0, TPS success=582(Login/Sec), avg elapseTime=88(ms), max elapseTime=13259(ms), min elapseTime=4(ms)]
         其中:
         success表示成功的操作的总次数(包括认证成功和失败)
         failure表示异常操作的总次数
         TPS success 表示每秒成功操作的次数
         avg/max/min elapse time 以毫秒计的一个成功操作的平均、最大和最小耗时
         
注： 本测试程序不会自动终止，需要终止时请自行杀掉进程！
   