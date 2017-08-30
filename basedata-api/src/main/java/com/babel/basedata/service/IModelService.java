package com.babel.basedata.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.babel.basedata.model.ModelPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IModelService  extends IBaseService<ModelPO> {
	public static Map<String, Long> modelIdMap=new HashMap<>();
	public Long initModelId(ModelPO model);
	public void loadModelIdMap();
	public ModelPO findModelById(Long id);
	
	public List<ModelPO> findModelByIds(List<Long> idList);
	public List<ModelPO> findModelAllByModel(ModelPO model);
	
	public RetResult<ModelPO> findModelByCode(String packageName, String className, String funcCode);
	
	public RetResult<ModelPO> findModelByCode_cache(String packageName, String className, String funcCode);
	
	/**
	 * 用于清除缓存
	 * @param className
	 * @param funcCode
	 */
	public void findModelbyCode_flush(String packageName, String className, String funcCode);
	
	public RetResult<ModelPO> getModelByName(String name);

	public PageVO<ModelPO> findPageByModel(ModelPO search, PageVO<ModelPO> page);
	
	public RetResult<Long> update(ModelPO record);
	
//	public RetResult<ModelPO> create(ModelPO record);
	
	public RetResult<ModelPO> insertModel(ModelPO record);
	
	
}
