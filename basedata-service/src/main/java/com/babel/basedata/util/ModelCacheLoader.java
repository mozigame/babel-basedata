package com.babel.basedata.util;

import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.babel.basedata.service.IModelService;
import com.babel.common.core.data.RetResult;
import com.babel.common.web.loader.IContextTaskLoader;

/**
 * 用于优化LogManagerService,LogSqlManagerService的异步日志性能，并减少并发保存时同时保存数据问题
 * @author 金和
 *
 */
@Service("modelCacheLoader")
public class ModelCacheLoader  implements IContextTaskLoader {
	private static Logger log = Logger.getLogger(ModelCacheLoader.class);
	@Autowired
	private IModelService modelService;


	@Override
	public RetResult<String> execute(ServletContextEvent event) {
		RetResult<String> ret = new RetResult<String>();
		this.initModelCacheMap();
		return ret;
	}
	
	

	private void initModelCacheMap(){
		this.log.info("-----load initModelCacheMap--start--");
		this.modelService.loadModelIdMap();
		this.log.info("-----load initModelCacheMap--size="+modelService.modelIdMap.size());
	}
}
