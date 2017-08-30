package com.babel.basedata.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babel.basedata.model.WhiteListPO;
import com.babel.basedata.model.WhiteTypePO;
import com.babel.basedata.service.IWhiteListService;
import com.babel.basedata.service.IWhiteTypeService;
import com.babel.common.core.data.RetResult;
import com.babel.common.web.loader.IContextTaskLoader;

/**
 * 白名单加载
 * @author 金和
 *
 */
@Service("whiteListLoader")
public class WhiteListLoader  implements IContextTaskLoader {
	private static Logger log = Logger.getLogger(WhiteListLoader.class);
	@Autowired
	private IWhiteTypeService whiteTypeService;
	@Autowired
	private IWhiteListService whiteListService;
	

	@Override
	public RetResult<String> execute(ServletContextEvent event) {
		RetResult<String> ret = new RetResult<String>();
		Integer dataType=(Integer)event.getServletContext().getAttribute("dataType");
		Integer whiteType=(Integer)event.getServletContext().getAttribute("whiteType");
		if(dataType!=null && whiteType!=null){
			this.initWhiteType(dataType, whiteType);
		}
		else{
			this.initWhiteTypeAll();
		}
		
		return ret;
	}
	
	

	private void initWhiteTypeAll(){
		this.log.info("-----load initWhiteTypeAll--start--");
		List<WhiteTypePO> ruleList=this.whiteTypeService.findWhiteTypeAll();
		
		List<Long> wtIdList=new ArrayList<>();
		for(WhiteTypePO wt:ruleList){
			wtIdList.add(wt.getCid());
		}
		List<WhiteListPO> list=this.whiteListService.findWhiteListByWhiteTypeIds(wtIdList);
		this.initCache(ruleList, list);
		
	}
	
	private void initCache(List<WhiteTypePO> ruleList, List<WhiteListPO> list){
		Map<String, List<WhiteListPO>> whiteListMap=new HashMap<>();
		String key=null;
		String ruleCode="";
		List<WhiteListPO> existList=null;
		for(WhiteTypePO rule:ruleList){
			key=rule.getDataType()+"_"+rule.getType();
//			WhiteListUtils.whiteTypeMap.put(rule.getCid(), rule);
			WhiteListUtils.putWhiteType(rule.getCid(), rule);
			existList=whiteListMap.get(key);
			if(existList==null){
				existList=new ArrayList<>();
				whiteListMap.put(key, existList);
			}
			
			for(WhiteListPO item:list){
				if(rule.getCid().longValue()==item.getWhiteTypeId().longValue()){
					existList.add(item);
				}
			}
			ruleCode+=rule.getCode()+",";
		}
		this.log.info("-----load initCache--start--list="+list.size()+" ruleCode="+ruleCode);
		Set<Map.Entry<String, List<WhiteListPO>>> entrys=whiteListMap.entrySet();
		for(Map.Entry<String, List<WhiteListPO>> entry:entrys){
			WhiteListUtils.putWhiteList(entry.getKey(), entry.getValue());
		}
//		WhiteListUtils.whiteListMap.putAll(whiteListMap);
		
	}
	
	private void initWhiteType(Integer dataType, Integer whiteType){
		this.log.info("-----load initWhiteType--start--dataType="+dataType+" whiteType="+whiteType);
		List<WhiteTypePO> whiteTypeList=this.whiteTypeService.findWhiteTypeByType(dataType, whiteType);
		if(whiteTypeList.size()==0){
			WhiteListUtils.removeWhiteList(dataType+"_"+whiteType);
		}
		else{
			List<Long> wtIdList=new ArrayList<>();
			for(WhiteTypePO wt:whiteTypeList){
				wtIdList.add(wt.getCid());
			}
			List<WhiteListPO> list=this.whiteListService.findWhiteListByWhiteTypeIds(wtIdList);
			this.initCache(whiteTypeList, list);
		}
		
	}
}
