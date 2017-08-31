package com.babel.basedata.mybatis;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.babel.basedata.model.LookupItemPO;
import com.babel.basedata.service.ILookupItemService;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.google.gson.Gson;

import junit.framework.TestCase;




public class TestLookupItemService{
	private static  ILookupItemService lookupItemService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(LookupItemPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		init();
	}
	
	public static void init() {
		System.out.println("--------init-----");
		try {
			act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
			lookupItemService = (ILookupItemService)act.getBean("lookupItemService");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSelectByLookupItem() throws Exception{
		LookupItemPO item = new LookupItemPO();
		List<LookupItemPO> list = lookupItemService.selectByLookupItem(item, 0, 1000);
		System.out.println(list.size());
		
//		String json=JSONObject.fromObject(ret.getFirstData()).toString();
//		System.out.println(json);
	}
	
	@Test
	public void testFindLookupById() throws Exception{
		RetResult<LookupItemPO> ret = lookupItemService.findLookupItemById(2l);
		System.out.println(ret.getFirstData());
		
		String json=new Gson().toJson(ret.getFirstData());
		System.out.println(json);
	}
	
	@Test
	public void testFindPageByLookup(){
		LookupItemPO search = new LookupItemPO();
		PageVO<LookupItemPO> page = new PageVO<LookupItemPO>(0, 10);
		PageVO<LookupItemPO> pages=lookupItemService.findPageByLookupItem(search, page);
		System.out.println(pages.getTotal());
		 for(LookupItemPO lp:pages.getRows()){
	        	System.out.println("cid="+lp.getCid()+" itemCode="+lp.getItemCode()+" itemName="+lp.getItemName());
	        }
	}
	
	private String getItemNames(Collection<LookupItemPO> list){
		String names="";
		for(LookupItemPO item:list){
			names+=item.getItemName()+",";
		}
		return names;
	}
	
//	@Test
	public void testFindLookupItemByLookupCode()throws Exception{
		this.lookupItemService.findLookupItemByLookupCode_flushCache("CN", "test3");
		RetResult<LookupItemPO> ret=this.lookupItemService.findLookupItemByLookupCode("CN", "test3");
		System.out.println(ret.getFlag()+" "+ret.getMsgBody());
		System.out.println(this.getItemNames(ret.getDataList()));
		TestCase.assertEquals("findLookupItemByLookupCode returnFlag=0", 0, ret.getFlag());
		TestCase.assertEquals("findLookupItemByLookupCode test3 size", 3, ret.getSize());
		
		Thread.sleep(500l);
		ret=this.lookupItemService.findLookupItemByLookupCode("CN", "test3");
		ret=this.lookupItemService.findLookupItemByLookupCode("CN", "test3");
		
		Thread.sleep(500l);
		LookupItemPO lookupItem=lookupItemService.findLookupItemById(40l).getFirstData();
		String itemName=lookupItem.getItemName();
		if("test1".equals(itemName)){
			itemName="test15";
		}
		else{
			itemName="test1";
		}
		lookupItem.setItemName(itemName);
		this.lookupItemService.update(lookupItem);
//		this.lookupItemService.flushCache("CN", "test3");
		ret=this.lookupItemService.findLookupItemByLookupCode("CN", "test3");
		System.out.println(this.getItemNames(ret.getDataList()));
		Thread.sleep(500l);
		ret=this.lookupItemService.findLookupItemByLookupCode("CN", "test3");
		ret=this.lookupItemService.findLookupItemByLookupCode("CN", "test3");
		Thread.sleep(500l);
		
	}
	
	

//	@Test
	public void add(){
		LookupItemPO lookupItem = new LookupItemPO();
		lookupItem.setLookupId(1l);
		lookupItem.setItemCode("test");
		lookupItem.setItemName("test");
//		RetResult<Long> ret=lookupItemService.create(lookupItem);
//		System.out.println(ret.getFirstData());
		
	}
//	@Test
	public void addList() throws Exception{
		System.out.println("---addList------");
		List<LookupItemPO> list=new ArrayList<>();
		LookupItemPO lookupItem = new LookupItemPO();
		lookupItem.setLookupId(50l);
		lookupItem.setItemCode("test");
		lookupItem.setItemName("test");
		list.add(lookupItem);
		
		lookupItem = new LookupItemPO();
		lookupItem.setLookupId(50l);
		lookupItem.setItemCode("test1");
		lookupItem.setItemName("test1");
		list.add(lookupItem);
		
//		lookupItem = new LookupItemPO();
//		lookupItem.setLookupId(50l);
//		lookupItem.setItemCode("test2");
//		lookupItem.setItemName("test2");
//		list.add(lookupItem);
//		lookupItem = new LookupItemPO();
//		lookupItem.setLookupId(50l);
//		lookupItem.setItemCode("test3");
//		lookupItem.setItemName("test3");
//		list.add(lookupItem);
//		lookupItem = new LookupItemPO();
//		lookupItem.setLookupId(50l);
//		lookupItem.setItemCode("test4");
//		lookupItem.setItemName("test4");
//		list.add(lookupItem);
//		RetResult<Long> ret=lookupItemService.create(lookupItem);
//		System.out.println(ret.getFirstData());
//		int status=this.lookupItemService.saveBatch(list);
//		System.out.println("----status="+status);
		Thread.sleep(500l);
	}
	
	@Test
	public void updateList() throws Exception{
		List<LookupItemPO> list=new ArrayList<>();
		LookupItemPO lookupItem = new LookupItemPO();
		lookupItem.setCid(259l);
		lookupItem.setItemCode("test");
		lookupItem.setItemName("test");
		lookupItem.setLanguage("EN");
		list.add(lookupItem);
		
		lookupItem = new LookupItemPO();
		lookupItem.setCid(260l);
		lookupItem.setItemCode("test");
		lookupItem.setItemName("test");
		lookupItem.setLanguage("EN");
		list.add(lookupItem);
//		
//		lookupItem = new LookupItemPO();
//		lookupItem.setCid(260l);
//		lookupItem.setItemCode("test");
//		lookupItem.setItemName("test");
//		lookupItem.setLanguage("EN");
//		list.add(lookupItem);
//		
//		lookupItem = new LookupItemPO();
//		lookupItem.setCid(260l);
//		lookupItem.setItemCode("test");
//		lookupItem.setItemName("test");
//		lookupItem.setLanguage("EN");
//		list.add(lookupItem);
		
		
//		RetResult<Long> ret=lookupItemService.create(lookupItem);
//		System.out.println(ret.getFirstData());
//		int status=this.lookupItemService.updateAllBatch(list);
//		int status=this.lookupItemService.updateNotNullBatch(list);
//		System.out.println("---status="+status);
		Thread.sleep(500l);
	}
	
	@Test
	public void testUpdate(){
		LookupItemPO lookupItem = new LookupItemPO();
		lookupItem.setCid(101l);
		lookupItem.setName("test");
		lookupItem.setCode("test");
		lookupItem.setItemCode("test");
		lookupItem.setItemName("test");
		lookupItem.setRemark("test");
//		lookupItemService.update(lookupItem);
		
		
	}
	
	@Test
	public void testDelete(){
		LookupItemPO lookupItem = new LookupItemPO();
		lookupItem.setCid(101l);
//		lookupItemService.deleteVirtual(1l, 101l);
	}
}
