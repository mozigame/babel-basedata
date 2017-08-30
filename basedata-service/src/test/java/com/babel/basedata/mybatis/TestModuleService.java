package com.babel.basedata.mybatis;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.babel.basedata.model.ModulePO;
import com.babel.basedata.service.IModuleService;
import com.babel.common.core.page.PageVO;

import junit.framework.TestCase;



public class TestModuleService extends TestCase{
	private static  IModuleService moduleService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(ModulePO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
		moduleService = (IModuleService)act.getBean("moduleService");
	}
	
	@Test
	public void testFindModuleById() throws Exception{
		ModulePO module = moduleService.findModuleById(2l);
		System.out.println(module);
		

	}
	
	@Test
	public void testFindPageByModule(){
		System.out.println("-----testFindPageByModule----");
		ModulePO search = new ModulePO();
		search.setName("test");
		PageVO<ModulePO> page = new PageVO<ModulePO>(1, 10);
		PageVO<ModulePO> pages=moduleService.findPageByModule(search, page);
		System.out.println(pages.getTotal());
		 for(ModulePO lp:pages.getRows()){
	        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getName());
	        }
		 
		
	}
	

	

//	@Test
	public void testAdd(){
		ModulePO module = new ModulePO();
		module.setName("test");
		module.setCode("test");
//		moduleService.create(module);
		
	}
	
	@Test
	public void testUpdate(){
		ModulePO module = new ModulePO();
		module.setCid(101l);
		module.setName("test");
		module.setCode("test");
		module.setRemark("test");
//		moduleService.update(module);
		
		
	}
	
	@Test
	public void testDelete(){
		ModulePO module = new ModulePO();
		module.setCid(101l);
//		moduleService.deleteVirtual(1l, 101l);
	}
}
