package com.babel.basedata.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.ModelPO;
import com.babel.basedata.service.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/basedata/model")
public class ModelController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(ModelController.class);

    @Autowired
    private IModelService modelService;
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(ModelPO model){
    	ModelAndView result = new ModelAndView("basedata/ey_model");
    	return result;
    }
    
    @RequestMapping(value = {"tree", "tree.html"})
    public ModelAndView tree(ModelPO model){
    	ModelAndView result = new ModelAndView("basedata/ey_modelTree");
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<ModelPO> findByPage(ModelPO model) {
    	logger.info("-------findByPage-------");
    	PageVO<ModelPO> pageVO = new PageVO<ModelPO>(this.getRequest());
    	PageVO<ModelPO> p=modelService.findPageByModel(model, pageVO);
    	this.initDisp(p.getRows());
    	return p;
    }
    
    /**
     * 检查类名是否属于包
     * 
     * @param pkg,exp:com.babel.test
     * @param clsName:com.babel.test.TestClass
     * @return
     */
    private boolean isPackageClass(final String pkg, final String clsName){
    	if(clsName.startsWith(pkg+".")){
    		String cName=clsName.substring(pkg.length()+1);
    		if(cName.indexOf(".")<0){
    			return true;
    		}
    	}
    	return false;
    }
    
    private String getIntfTypeColor(final Integer intfType){
    	String color="black";
    	if(intfType!=null){
    		if(intfType==1){
    			color="blue";
    		}
    		else if(intfType==2){
    			color="orange";
    		}
    		else if(intfType==3){
    			color="green";
    		}
    	}
    	return color;
    }
    
    private static List<ModelPO> modelAllList=new ArrayList<>();
    private static Date loadModelDate=null;
    
    private static synchronized List<ModelPO> findModelAll(IModelService modelService, ModelPO model){
    	loadModelDate=new Date();
		List<ModelPO> modelList= modelService.findModelAllByModel(model);
		return modelList;
    }
    
    private List<HashMap<String, Object>> getModelByTree(ModelPO model, Long parentId){
    	logger.info("-------findModelByTree--parentId="+parentId);
    	List<ModelPO> modelList=modelAllList;
    	if(loadModelDate==null|| new Date().getTime()-loadModelDate.getTime()>3000){
    		modelList=findModelAll(this.modelService, model);
    		modelAllList=modelList;
    	}
    	List<HashMap<String, Object>> treeList=new ArrayList<>();
    	Set<String> pkgSet=new HashSet<>();
    	Set<String> classSet=new HashSet<>();
    	HashMap<String, Object> map=null;
    	for(ModelPO m:modelList){
    		pkgSet.add(m.getPackageName());
    		classSet.add(m.getPackageName()+"."+m.getClassName());
    	}
    	
    	String className=null;
    	Long id=0l;
    	Long cId=0l;
    	Integer count=0;
    	for(String pkg:pkgSet){
    		id++;
    		map=new HashMap<>();
    		map.put("id", id);
    		map.put("text", pkg);
    		map.put("state", "closed");
    		if(parentId.longValue()==0){
    			treeList.add(map);
    		}
    		count=0;
    		for(String clsName:classSet){
    			if(isPackageClass(pkg, clsName)){
    				className=clsName.substring(pkg.length()+1);
    				map=new HashMap<>();
    				count++;
    				cId=id*100+count;
    				if(parentId.longValue()==id.longValue()){
	    	    		map.put("id", cId);
	    	    		map.put("parentId", id);
	    	    		map.put("text", className);
	    	    		map.put("state", "closed");
	    	    		treeList.add(map);
    				}
    				if(parentId.longValue()==cId.longValue()){
	    	    		//处理类的接口方法
	    	    		for(ModelPO m:modelList){
	    	    			if(m.getClassName().equals(className) && m.getPackageName().equals(pkg)){
	    	    				map=new HashMap<>();
	    	    	    		map.put("id", m.getCid());
	    	    	    		map.put("parentId", cId);
	    	    	    		map.put("text", m.getFuncCode());
	    	    	    		map.put("code", m.getFuncCode());
	    	    	    		map.put("color", this.getIntfTypeColor(m.getIntfType()));
	    	    	    		treeList.add(map);
    	    				}
    	    			}
    	    		}
    			}
    		}
    	}
    	
    	
    	return treeList;
    }
    
    @RequestMapping(value = {"findModelByTree"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<HashMap<String, Object>> findModelByTree(ModelPO model, Long modelId){
    	logger.info("-------findModelByTree--modelId="+modelId);
    	if(modelId==null){
    		modelId=0l;
    	}
    	List<HashMap<String, Object>> treeList=this.getModelByTree(model, modelId);
    	return treeList;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ModelPO> view(ModelPO model) {
    	RetResult<ModelPO> ret = new RetResult<ModelPO>();
    	if(model.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
    	
        model = modelService.selectByKey(model.getCid());
        
        ret.setData(model);
        return ret;
    }
    
    @RequestMapping(value = "getModelByName")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ModelPO> getModelByName(String name) {
    	RetResult<ModelPO> ret = new RetResult<ModelPO>();
    	if(StringUtils.isEmpty(name)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "name is empty", null);
    		return ret;
    	}
    	
    	return this.modelService.getModelByName(name);
    }
    
//    /**
//     * 修改父子关系
//     * @param cid
//     * @param parentId
//     * @return
//     */
//    @RequestMapping(value = "updateRel")
//    @ResponseStatus(value = HttpStatus.OK)
//    @ResponseBody
//    @LogAudit(title="updateRel", descs="")
//    public RetResult<Long> updateRel(Long cid,  Long oldParentId, Long newParentId){
//    	RetResult<Long> ret=new RetResult<Long>();
//    	if(cid==null||cid.longValue()==0){
//    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
//    		return ret;
//    	}
//    	if(oldParentId==null||oldParentId.longValue()==0){
//    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "oldParentId is empty", null);
//    		return ret;
//    	}
//    	if(newParentId==null||newParentId.longValue()==0){
//    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "newParentId is empty", null);
//    		return ret;
//    	}
//    	return this.modelService.updateRel(this.getCurrentUserId(), cid, oldParentId, newParentId);
//    }
    

    /**
     * save or update
     * @param model
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="save model", descs="id:")
    public RetResult<Long> save(ModelPO model) {
    	RetResult<Long> ret=new RetResult<Long>();
    	if(StringUtils.isEmpty(model.getFuncCode())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "funcCode is empty", null);
    		return ret;
    	}
    	if(StringUtils.isEmpty(model.getClassName())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "className is empty", null);
    		return ret;
    	}
    	logger.info("------model--cid="+model.getCid()+" funcCode="+model.getFuncCode()+" className="+model.getClassName());
//    	if(model.getParentId()==null){
//    		model.setParentId(0l);
//    	}
    	ret=new RetResult<Long>();
        if (model.getCid() != null) {
        	this.initUpdate(model);
            ret=modelService.update(model);
        } 
//        else {
//        	this.initUpdate(model);
//            ret=modelService.create(model);
//        }
        ret.setData(model.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="delete model", descs="id:")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
    	if(id==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id is empty", null);
    		return ret;
    	}
//    	Long operUserId=AppContext.getCurrentUserId();
//    	ret =this.modelService.deleteVirtual(operUserId, id);
        return ret;
    }
    
}
