package com.babel.basedata.mybatis;

import org.junit.Test;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.babel.basedata.model.UserPO;
import com.babel.basedata.service.ILoginService;
import com.babel.common.core.data.RetResult;

import junit.framework.TestCase;

public class TestLoginService  extends TestCase{
	private static  ILoginService loginService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(LookupItemPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		try {
			act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
			loginService = (ILoginService)act.getBean("loginService");
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testLogin(){
		try {
			RetResult<UserPO> ret=this.loginService.login(null, "admin", "test@1234");
			System.out.println(ret.getFlag());
			UserPO user=ret.getFirstData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testChangePassword(){
		RetResult<Integer> ret=this.loginService.changePassword(null, 1l, "test@1234", "test@1234");
		System.out.println(ret.getFlag());
		
	}
	
	@Test
	public void testChangePasswordByManage(){
		RetResult<Integer> ret=this.loginService.changePasswordByManage(null, 1l, "test@1234");
		System.out.println(ret.getFlag());
		
	}
	

}
