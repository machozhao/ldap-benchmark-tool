1. JDK 1.6�����

2. ׼���û��ļ���ÿһ�а���һ���û����Ϳ���ֵ��ʹ��,�ָ�������
test01,password

3. ���ò��Ի�������, �޸�jar�е�config.properties�ļ�, �޸ĺ����滻�� jar����

LDAP.url=ldap://10.5.82.21:389
LDAP.user=cn=root
LDAP.password=passw0rd
# �û������ļ���·��
mock.user.file=c:/temp/users.csv
# ѹ�����Բ����߳���
max.threads=50

4. �������Գ���:
   java -jar ldap-benchmark-tool.jar
          ����������£�
   PerformanceMonitor [Thu May 03 14:13:41 CST 2012] [total success=127333, total failure=0, TPS success=582(Login/Sec), avg elapseTime=88(ms), max elapseTime=13259(ms), min elapseTime=4(ms)]
         ����:
         success��ʾ�ɹ��Ĳ������ܴ���(������֤�ɹ���ʧ��)
         failure��ʾ�쳣�������ܴ���
         TPS success ��ʾÿ��ɹ������Ĵ���
         avg/max/min elapse time �Ժ���Ƶ�һ���ɹ�������ƽ����������С��ʱ
         
ע�� �����Գ��򲻻��Զ���ֹ����Ҫ��ֹʱ������ɱ�����̣�
   