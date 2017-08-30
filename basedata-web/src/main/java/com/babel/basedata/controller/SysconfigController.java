package com.babel.basedata.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.util.CommUtil;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.SysconfigPO;
import com.babel.basedata.service.ISysconfigService;
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
@RequestMapping("/basedata/sysconfig")
public class SysconfigController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(SysconfigController.class);

    @Autowired
    private ISysconfigService sysconfigService;
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(SysconfigPO sysconfig){
    	ModelAndView result = new ModelAndView("basedata/ey_sysconfig");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    @RequestMapping(value = {"tree", "tree.html"})
    public ModelAndView tree(SysconfigPO sysconfig){
    	ModelAndView result = new ModelAndView("basedata/ey_sysconfigTree");
    	return result;
    }
    
    @RequestMapping(value = {"findSysconfigByParentId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<HashMap<String, Object>> findSysconfigByParentId(Integer confType, Long parentId){
    	logger.info("-------findSysconfigByParentId--sysconfigId="+parentId);
    	if(parentId==null){
    		parentId=0l;
    	}
    	List<Long> idList=CommUtil.newList(parentId);
    	List<SysconfigPO> list=this.sysconfigService.findSysconfigByParentIds(confType, idList);
    	idList.clear();
    	for(SysconfigPO m:list){
    		idList.add(m.getCid());
    	}
//    	List<SysconfigPO> mList=this.sysconfigService.findSysconfigByParentIds(confType, idList);//用于检查是否有子节点
    	List<Map<String, Object>> mCountMapList=this.sysconfigService.findSysconfigByParentIdsCount(confType, idList);//用于检查是否有子节点
    	List<HashMap<String, Object>> mapList=new ArrayList();
    	HashMap<String, Object> map=null;
    	boolean hasChild=false;
    	String status=null;
    	Long pId=null;
		Long pCount=null;
    	for(SysconfigPO sysconfig:list){
    		map=new HashMap<>();
    		map.put("id", sysconfig.getCid());
    		map.put("parentId", parentId);
    		map.put("text", sysconfig.getName());
    		map.put("code", sysconfig.getCode());
    		map.put("confType", sysconfig.getConfType());
    		//检查是否有子节点
    		hasChild=false;
//    		for(SysconfigPO m:mList){
//    			if(m.getPid().longValue()==sysconfig.getCid().longValue()){
//    				hasChild=true;
//    				break;
//    			}
//    		}
    		for(Map<String, Object> mCountMap:mCountMapList){
    			pId=getLongValue(mCountMap.get("parentId"));
				pCount=getLongValue(mCountMap.get("cout"));
    			if(pId.longValue()==sysconfig.getCid().longValue() && pCount>0){
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
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<SysconfigPO> findByPage(SysconfigPO sysconfig) {
    	logger.info("-------findByPage-------");
    	PageVO<SysconfigPO> pageVO = new PageVO<SysconfigPO>(this.getRequest());
    	PageVO<SysconfigPO> p=sysconfigService.findPageBySysconfig(sysconfig, pageVO);
//    	try {
//			ObjectToMapUtil.setFieldsEmpty(p.getDatas(), "cid,modifyDate,code,name,orderCount", false);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	for(SysconfigPO sys:p.getDatas()){
//    		sys.setCid(null);
//    		sys.setCreateDate(null);
//    	}
    	this.initDisp(p.getRows());
    	return p;
    }
    
 
    @RequestMapping(value = "getSysconfigByName")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<SysconfigPO> getSysconfigByName(Integer confType, String name) {
    	RetResult<SysconfigPO> ret = new RetResult<SysconfigPO>();
    	if(StringUtils.isEmpty(name)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "name is empty", null);
    		return ret;
    	}
    	
    	return this.sysconfigService.getSysconfigByName(confType, name);
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<SysconfigPO> view(SysconfigPO sysconfig) {
    	RetResult<SysconfigPO> ret = new RetResult<SysconfigPO>();
    	if(sysconfig.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
    	
        sysconfig = sysconfigService.selectByKey(sysconfig.getCid());
        
        ret.setData(sysconfig);
        return ret;
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
    	return this.sysconfigService.updateRel(this.getCurrentUserId(), cid, oldParentId, newParentId);
    }
    
    

    /**
     * save or update
     * @param sysconfig
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(author="cjh", title="sysconfig.save", calls="update,create", descs="cid=#{0.cid},code=#{0.code}")
    public RetResult<Long> save(SysconfigPO sysconfig) {
    	RetResult<Long> ret=new RetResult<Long>();
    	if(StringUtils.isEmpty(sysconfig.getCode())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code is empty", null);
    		return ret;
    	}
    	if(StringUtils.isEmpty(sysconfig.getName())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "name is empty", null);
    		return ret;
    	}
    	logger.info("------sysconfig--cid="+sysconfig.getCid()+" code="+sysconfig.getCode()+" name="+sysconfig.getName()+" status="+sysconfig.getStatus());
    	if(sysconfig.getPid()==null){
    		sysconfig.setPid(0l);
    	}
    	ret=new RetResult<Long>();
        if (sysconfig.getCid() != null) {
        	this.initUpdate(sysconfig);
            ret=sysconfigService.update(sysconfig);
        } else {
        	this.initCreate(sysconfig);
            ret=sysconfigService.create(sysconfig);
        }
        ret.setData(sysconfig.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(author="cjh", title="sysconfig.delete", calls="deleteVirtual", descs="cid=#{0}")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
    	if(id==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id is empty", null);
    		return ret;
    	}
    	Long operUserId=AppContext.getCurrentUserId();
    	ret =this.sysconfigService.deleteVirtual(operUserId, id);
        return ret;
    }
    
}
