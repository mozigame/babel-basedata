package com.babel.basedata.mybatis;

import java.util.Map;

import org.junit.Test;

import com.babel.basedata.model.LookupPO;
import com.babel.basedata.model.SysconfigPO;
import com.babel.common.core.util.ObjectToMapUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestGson {
	@Test
	public void testGson() throws Exception{
		LookupPO lookup = new LookupPO();
		lookup.setCid(101l);
		lookup.setName("test");
		lookup.setCode("test");
		lookup.setNameCn("test");
		lookup.setNameEn("test");
		lookup.setRemark("test");
//		lookupService.update(lookup);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		Gson gson = new Gson();
		System.out.println(gson.toJson(lookup));
		
	}
	
	@Test
	public void testMap() throws Exception{
		SysconfigPO sys=new SysconfigPO();
		sys.setCid(1l);
		sys.setPid(123l);
		sys.setCode("test");
		sys.setName("test=aaa");
		
//		Map<String, Object> map=ObjectToMapUtil.objectToMap(sys);
		Map map=ObjectToMapUtil.objectToMap(sys);
		System.out.println(map);
		
		SysconfigPO sys2=(SysconfigPO)ObjectToMapUtil.mapToObject(map, SysconfigPO.class);
		System.out.println(sys2.getCode()+" "+sys2.getCid());
	}
	
	private Gson gson = new Gson();
//	@Test
//	public void testGson2(){
//		TemplateBody templateBody=new TemplateBody();
//		templateBody.setUrl("http://cap.yc.ai/law_details?consultId=1019&consultId=1019");
//		System.out.println(gson.toJson(templateBody));
//		String string=gson.toJson(templateBody);
//		if(string.indexOf("\\u003d")>0){
//			string=string.replaceAll("\\\\u003d", "=");
//			System.out.println("----");
//		}
//		System.out.println(UUID.randomUUID());
//		System.out.println(string);
//	}
//	
//	@Test
//	public void testTemplate(){
//		TemplateBody templateBody=new TemplateBody();
//		templateBody.setUrl("http://cap.yc.ai/law_details?consultId=1019&consultId=1019");
//		templateBody.setTouser("1234");
//		templateBody.setTemplate_id("asdfsdfsf");
//		Map<String, TemplateData> dataMap=new HashMap<>();
//		dataMap.put("first", new TemplateData(null, "this is a test"));
//		dataMap.put("keyword1", new TemplateData(null, "test"));
//		templateBody.setData(dataMap);
//		
//		String tplStr=WeixinUtil.getTplDataMsg(templateBody);
//		System.out.println(tplStr);
//	}
	
}

