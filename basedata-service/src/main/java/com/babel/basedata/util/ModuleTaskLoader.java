package com.babel.basedata.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babel.basedata.model.ModulePO;
import com.babel.basedata.service.IModuleService;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.util.CommUtil;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.loader.IContextTaskLoader;

@Service("moduleTaskLoader")
public class ModuleTaskLoader implements IContextTaskLoader {
	private static Logger log = Logger.getLogger(ModuleTaskLoader.class);
	@Autowired
	private IModuleService moduleService;

	@Override
	public RetResult<String> execute(ServletContextEvent event) {
		RetResult<String> ret =new RetResult<String>();
		try {
			if(event!=null){
				Thread.sleep(1000l);//延时1秒
			}
			this.initLoadModule();
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "initLoadModule error:"+e.getMessage(), e);
		}
	
		
		return ret;
	}
	
	/**
	 * 模块菜单启动载入缓存处理
	 */
	public void initLoadModule(){
		log.info("----initLoadModule--");
		long time=System.currentTimeMillis();
		List<ModulePO> mList=new ArrayList();
		this.loadModuleByParent(0l, mList);

		Set<String> moduleUrlSet=new HashSet<>();
		for(ModulePO m:mList){
			if(!StringUtils.isEmpty(m.getUrl()))
				moduleUrlSet.add(m.getUrl());
		}
		AppContext.putRoleModule(AppContext.ROLE_ALL_MODULE_URL, moduleUrlSet);
		log.info("----initLoadModule--moduleList="+mList.size()+" moduleUrl="+moduleUrlSet.size()+" time="+(System.currentTimeMillis()-time));
	}
	
	private Long getLongValue(Object obj){
		if(obj==null){
			return 0l;
		}
		else if(obj instanceof BigDecimal){
			return ((BigDecimal)obj).longValue();
		}
		else if(obj instanceof Long){
			return (Long)obj;
		}
		else{
			return Long.valueOf(""+obj);
		}
	}
	/**
	 * 递归取出所有的菜单
	 * @param parentId
	 * @param mList
	 */
	private void loadModuleByParent(final Long parentId, List<ModulePO> mList){
		List<Long> parentIdList=CommUtil.newList(parentId);
		List<ModulePO> moduleList=this.moduleService.findModuleByParentIds(parentIdList);
		
		List<Long> idList=new ArrayList();
		for(ModulePO m:moduleList){
    		idList.add(m.getCid());
    	}
		List<Map<String, Object>> mCountMapList=this.moduleService.getModuleCountByParentId(idList);
		Long pId=null;
		Long pCount=null;
		boolean hasChild=false;
		for(ModulePO module:moduleList){
			if(module.getParentId().longValue()!=0 && (module.getStatus()==null||module.getStatus().intValue()==0)){//状态无效的不载入
				continue;
			}
			hasChild=false;
    		for(Map<String, Object> mCountMap:mCountMapList){
    			pId=getLongValue(mCountMap.get("parentId"));
				pCount=getLongValue(mCountMap.get("cout"));
    			if(pId.longValue()==module.getCid().longValue() && pCount>0){
    				hasChild=true;
    				break;
    			}
    		}
    		if(hasChild){
    			this.loadModuleByParent(module.getCid(), mList);
    		}
			mList.add(module);
		}
	
	}
}
