package com.babel.basedata.controller;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.common.web.loader.IContextTaskLoader;
import com.babel.basedata.model.LookupItemPO;
import com.babel.basedata.service.ILookupItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping("/basedata/lookupItem")
public class LookupItemController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(LookupItemController.class);

    @Autowired
    private ILookupItemService lookupItemService;
    
    @Autowired
    @Qualifier("lookupJsResetTaskLoader")
    private IContextTaskLoader lookupJsResetTaskLoader;
    
    @RequestMapping(value = {"index",  ""})
    public ModelAndView index(LookupItemPO lookupItem){
    	ModelAndView result = new ModelAndView("basedata/ey_lookupItem");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    @RequestMapping(value = {"manage", ""})
    public ModelAndView manage(LookupItemPO lookupItem){
    	ModelAndView result = new ModelAndView("basedata/ey_lookupManage");
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<LookupItemPO> findByPage(LookupItemPO lookupItem) {
    	logger.info("-------findByPage-------");
    	PageVO<LookupItemPO> pageVO = new PageVO<LookupItemPO>(this.getRequest());
    	PageVO<LookupItemPO> p= lookupItemService.findPageByLookupItem(lookupItem, pageVO);
    	this.initDisp(p.getRows());
    	return p;
    }
    
    @RequestMapping(value = {"findByLookupCode"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<LookupItemPO> findLookupItemByLookupCode(@RequestParam String lookupCode
    		, @RequestParam(required = false, defaultValue = "CN")String language) {
    	RetResult<LookupItemPO> ret = new RetResult<LookupItemPO>();
    	if(StringUtils.isEmpty(lookupCode)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "lookupCode is empty", null);
    		return ret;
    	}
    	ret = this.lookupItemService.findLookupItemByLookupCode(language, lookupCode);
    	return ret;
    }
    

    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<LookupItemPO> view(LookupItemPO lookupItem) {
    	RetResult<LookupItemPO> ret = new RetResult<LookupItemPO>();
    	if(lookupItem.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
       
        lookupItem = lookupItemService.selectByKey(lookupItem.getCid());
        
        ret.setData(lookupItem);
        return ret;
    }
    

    /**
     * save or update
     * @param lookupItem
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="lookupItem.save",author="cjh",calls="update,create")
    public RetResult<Long> save(LookupItemPO lookupItem) {
    	logger.info("------lookupItem--cid="+lookupItem.getCid()+" itemCode="+lookupItem.getItemCode()+" itemName="+lookupItem.getItemName());
//        ModelAndView result = new ModelAndView("lookupItem");
    	RetResult<Long> ret=new RetResult<Long>();
    	if(StringUtils.isEmpty(lookupItem.getItemCode())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "itemCode is empty", null);
    		return ret;
    	}
    	if(StringUtils.isEmpty(lookupItem.getItemName())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "itemName is empty", null);
    		return ret;
    	}
    	
        if (lookupItem.getCid() != null) {
        	this.initCreate(lookupItem);
            lookupItemService.update(lookupItem);
        } else {
        	this.initUpdate(lookupItem);
            lookupItemService.create(lookupItem);
        }
        ret.setData(lookupItem.getCid());
        RetResult<String> retJs=this.lookupJsResetTaskLoader.execute(null);
        if(retJs.isSuccess()){
        	logger.info("-------lookupItem--resetJs success");
        }
        else{
        	logger.warn("-------lookupItem--resetJs fail msgBody="+ret.getMsgBody());
        }
        
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="lookupItem.delete",author="cjh",calls="deleteVirtual")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
    	if(id==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id is empty", null);
    		return ret;
    	}
    	Long operUserId=AppContext.getCurrentUserId();
    	ret =this.lookupItemService.deleteVirtual(operUserId, id);
    	 RetResult<String> retJs=this.lookupJsResetTaskLoader.execute(null);
         if(retJs.isSuccess()){
         	logger.info("-------lookupItem--resetJs success");
         }
         else{
         	logger.warn("-------lookupItem--resetJs fail msgBody="+ret.getMsgBody());
         }
        return ret;
    }
    
}
