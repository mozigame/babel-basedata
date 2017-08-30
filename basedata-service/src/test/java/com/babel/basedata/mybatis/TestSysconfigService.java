package com.babel.basedata.mybatis;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.babel.basedata.model.SysconfigPO;
import com.babel.basedata.service.ISysconfigService;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;

import junit.framework.TestCase;



public class TestSysconfigService extends TestCase{
	private static  ISysconfigService sysconfigService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(SysconfigPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
		sysconfigService = (ISysconfigService)act.getBean("sysconfigService");
	}
	
	@Test
	public void testFindSysconfigById() throws Exception{
		SysconfigPO sysconfig = sysconfigService.findSysconfigById(1l);
		Thread.sleep(500);
		sysconfig = sysconfigService.findSysconfigById(1l);
//		Thread.sleep(500);
		sysconfig = sysconfigService.findSysconfigById(1l);
		System.out.println(sysconfig);
		
		sysconfig = sysconfigService.findSysconfigById(2l);
		sysconfig = sysconfigService.findSysconfigById(2l);
		sysconfig = sysconfigService.findSysconfigById(2l);
		
		try {
			Thread.sleep(2000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindPageBySysconfig(){
		System.out.println("-----testFindPageBySysconfig----");
		SysconfigPO search = new SysconfigPO();
		search.setName("test");
		PageVO<SysconfigPO> page = new PageVO<SysconfigPO>(1, 10);
		PageVO<SysconfigPO> pages=sysconfigService.findPageBySysconfig(search, page);
		System.out.println(pages.getTotal());
		 for(SysconfigPO lp:pages.getRows()){
	        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getName());
	        }
		 
		
	}
	
	@Test
	public void testFindSysconfigAll() throws Exception{
		System.out.println("-----testFindPageBySysconfig----");
		SysconfigPO search = new SysconfigPO();
		search.setName("test");
		
		String code=null;
		RetResult<SysconfigPO> ret=sysconfigService.findSysconfigAll(null, code);
		System.out.println("flag="+ret.getFlag()+" size="+ret.getSize());
		if(!ret.isSuccess()){
			return ;
		}
	
		 for(SysconfigPO lp:ret.getDataList()){
	        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getName());
	        }
		 
		Thread.sleep(500l);
		ret=sysconfigService.findSysconfigAll(null, null);
		System.out.println("flag="+ret.getFlag()+" size="+ret.getSize());
	}

	

////	@Test
//	public void testAdd(){
//		SysconfigPO sysconfig = new SysconfigPO();
//		sysconfig.setName("test");
//		sysconfig.setCode("test");
//		sysconfig.setNameCn("test");
//		sysconfig.setNameEn("test");
//		sysconfigService.create(sysconfig);
//		
//	}
//	
//	@Test
//	public void testUpdate(){
//		SysconfigPO sysconfig = new SysconfigPO();
//		sysconfig.setCid(101l);
//		sysconfig.setName("test");
//		sysconfig.setCode("test");
//		sysconfig.setNameCn("test");
//		sysconfig.setNameEn("test");
//		sysconfig.setRemark("test");
//		sysconfigService.update(sysconfig);
//		
//		
//	}
	
	@Test
	public void testDelete(){
		SysconfigPO sysconfig = new SysconfigPO();
		sysconfig.setCid(101l);
		sysconfigService.deleteVirtual(1l, 101l);
	}
}
