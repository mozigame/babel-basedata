package com.babel.basedata.util;





import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.babel.common.core.util.SpringContextUtil;

/**
 * spring容器，启动后加载
 * @author jinhe.chen
 *
 */
@Service
public class SysconfigsLoadListener implements ApplicationListener<ContextRefreshedEvent>
{
	 private static final Log logger = LogFactory.getLog(SysconfigsLoadListener.class);
	 private static boolean isLoadMap=false;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if(event.getApplicationContext().getParent() == null)//root application context 没有parent，他就是老大.
        {  
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。  
        	if( !isLoadMap){
        		logger.info("-----load1 "+event.getApplicationContext().getDisplayName());
        		this.invokeClassOnLoad();
        	}
        }  
        
        //或者下面这种方式
        if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext"))
        {
        	if(!isLoadMap){
        		logger.info("-----load2 "+event.getApplicationContext().getDisplayName());
        		this.invokeClassOnLoad();
        	}
        }
        
        
    }
    
    private void invokeClassOnLoad(){
    	logger.info("-----invokeClassOnLoad--sysconfigs.envMap="+Sysconfigs.getEnvMap());
//    	logger.info("-----invokeClassOnLoad--sysconfigs.envCustomerMap="+Sysconfigs.getEnvCustomerMap());
    	if(this.invokeMethods!=null){
    		logger.info("-----invokeClassOnLoad--invokeMethods="+this.invokeMethods);
    		//Sysconfigs.getEnvMap()需要放在初始前面
    		isLoadMap=true;
    		int count=0;
    		Set<Map.Entry<Object, Object>> entrys=this.invokeMethods.entrySet();
    		String beanName=null;
    		String methodStr=null;
    		for(Map.Entry<Object, Object> entry:entrys){
    			beanName=(String)entry.getKey();
    			methodStr=(String)entry.getValue();
    			if(methodStr!=null){
    				methodStr=methodStr.trim();
    				String[] methods=methodStr.split(",");
    				for(String methodName:methods){
    					count++;
    					this.invokeClassMethod(beanName, methodName);;
    				}
    			}
    		}
    		logger.info("-----invokeClassOnLoad count="+count);
    	}
    }
    
    /**
     * 调用指定spring服务类的接口方法
     * @param beanName
     * @param methodName
     */
    private void invokeClassMethod(String beanName, String methodName){
    	logger.info("-----invokeClassMethod--beanName="+beanName+" methodName="+methodName);
//    	String beanName="userVisitCheckService";
		if(SpringContextUtil.containsBean(beanName)){
			Object object=SpringContextUtil.getBean(beanName);
			Method[] methods=object.getClass().getMethods();
			boolean isExistMethod=false;
			for(final Method m:methods){
				if(m.getName().equals(methodName)){
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								m.invoke(object, null);
							} catch (Exception e) {
								logger.warn("-----invokeClassMethod--method:"+beanName+"."+methodName+",error:"+e.getMessage(), e);
							}
						}
					});
					isExistMethod=true;
					thread.start();
					break;
				}
			}
			if(!isExistMethod){
				logger.warn("-----invokeClassMethod--method:"+beanName+"."+methodName+" not found");
			}
		}
		else{
			logger.warn("-----initUserVisitData--beanName:"+beanName+" not found");
		}
		
    }
    
    private Properties invokeMethods;
	public Properties getInvokeMethods() {
		return invokeMethods;
	}

	public void setInvokeMethods(Properties invokeMethods) {
		this.invokeMethods = invokeMethods;
	}
	
    

}