package com.babel.basedata.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.data.RetResult.msg_codes;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.util.CommUtil;
import com.babel.common.core.util.SpringContextUtil;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.common.web.loader.IContextTaskLoader;
import com.babel.basedata.entity.ModuleTreeVO;
import com.babel.basedata.model.ModulePO;
import com.babel.basedata.service.IModuleService;
import com.babel.basedata.service.IRoleModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * @author liuzh_3nofxnp
 * @since 2015-09-19 17:15
 */
@Controller
@RequestMapping("/basedata/module")
public class ModuleController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(ModuleController.class);

    @Autowired
    private IModuleService moduleService;
    
    @Autowired
    private IRoleModuleService roleModuleService;
    
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(ModulePO module){
    	ModelAndView result = new ModelAndView("basedata/ey_module");
    	return result;
    }
    
    @RequestMapping(value = {"tree", "tree.html"})
    public ModelAndView tree(ModulePO module){
    	ModelAndView result = new ModelAndView("basedata/ey_moduleTree");
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<ModulePO> findByPage(ModulePO module) {
    	logger.info("-------findByPage-------");
    	PageVO<ModulePO> pageVO = new PageVO<ModulePO>(this.getRequest());
    	PageVO<ModulePO> p=moduleService.findPageByModule(module, pageVO);
    	this.initDisp(p.getRows());
    	return p;
    }
    
    @RequestMapping(value = {"findModuleByParentId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<HashMap<String, Object>> findModuleByParentId(Long moduleId){
    	
    	if(moduleId==null){
    		moduleId=0l;
    	}
    	List<Long> idList=CommUtil.newList(moduleId);
    	List<ModulePO> list=this.moduleService.findModuleByParentIds(idList);
    	idList.clear();
    	for(ModulePO m:list){
    		idList.add(m.getCid());
    	}
    	List<Map<String, Object>> mCountMapList=this.moduleService.getModuleCountByParentId(idList);//用于检查是否有子节点
    	logger.info("-------findModuleByParentId--moduleId="+moduleId+" idList="+idList+" mapCountMapList="+mCountMapList);
    	List<HashMap<String, Object>> mapList=new ArrayList();
    	HashMap<String, Object> map=null;
    	boolean hasChild=false;
    	Long pId=null;
		Long pCount=null;
    	for(ModulePO module:list){
    		map=new HashMap<>();
    		map.put("id", module.getCid());
    		map.put("parentId", moduleId);
    		map.put("text", module.getName());
    		map.put("code", module.getCode());
    		map.put("color", module.getColor());
    		//检查是否有子节点
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
    			map.put("state", "closed");
    		}
    		mapList.add(map);
    	}
    	return mapList;
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
    
    @RequestMapping(value = "refreshModule", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<String> refreshModule(Model model) {
    	RetResult<String> ret = new RetResult<String>();
    	String beanName="moduleTaskLoader";
    	if(SpringContextUtil.containsBean(beanName)){
    		IContextTaskLoader loader=(IContextTaskLoader)SpringContextUtil.getBean(beanName);
    		loader.execute(null);
    		
    	}
    	else{
    		ret.initError(msg_codes.ERR_DATA_EXISTED, "beanName:"+beanName+" not found", null);
    	}
        return ret;
    }
    

    @RequestMapping(value = {"findRoleModuleByParentId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<HashMap<String, Object>> findRoleModuleByParentId(Long roleId, Long moduleId){
    	logger.info("-------findRoleModuleByParentId--roleId="+roleId+" moduleId="+moduleId);
    	if(moduleId==null){
    		moduleId=0l;
    	}
    	List<HashMap<String, Object>> mapList=new ArrayList();
    	if(roleId==null){
    		return mapList;
    	}
    	List<Long> idList=CommUtil.newList(moduleId);
    	List<ModuleTreeVO> list=this.moduleService.findRoleModuleTreeByParentIds(roleId, idList);
    	idList.clear();
    	for(ModuleTreeVO m:list){
    		idList.add(m.getCid());
    	}
    	List<ModuleTreeVO> mList=this.moduleService.findRoleModuleTreeByParentIds(roleId, idList);//用于检查是否有子节点
    	logger.info("-----list="+list.size()+" mList="+mList.size());
    	HashMap<String, Object> map=null;
    	boolean hasChild=false;
    	String status=null;
    	for(ModuleTreeVO module:list){
    		map=new HashMap<>();
    		map.put("id", module.getCid());
    		map.put("parentId", moduleId);
    		map.put("text", module.getName());
    		map.put("code", module.getCode());
    		map.put("color", module.getColor());
    		map.put("checked", module.isChecked());
    		//检查是否有子节点
    		hasChild=false;
    		for(ModuleTreeVO m:mList){
    			if(m.getParentId().longValue()==module.getCid().longValue()){
    				hasChild=true;
    				break;
    			}
    		}
    		if(hasChild){
    			map.put("state", "closed");
    		}
    		mapList.add(map);
    	}
    	return mapList;
    }
    
    @RequestMapping(value = "saveRoleModules")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<String> saveRoleModules(Long roleId, String moduleIds) {
    	RetResult<String> ret = new RetResult<String>();
    	if(StringUtils.isEmpty(moduleIds)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "moduleIds is empty", null);
    		return ret;
    	}
    	
    	if(roleId==null||roleId.longValue()==0){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "roleId="+roleId+" invalid", null);
    		return ret;
    	}
    	
    	
    	
//    	return this.moduleService.getModuleByName(name);
    	return this.roleModuleService.saveRoleModules(roleId, moduleIds, this.getCurrentUserId());
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ModulePO> view(ModulePO module) {
    	RetResult<ModulePO> ret = new RetResult<ModulePO>();
    	if(module.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
    	
        module = moduleService.selectByKey(module.getCid());
        
        ret.setData(module);
        return ret;
    }
    

    @RequestMapping(value = "getModuleByName")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ModulePO> getModuleByName(String name) {
    	RetResult<ModulePO> ret = new RetResult<ModulePO>();
    	if(StringUtils.isEmpty(name)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "name is empty", null);
    		return ret;
    	}
    	
    	return this.moduleService.getModuleByName(name);
    }
    
    /**
     * 修改父子关系
     * @param cid
     * @param parentId
     * @return
     */
    @RequestMapping(value = "updateRel")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="updateRel", descs="")
    public RetResult<Long> updateRel(Long cid,  Long oldParentId, Long newParentId){
    	RetResult<Long> ret=new RetResult<Long>();
    	if(cid==null||cid.longValue()==0){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
    	if(oldParentId==null||oldParentId.longValue()==0){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "oldParentId is empty", null);
    		return ret;
    	}
    	if(newParentId==null||newParentId.longValue()==0){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "newParentId is empty", null);
    		return ret;
    	}
    	return this.moduleService.updateRel(this.getCurrentUserId(), cid, oldParentId, newParentId);
    }
    

    /**
     * save or update
     * @param module
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="save module", descs="id:")
    public RetResult<Long> save(ModulePO module) {
    	RetResult<Long> ret=new RetResult<Long>();
    	if(StringUtils.isEmpty(module.getCode())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code is empty", null);
    		return ret;
    	}
    	if(StringUtils.isEmpty(module.getName())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "name is empty", null);
    		return ret;
    	}
    	logger.info("------module--cid="+module.getCid()+" code="+module.getCode()+" name="+module.getName());
    	if(module.getParentId()==null){
    		module.setParentId(0l);
    	}
    	ret=new RetResult<Long>();
        if (module.getCid() != null) {
        	this.initUpdate(module);
            ret=moduleService.update(module);
        } else {
        	this.initCreate(module);
            ret=moduleService.create(module);
        }
        ret.setData(module.getCid());
        return ret;
    }
    
    

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="delete module", descs="id:")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
    	if(id==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id is empty", null);
    		return ret;
    	}
    	Long operUserId=AppContext.getCurrentUserId();
    	ret =this.moduleService.deleteVirtual(operUserId, id);
        return ret;
    }
    
}
