package com.babel.basedata.mybatis;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.babel.basedata.model.LookupPO;
import com.babel.basedata.service.ILookupService;
import com.babel.basedata.util.Sysconfigs;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.util.CommMethod;
import com.babel.common.core.util.ObjectToMapUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.TestCase;



public class TestLookupService extends TestCase{
	private static  ILookupService lookupService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(LookupPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
		lookupService = (ILookupService)act.getBean("lookupService");
	}
	
	@Test
	public void testFindLookupById() throws Exception{
		LookupPO lookup = lookupService.findLookupById(1l);
		Thread.sleep(500);
		lookup = lookupService.findLookupById(1l);
//		Thread.sleep(500);
		lookup = lookupService.findLookupById(1l);
		System.out.println(lookup);
		
		lookup = lookupService.findLookupById(2l);
		lookup = lookupService.findLookupById(2l);
		lookup = lookupService.findLookupById(2l);
		
		try {
			Thread.sleep(2000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindPageByLookup(){
		System.out.println("-----testFindPageByLookup----");
		LookupPO search = new LookupPO();
		search.setNameCn("test");
		PageVO<LookupPO> page = new PageVO<LookupPO>(1, 10);
		PageVO<LookupPO> pages=lookupService.findPageByLookup(search, page);
		System.out.println(pages.getTotal());
		 for(LookupPO lp:pages.getRows()){
	        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getNameCn());
	        }
		 
		
	}
	
	@Test
	public void testFindLookupMapByPage() throws Exception{
		System.out.println("-----testFindLookupMapByPage----");
		HashMap search = new HashMap();
		search.put("nameCn", "test");
		PageVO<HashMap> page = new PageVO<HashMap>(1, 10);
		PageVO<HashMap> pages=lookupService.findLookupMapByPage(search, page);
		System.out.println(pages.getTotal());
		 for(HashMap lp:pages.getRows()){
	        	System.out.println("cid="+lp.get("cid")+" code="+lp.get("code")+" nameCn="+lp.get("nameCn"));
	        }
		 
		 System.out.println("----1="+Sysconfigs.getEnvMap());
		 Thread.sleep(5010l);
		 System.out.println("----2="+Sysconfigs.getEnvMap());
		 Thread.sleep(5010l);
		 System.out.println("----3="+Sysconfigs.getEnvMap());
		
	}
	
	@Test
	public void testSelectByLookup(){
		System.out.println("----testSelectByLookup---");
//		System.out.println("----envMap="+Sysconfigs.getEnvMap());
		
		long time=System.currentTimeMillis();
		LookupPO search = new LookupPO();
		
		
		try {
			List<LookupPO> list=lookupService.selectByLookup(search, 0, 200);
			List<Map<String, Object>> mapList=ObjectToMapUtil.getDataMapListByPropName(list, null, null);
//			System.out.println(mapList);
			CommMethod.print(mapList);
			System.out.println(list.size());
			for(LookupPO lp:list){
	        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getNameCn());
	        }
			
			System.out.println("---end---"+(System.currentTimeMillis()-time));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("----sleeping---");
		try {
			Thread.sleep(2000l);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("---end2---"+(System.currentTimeMillis()-time));
	}

//	@Test
	public void testAdd(){
		LookupPO lookup = new LookupPO();
		lookup.setName("test");
		lookup.setCode("test");
		lookup.setNameCn("test");
		lookup.setNameEn("test");
		lookupService.create(lookup);
		
	}
	
	@Test
	public void testUpdate(){
		LookupPO lookup = new LookupPO();
		lookup.setCid(101l);
		lookup.setName("test");
		lookup.setCode("test");
		lookup.setNameCn("test");
		lookup.setNameEn("test");
		lookup.setRemark("test");
//		lookupService.update(lookup);
		Gson gson = new GsonBuilder().serializeNulls().create();
		System.out.println(gson.toJson(lookup));
		
		
	}
	
	@Test
	public void testDelete() throws Exception{
		LookupPO lookup = new LookupPO();
		lookup.setCid(101l);
		RetResult<Integer> ret=lookupService.deleteVirtual(1l, 101l);
		System.out.println("flag="+ret.getFlag()+" firstData="+ret.getFirstData());
	}
}
