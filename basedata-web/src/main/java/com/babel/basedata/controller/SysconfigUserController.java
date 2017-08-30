package com.babel.basedata.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
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
import com.babel.basedata.model.SysconfigUserPO;
import com.babel.basedata.service.ISysconfigService;
import com.babel.basedata.service.ISysconfigUserService;
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
@RequestMapping("/basedata/sysconfigUser")
public class SysconfigUserController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(SysconfigUserController.class);

    @Autowired
    private ISysconfigUserService sysconfigUserService;
    
    @Autowired
    private ISysconfigService sysconfigService;
    

    
    @RequestMapping(value = {"index",  ""})
    public ModelAndView index(SysconfigUserPO sysconfigUser){
    	ModelAndView result = new ModelAndView("basedata/ey_sysconfigUser");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    @RequestMapping(value = {"tree", "tree.html"})
    public ModelAndView tree(SysconfigPO sysconfig){
    	ModelAndView result = new ModelAndView("basedata/ey_sysconfigUserTree");
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
    		if(m.getStatus().intValue()==0){//不显示状态无效的数据
    			continue;
    		}
    		idList.add(m.getCid());
    	}
    	List<SysconfigUserPO> sysUserList=this.sysconfigUserService.findSysconfigUserListByUsers(CommUtil.newList(this.getCurrentUserId()), idList);
    	List<Map<String, Object>> mCountMapList=this.sysconfigService.findSysconfigByParentIdsCount(confType, idList);//用于检查是否有子节点
    	
    	List<HashMap<String, Object>> mapList=new ArrayList();
    	HashMap<String, Object> map=null;
    	boolean hasChild=false;
    	Long pId=null;
		Long pCount=null;
    	for(SysconfigPO sysconfig:list){
    		if(sysconfig.getStatus().intValue()==0){//不显示状态无效的数据
    			continue;
    		}
    		map=new HashMap<>();
    		map.put("id", sysconfig.getCid());
    		map.put("parentId", parentId);
    		map.put("text", sysconfig.getName());
    		map.put("code", sysconfig.getCode());
    		map.put("value", sysconfig.getValue());
    		map.put("value1", sysconfig.getValue1());
    		map.put("value2", sysconfig.getValue2());
    		map.put("valueJson", sysconfig.getValueJson());
    		for(SysconfigUserPO sysUser:sysUserList){
    			if(sysconfig.getCid().longValue()==sysUser.getSysconfigId().longValue()){
    				map.put("value", sysUser.getValue());
    				map.put("value1", sysUser.getValue1());
    				map.put("value2", sysUser.getValue2());
    				map.put("valueJson", sysUser.getValueJson());
    				break;
    			}
    		}
    		map.put("confType", sysconfig.getConfType());
    		//检查是否有子节点
    		hasChild=false;
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
    public PageVO<SysconfigUserPO> findByPage(SysconfigUserPO sysconfigUser) {
    	logger.info("-------findByPage-------");
    	PageVO<SysconfigUserPO> pageVO = new PageVO<SysconfigUserPO>(this.getRequest());
    	PageVO<SysconfigUserPO> p= sysconfigUserService.findPageBySysconfigUser(sysconfigUser, pageVO);
    	this.initDisp(p.getRows());
    	return p;
    }
    
    @RequestMapping(value = {"userlist"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<SysconfigPO> findByPageUser(SysconfigPO sysconfig) {
    	logger.info("-------findByPageUser-------");
    	PageVO<SysconfigPO> pageVO = new PageVO<SysconfigPO>(this.getRequest());
    	sysconfig.setConfType(4);//user config
    	pageVO=this.sysconfigService.findPageBySysconfig(sysconfig, pageVO);
    	
    	Collection<SysconfigPO> sysList=pageVO.getRows();
    	List<Long> sysIdList=new ArrayList<>();
    	for(SysconfigPO sys:sysList){
    		sysIdList.add(sys.getCid());
    	}
    	List<SysconfigUserPO> sysUserList=this.sysconfigUserService.findSysconfigUserListByUsers(CommUtil.newList(this.getCurrentUserId()), sysIdList);
    	for(SysconfigPO sys:sysList){
    		for(SysconfigUserPO sysUser:sysUserList){
    			if(sys.getCid().longValue()==sysUser.getSysconfigId().longValue()){
    				sys.loadUserConfig(sysUser);
    				break;
    			}
    		}
    	}
    	
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }
    
    @RequestMapping(value = {"findBySysconfigCode"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<SysconfigUserPO> findSysconfigUserBySysconfigCode(@RequestParam String sysconfigCode
    		, @RequestParam(required = false, defaultValue = "CN")String language) {
    	RetResult<SysconfigUserPO> ret = new RetResult<SysconfigUserPO>();
    	if(StringUtils.isEmpty(sysconfigCode)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sysconfigCode is empty", null);
    		return ret;
    	}
    	ret = this.sysconfigUserService.findSysconfigUserBySysconfigCode(this.getCurrentUserId(), sysconfigCode);
    	return ret;
    }
    

//    @RequestMapping(value = "view", method = RequestMethod.GET)
//    @ResponseStatus(value = HttpStatus.OK)
//    @ResponseBody
//    public RetResult<SysconfigUserPO> view(SysconfigUserPO sysconfigUser) {
//    	RetResult<SysconfigUserPO> ret = new RetResult<SysconfigUserPO>();
//    	if(sysconfigUser.getCid()==null){
//    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
//    		return ret;
//    	}
//       
//        sysconfigUser = sysconfigUserService.selectByKey(sysconfigUser.getCid());
//        
//        ret.setData(sysconfigUser);
//        return ret;
//    }
    
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
        
        
        List<SysconfigUserPO> list=sysconfigUserService.findSysconfigUserByUser(this.getCurrentUserId(), sysconfig.getCid());
        if(list.size()>0){
        	SysconfigUserPO sysUser=list.get(0);
        	sysconfig.setValue(sysUser.getValue());
        	sysconfig.setValue1(sysUser.getValue1());
        	sysconfig.setValue2(sysUser.getValue2());
        	sysconfig.setValueJson(sysUser.getValueJson());
        }
        
        
        ret.setData(sysconfig);
        return ret;
    }
    

    /**
     * save or update
     * @param sysconfigUser
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="sysconfigUser.save",author="cjh",calls="update,create")
    public RetResult<Long> save(SysconfigUserPO sysconfigUser) {
    	logger.info("------sysconfigUser--cid="+sysconfigUser.getCid()+" sysconfigId="+sysconfigUser.getSysconfigId());
//        ModelAndView result = new ModelAndView("sysconfigUser");
    	RetResult<Long> ret=new RetResult<Long>();
    	
    	if(sysconfigUser.getSysconfigId()==null||sysconfigUser.getSysconfigId().longValue()<=0){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sysconfigId is empty", null);
    		return ret;
    	}
    	
    	SysconfigPO sysconfig=this.sysconfigService.findSysconfigById(sysconfigUser.getSysconfigId());
    	if(sysconfig==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_UNEXISTED, "sysconfigId="+sysconfigUser.getSysconfigId()+" not exist", null);
    		return ret;
    	}
    	
    	String sysV=sysconfig.getValue()+"_"+sysconfig.getValue1()+"_"+sysconfig.getValue2()+"_"+sysconfig.getValueJson();
    	String sysUserV=sysconfigUser.getValue()+"_"+sysconfigUser.getValue1()+"_"+sysconfigUser.getValue2()+"_"+sysconfigUser.getValueJson();
    	if(sysV.equals(sysUserV)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "data value unchanged", null);
    		return ret;
    	}
    	
    	sysconfigUser.setCid(null);
    	sysconfigUser.setUserId(this.getCurrentUserId());
    	List<SysconfigUserPO> list=sysconfigUserService.findSysconfigUserByUser(this.getCurrentUserId(), sysconfig.getCid());
        if(list.size()>0){
        	SysconfigUserPO sysUser=list.get(0);
        	sysconfigUser.setCid(sysUser.getCid());
        }
    	
    	
        if (sysconfigUser.getCid() != null) {
        	this.initCreate(sysconfigUser);
            sysconfigUserService.update(sysconfigUser);
        } else {
        	this.initUpdate(sysconfigUser);
            sysconfigUserService.create(sysconfigUser);
        }
        ret.setData(sysconfigUser.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="sysconfigUser.delete",author="cjh",calls="deleteVirtual")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
    	if(id==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id is empty", null);
    		return ret;
    	}
    	Long operUserId=AppContext.getCurrentUserId();
    	ret =this.sysconfigUserService.deleteVirtual(operUserId, id);
        return ret;
    }
    
    @RequestMapping(value = {"deleteByUser"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="sysconfigUser.deleteByUser",author="cjh",calls="deleteVirtual")
    public RetResult<Integer> deleteByUser(@RequestParam("sysconfigId") Long sysconfigId) {
    	RetResult<Integer> ret = new RetResult<Integer>();
    	if(sysconfigId==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "sysconfigId is empty", null);
    		return ret;
    	}
    	Long operUserId=AppContext.getCurrentUserId();
    	ret =this.sysconfigUserService.deleteVirtualByUser(operUserId, sysconfigId);
        return ret;
    }
    
}
