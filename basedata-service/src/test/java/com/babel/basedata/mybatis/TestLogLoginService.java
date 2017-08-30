package com.babel.basedata.mybatis;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.babel.basedata.model.LogLoginPO;
import com.babel.basedata.service.ILogLoginService;
import com.babel.common.core.page.PageVO;



public class TestLogLoginService {
	private static  ILogLoginService logLoginService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(LogLoginPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
		logLoginService = (ILogLoginService)act.getBean("logLoginService");
	}
	
//	@Test
//	public void testFindLookupById(){
//		LogLoginPO lookup = logLoginService.findLookupById(1l);
//		System.out.println(lookup);
//	}
	
	@Test
	public void testFindPageByLogLogin(){
		LogLoginPO search = new LogLoginPO();
//		search.setNameCn("test");
		
		PageVO<LogLoginPO> page = new PageVO<LogLoginPO>(1, 10);
		PageVO<LogLoginPO> pages=logLoginService.findPageByLogLogin(search, page);
		System.out.println(pages.getTotal());
		 for(LogLoginPO lp:pages.getRows()){
	        	System.out.println("cid="+lp.getCid()+" ip="+lp.getIp()+" userName="+lp.getUserName());
	        }
	}
	
//	@Test
	public void te1stCreateBatch(){
//		List<LogLoginPO> list=new ArrayList();
//		LogLoginPO log = new LogLoginPO();
//		log.setMsgType(0);
//		log.setMsgCode("test");
//		log.setTitle("test");
//		log.setSendType(1);
//		log.setContent("test");
//		list.add(log);
//		log = new LogLoginPO();
//		log.setMsgType(0);
//		log.setMsgCode("test");
//		log.setTitle("test1");
//		log.setSendType(1);
//		log.setContent("test1");
//		list.add(log);
//		log = new LogLoginPO();
//		log.setMsgType(0);
//		log.setMsgCode("test");
//		log.setTitle("test2");
//		log.setSendType(1);
//		log.setContent("test2");
//		list.add(log);
//		this.logLoginService.insertBatch(list);
	}
	
//	@Test
//	public void testSelectByLookup(){
//		LogLoginPO search = new LogLoginPO();
//		List<LogLoginPO> list=logLoginService.selectByLogLogin(search, 1, 10);
//		System.out.println(list.size());
//		for(LogLoginPO lp:list){
//        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getNameCn());
//        }
//	}

}
