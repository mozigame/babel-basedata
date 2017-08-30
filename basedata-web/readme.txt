1,线程池：系统使用统一线程池管理器TaskExecutorUtils来获得生成的线程池，如果只是异步处理，而不是并发则共用统一的getExecutorSingle，否则可以独立线程池：getTaskExecutorInstance
用TaskExecutorUtils管理线程池可自动将线程参数数据写到tf_thread_pool表
并允许通过修改tf_thread_pool表来更改线程池的参数，系统每次获取线程池都会在最短5秒异步检查一下tf_thread_pool看是否有修改，有修改就重载线程缓存参数，每次获取线程池都会获取这些参数
系统启动时，首次获取线程池时会加载tf_thread_pool中的参数配置，没有找到则自动创建到tf_thread_pool
查看线程池数据：http://localhost:8080/serverInfo?getType=poolInfo
2,系统参数tf_sysconfig
在系统启动时会自动按sysType即code的开头部份加载tf_sysconfig，如：app./sys.开头的参数
在后面则每次调用时，最短5秒钟一次检查tf_sysconfig表是否有修改，有修改就重新加载tf_sysconfig的参数到缓存
Sysconfig.getValue(code, defaultValue)如果参数未配置则使用defaultValue
Sysconfig.getValue(code, defaultValue, desc,parent_code)允许在代中自动将配置默认值写到数据库

3，数据字典
系统启动时或修改时会自动从数据库tf_lookup,tf_lookup_item生成comm.dict.js以供前台js使用
需要将类型转成名称显示时，需要在comm.render.js中写对应的render转换，支持颜色




4,日志到数据库
所有日志的保存先存到redis，然后通过异步任务从redis批量更新到数据库，3秒一次
1，@LogAudit审计日志，可以加到Controller层，可记录操作人的IP，请求信息等信息
2，@LogService记录service的日志
3，sql日志，默认全部开启，要取消可在spring-mybatis.xml配置文件的sqlSessionFactory中配置sql.ignoreSqlId


5，系统信息查看：http://localhost:8080/serverInfo?getType=$type
此功能处理安全考虑，开发或测试环境不限，但生产环境会限白名单，即只有白名单的IP才可以查看，具体在系统参数的白名单管理中配置
$type=poolInfo线程池信息
$type=envInfo系统参数信息
$type=userEnvInfo用户参数信息
$type=memoryInfo内存信息
$type=mbean内存/gc及线程信息
$type=requestInfo请求信息
$type=userPermit用户权限信息
$type=onlineUser在线用户信息
$type=dataSource数据库连接池信息
$type=retryRule重试规则


6，开机启动配置（用的是多线程处理，不保证顺序）
因spring的加载顺序先先mvc的加载，所以b的加载早于a
a,利用tomcat的启动项ContextListener，对应的任务需要实现IContextTaskLoader类，然后配置configWebUtil的contextTaskBeanName，如lookupJsResetTaskLoader等
b,利用spring的启动首次加载，配置SysconfigsLoadListener的invokeMethods，支持直接配置spring的beanName及对应方法，可配置多个，支持任何无参的spring方法


7,白名单

8，重试规则

9，配置说明
文件配置server-dev.properties用于配置数据库，密钥等信息
系统参数配置，在系统内配置
用户参数配置，在系统内配置
数据字典，在系统内配置
线程池，初始在代码中配置，后续可以在系统内修改


获取sequenceId
nextSeqId

批量获取
nextSeqIds


待处理：
propertyIgnore的mapper可以改成启动时从数据库自动识别
菜单可以改成从功能注解自动识别
mapper保存后直接返回cid



