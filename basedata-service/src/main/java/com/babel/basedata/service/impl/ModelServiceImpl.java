package com.babel.basedata.service.impl;

import java.util.List;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.ISelfInject;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.mapper.ModelMapper;
import com.babel.basedata.model.ModelPO;
import com.babel.basedata.service.ILogDbService;
import com.babel.basedata.service.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("modelService")
public class ModelServiceImpl extends BaseService<ModelPO> implements IModelService,  ISelfInject{
	 private static final Log logger = LogFactory.getLog(ModelServiceImpl.class);
	 
	 public ModelServiceImpl(){
		 
	 }
	 
	 @Autowired
	 private ModelMapper mapper;
	 
	@Override
	public ModelMapper getMapper() {
		return mapper;
	}
	
	private IModelService _self;

	public void setSelf(Object proxyBean) { // 通过InjectBeanSelfProcessor注入自己（目标对象）的AOP代理对象
		this._self = (IModelService) proxyBean;
	}
	
	 @Autowired
	 private ILogDbService logDbService;
	
	
	@Override
	public ModelPO findModelById(Long id) {
		logger.info("----findModelById--id="+id);
		return mapper.selectByPrimaryKey(id);
	}
	
	/**
	 * @param logDb
	 * @param model
	 */
	public synchronized Long initModelId(ModelPO model) {
		String key=model.getPackageName()+"."+model.getClassName()+"."+model.getFuncCode();
		Long modelId=modelIdMap.get(key);
		if(modelId==null){
			RetResult<ModelPO> ret=this.findModelByCode(model.getPackageName(), model.getClassName(), model.getFuncCode());
			if(ret.isSuccess() && ret.getSize()>0){
				modelId=ret.getFirstData().getCid();
			}
			else{
				this.insertModel(model);
				modelId=model.getCid();
			}
			modelIdMap.put(key, modelId);
		}
			
		return modelId;
		
	}
	
	public void loadModelIdMap(){
		ModelPO model= new ModelPO();
		List<ModelPO> modelList=this.findModelAllByModel(model);
		for(ModelPO m:modelList){
			String key=m.getPackageName()+"."+m.getClassName()+"."+m.getFuncCode();
			modelIdMap.put(key, m.getCid());
		}
	}
	
	public List<ModelPO> findModelByIds(List<Long> idList){
		Example example = new Example(ModelPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", idList);
		return mapper.selectByExample(example);
	}
	
	public List<ModelPO> findModelAllByModel(ModelPO model){
		Example example = new Example(ModelPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
		if(!StringUtils.isEmpty(model.getFuncCode())){
			criteria.andEqualTo("funcCode", model.getFuncCode());
		}
		if(!StringUtils.isEmpty(model.getClassName())){
			criteria.andEqualTo("className", model.getClassName());
		}
		if(!StringUtils.isEmpty(model.getPackageName())){
			criteria.andLike("packageName", model.getPackageName()+"%");
		}
		if(model.getOpenType()!=null){
			criteria.andEqualTo("openType", model.getOpenType());
		}
		example.setOrderByClause("package_name, order_count, modify_date desc");
		return mapper.selectByExample(example);
	}
	
	/**
	 * 建议不要对RetResult的数据进行缓存，因为会造成空数据也被缓存
	 * @param funcCode
	 * @param className
	 * @return
	 */
	
	public RetResult<ModelPO> findModelByCode(String packageName, String className, String funcCode){
		RetResult<ModelPO> ret = this._self.findModelByCode_cache(packageName, className, funcCode);
		if(ret.isSuccess() && ret.getSize()==0){//第一次找到数据为空
			this._self.findModelbyCode_flush(packageName, className, funcCode);//清除缓存
//			ret = this._self.findModelByCode_cache(className, funcCode);//第二次查找
		}
		return ret;
	}
	
	@Cacheable(value = "basedata",key = "'findModelByCode'+#packageName+#className+'.'+#funcCode")
	public RetResult<ModelPO> findModelByCode_cache(String packageName, String className, String funcCode){
		RetResult<ModelPO> ret = new RetResult<>();
		Example example = new Example(ModelPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("packageName", packageName);
		criteria.andEqualTo("className", className);
		criteria.andEqualTo("funcCode", funcCode);
		List<ModelPO> list=this.selectByExample(example);
//		System.out.println("--------findModelByCode-----"+className+" "+funcCode);
		ret.setDataList(list);
		return ret;
	}
	
	@CacheEvict(value = "basedata",key = "'findModelByCode'+#className+'.'+#funcCode")
	public void findModelbyCode_flush(String packageName, String className, String funcCode){
		logger.info("--------findModelbyCode_flush-----"+packageName+" "+className+" "+funcCode);
	}
	
	public RetResult<ModelPO> getModelByName(String name){
		RetResult<ModelPO> ret = new RetResult<>();
		Example example = new Example(ModelPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andLike("funcCode", "%"+name+"%");
		criteria.andEqualTo("ifDel", 0);
		List<ModelPO> list=this.selectByExample(example);
//		System.out.println("--------findModelByCode-----"+className+" "+funcCode);
		ret.setDataList(list);
		return ret;
	}
	

	public PageVO<ModelPO> findPageByModel(ModelPO model, PageVO<ModelPO> page) {
		logger.info("----findModelById--id="+model.getCid());
		Example example = new Example(ModelPO.class);
		Example.Criteria criteria = example.createCriteria();
		if (StringUtil.isNotEmpty(model.getClassName())) {
			criteria.andLike("className", "%" + model.getClassName() + "%");
		}
		if (StringUtil.isNotEmpty(model.getFuncCode())) {
			criteria.andLike("funcCode", "%" + model.getFuncCode() + "%");
		}
		if (StringUtil.isNotEmpty(model.getPackageName())) {
			criteria.andLike("packageName", model.getPackageName() + "%");
		}
		if (model.getOpenType() != null) {
			criteria.andEqualTo("openType", model.getOpenType());
		}
		if (model.getIntfType() != null) {
			criteria.andEqualTo("intfType", model.getIntfType());
		}
		if (model.getCid() != null) {
			criteria.andEqualTo("cid", model.getCid());
		}
		criteria.andEqualTo("ifDel", 0);
		
		//order default
        if(StringUtils.isEmpty(page.getSort())){
        	example.setOrderByClause("package_name, class_name, order_count desc, create_date desc");
        }
        else{
        	example.setOrderByClause(page.getOrderClause());
        }
		// 分页查询
		PageHelper.startPage(page.getCurrentPage(), page.getPageSize());
		List<ModelPO> list = selectByExample(example);
		PageInfo<ModelPO> pageInfo = new PageInfo<ModelPO>(list);
		PageVO<ModelPO> pageRet = new PageVO<ModelPO>(pageInfo);
		return pageRet;
	}
	

	public RetResult<ModelPO> create(ModelPO record){
		logger.info("----create--");
		long time=System.currentTimeMillis();
		RetResult<ModelPO> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getCode())||StringUtils.isEmpty(record.getName())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code="+record.getCode()+" or name="+record.getName()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insert(record);
		ret.setData(record);
		return ret;
	}
	
//	@CacheEvict(value = "basedata",key = "'findModelByCode'+#className+'.'+#funcCode")
	public RetResult<ModelPO> insertModel(ModelPO record){
		logger.info("----insertModel--");
		long time=System.currentTimeMillis();
		RetResult<ModelPO> ret = new RetResult<>();
		if(StringUtils.isEmpty(record.getClassName())||StringUtils.isEmpty(record.getFuncCode())){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "className="+record.getClassName()+" or funcCode="+record.getFuncCode()+" is empty", null);
			logger.warn("create="+ret.getMsgBody());
			return ret;
		}
		record.initCreate();
		this.getMapper().insertModel(record);
		ret.setData(record);
		return ret;
	}
	
	public RetResult<Long> update(ModelPO record){
		logger.info("----update--");
		long time=System.currentTimeMillis();
		RetResult<Long> ret = new RetResult<>();
		if(record.getCid()==null||record.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+record.getCid()+" is empty", null);
			logger.warn("update="+ret.getMsgBody());
			return ret;
		}
		record.initUpdate();
		record.setIfDel(0);
		this.mapper.updateByPrimaryKey(record);
		ret.setData(record.getCid());
		return ret;
	}
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid){
		logger.info("----update--");
		long time=System.currentTimeMillis();
		RetResult<Integer> ret = new RetResult<>();
		if(cid==null||cid==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid="+cid+" is empty", null);
			logger.warn("deleteVirtual="+ret.getMsgBody());
			return ret;
		}
		ModelPO model = new ModelPO();
		model.setCid(cid);
		model.setModifyUser(operUserId);
		int v=0;
		try {
			v = this.deleteVirtual(model);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid="+cid+",error="+e.getMessage(), e);
		}
		ret.setData(v);
		return ret;
	}

}
