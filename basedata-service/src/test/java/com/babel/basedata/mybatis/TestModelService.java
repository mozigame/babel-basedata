package com.babel.basedata.mybatis;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.babel.basedata.model.ModelPO;
import com.babel.basedata.service.IModelService;
import com.babel.common.core.page.PageVO;

import junit.framework.TestCase;



public class TestModelService extends TestCase{
	private static  IModelService modelService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(ModelPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
		modelService = (IModelService)act.getBean("modelService");
	}
	
	@Test
	public void testFindModelById() throws Exception{
		ModelPO model = modelService.findModelById(2l);
		System.out.println(model);
		

	}
	
	@Test
	public void testFindModelByCode() throws Exception{
//		RetResult<ModelPO> model = modelService.findModelByCode("LoginController", "defaultPage");
//		System.out.println(model.getFirstData());
//		
//		
//		
//		Thread.sleep(500l);
//		model = modelService.findModelByCode("LoginController", "login");
//		System.out.println(model.getFirstData());

	}
	
	@Test
	public void testFindPageByModel(){
		System.out.println("-----testFindPageByModel----");
		ModelPO search = new ModelPO();
		search.setName("test");
		PageVO<ModelPO> page = new PageVO<ModelPO>(1, 10);
		PageVO<ModelPO> pages=modelService.findPageByModel(search, page);
		System.out.println(pages.getTotal());
		 for(ModelPO lp:pages.getRows()){
	        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getName());
	        }
		 
		
	}

}
