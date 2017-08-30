package com.babel.basedata.mybatis;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.babel.basedata.model.LogDbPO;
import com.babel.basedata.service.ILogDbService;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;

import junit.framework.TestCase;



public class TestLogDbService extends TestCase{
	private static  ILogDbService logDbService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(LogDbPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		try {
			act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
			logDbService = (ILogDbService)act.getBean("logDbService");
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testFindLookupById(){
//		LogDbPO lookup = logDbService.findLookupById(1l);
//		System.out.println(lookup);
//	}
	
	@Test
	public void testFindPageByLogDb(){
		LogDbPO search = new LogDbPO();
//		search.setNameCn("test");
		
		try {
			PageVO<LogDbPO> page = new PageVO<LogDbPO>(1, 10);
			PageVO<LogDbPO> pages=logDbService.findPageByLogDb(search, page);
			System.out.println(pages.getTotal());
			 for(LogDbPO lp:pages.getRows()){
			    	System.out.println("cid="+lp.getCid()+" title="+lp.getTitle()+" descs="+lp.getDescs());
			    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testStaticIntfCallCount() throws Exception{
		Date startDate=null;
		Date endDate=null;
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("runTime", 10);
		paramMap.put("intfCode", "LogDbMapper.selectByExample");
		RetResult<Map<String, Object>> list=this.logDbService.staticIntfCallCount(startDate, endDate, paramMap);
		System.out.println(list.getDataList());
		
		Thread.sleep(500l);
	}
	
	@Test
	public void testStaticIntfCodeTopCount()  throws Exception{
		Date startDate=null;
		Date endDate=null;
		Map<String, Object> paramMap=new HashMap<String, Object>();
		paramMap.put("runTime", 10);
		paramMap.put("intfCode", "LogDbMapper.selectByExample");
		Integer topN=20;
		RetResult<Map<String, Object>> list=this.logDbService.staticIntfCodeTopCount(startDate, endDate, topN, paramMap);
		System.out.println(list.getDataList());
		Thread.sleep(500l);
	}
	
//	@Test
//	public void testSelectByLookup(){
//		LogDbPO search = new LogDbPO();
//		List<LogDbPO> list=logDbService.selectByLogDb(search, 1, 10);
//		System.out.println(list.size());
//		for(LogDbPO lp:list){
//        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getNameCn());
//        }
//	}

}
