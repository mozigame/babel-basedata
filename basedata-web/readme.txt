1,�̳߳أ�ϵͳʹ��ͳһ�̳߳ع�����TaskExecutorUtils��������ɵ��̳߳أ����ֻ���첽���������ǲ�������ͳһ��getExecutorSingle��������Զ����̳߳أ�getTaskExecutorInstance
��TaskExecutorUtils�����̳߳ؿ��Զ����̲߳�������д��tf_thread_pool��
������ͨ���޸�tf_thread_pool���������̳߳صĲ�����ϵͳÿ�λ�ȡ�̳߳ض��������5���첽���һ��tf_thread_pool���Ƿ����޸ģ����޸ľ������̻߳��������ÿ�λ�ȡ�̳߳ض����ȡ��Щ����
ϵͳ����ʱ���״λ�ȡ�̳߳�ʱ�����tf_thread_pool�еĲ������ã�û���ҵ����Զ�������tf_thread_pool
�鿴�̳߳����ݣ�http://localhost:8080/serverInfo?getType=poolInfo
2,ϵͳ����tf_sysconfig
��ϵͳ����ʱ���Զ���sysType��code�Ŀ�ͷ���ݼ���tf_sysconfig���磺app./sys.��ͷ�Ĳ���
�ں�����ÿ�ε���ʱ�����5����һ�μ��tf_sysconfig���Ƿ����޸ģ����޸ľ����¼���tf_sysconfig�Ĳ���������
Sysconfig.getValue(code, defaultValue)�������δ������ʹ��defaultValue
Sysconfig.getValue(code, defaultValue, desc,parent_code)�����ڴ����Զ�������Ĭ��ֵд�����ݿ�

3�������ֵ�
ϵͳ����ʱ���޸�ʱ���Զ������ݿ�tf_lookup,tf_lookup_item����comm.dict.js�Թ�ǰ̨jsʹ��
��Ҫ������ת��������ʾʱ����Ҫ��comm.render.js��д��Ӧ��renderת����֧����ɫ




4,��־�����ݿ�
������־�ı����ȴ浽redis��Ȼ��ͨ���첽�����redis�������µ����ݿ⣬3��һ��
1��@LogAudit�����־�����Լӵ�Controller�㣬�ɼ�¼�����˵�IP��������Ϣ����Ϣ
2��@LogService��¼service����־
3��sql��־��Ĭ��ȫ��������Ҫȡ������spring-mybatis.xml�����ļ���sqlSessionFactory������sql.ignoreSqlId


5��ϵͳ��Ϣ�鿴��http://localhost:8080/serverInfo?getType=$type
�˹��ܴ���ȫ���ǣ���������Ի������ޣ��������������ް���������ֻ�а�������IP�ſ��Բ鿴��������ϵͳ�����İ���������������
$type=poolInfo�̳߳���Ϣ
$type=envInfoϵͳ������Ϣ
$type=userEnvInfo�û�������Ϣ
$type=memoryInfo�ڴ���Ϣ
$type=mbean�ڴ�/gc���߳���Ϣ
$type=requestInfo������Ϣ
$type=userPermit�û�Ȩ����Ϣ
$type=onlineUser�����û���Ϣ
$type=dataSource���ݿ����ӳ���Ϣ
$type=retryRule���Թ���


6�������������ã��õ��Ƕ��̴߳�������֤˳��
��spring�ļ���˳������mvc�ļ��أ�����b�ļ�������a
a,����tomcat��������ContextListener����Ӧ��������Ҫʵ��IContextTaskLoader�࣬Ȼ������configWebUtil��contextTaskBeanName����lookupJsResetTaskLoader��
b,����spring�������״μ��أ�����SysconfigsLoadListener��invokeMethods��֧��ֱ������spring��beanName����Ӧ�����������ö����֧���κ��޲ε�spring����


7,������

8�����Թ���

9������˵��
�ļ�����server-dev.properties�����������ݿ⣬��Կ����Ϣ
ϵͳ�������ã���ϵͳ������
�û��������ã���ϵͳ������
�����ֵ䣬��ϵͳ������
�̳߳أ���ʼ�ڴ��������ã�����������ϵͳ���޸�


��ȡsequenceId
nextSeqId

������ȡ
nextSeqIds


������
propertyIgnore��mapper���Ըĳ�����ʱ�����ݿ��Զ�ʶ��
�˵����Ըĳɴӹ���ע���Զ�ʶ��
mapper�����ֱ�ӷ���cid



