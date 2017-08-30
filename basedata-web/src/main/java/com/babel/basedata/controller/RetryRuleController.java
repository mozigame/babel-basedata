package com.babel.basedata.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.RetryRulePO;
import com.babel.basedata.service.IRetryRuleService;
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
@RequestMapping("/basedata/retryRule")
public class RetryRuleController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(RetryRuleController.class);

    @Autowired
    private IRetryRuleService retryRuleService;
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(RetryRulePO retryRule){
    	ModelAndView result = new ModelAndView("basedata/ey_retryRule");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="retryRule",author="cjh",calls="findPageByRetryRule",  descs=LogAudit.noRet)
    public PageVO<RetryRulePO> findByPage(RetryRulePO retryRule) {
    	logger.info("-------findByPage-------");
    	PageVO<RetryRulePO> pageVO = new PageVO<RetryRulePO>(this.getRequest());
    	PageVO<RetryRulePO> p= retryRuleService.findPageByRetryRule(retryRule, pageVO);
    	this.initDisp(p.getRows());
    	return p;
    }
    
    @RequestMapping(value = "findRetryRuleAll", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public List<RetryRulePO> findRetryRuleAll(){
    	return this.retryRuleService.findRetryRuleAll();
    }



    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<RetryRulePO> view(RetryRulePO retryRule) {
    	RetResult<RetryRulePO> ret = new RetResult<RetryRulePO>();
    	if(retryRule.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
        
        retryRule = retryRuleService.selectByKey(retryRule.getCid());
        
        ret.setData(retryRule);
        return ret;
    }
    

    /**
     * save or update
     * @param retryRule
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="retryRule.save",author="cjh",calls="update,create",  descs="cid=#{0.cid},code=#{0.code}")
    public RetResult<Long> save(RetryRulePO retryRule) {
    	logger.info("------retryRule--cid="+retryRule.getCid()+" code="+retryRule.getCode()+" name="+retryRule.getName());
//        ModelAndView result = new ModelAndView("retryRule");
    	RetResult<Long> ret=new RetResult<Long>();
    	if(StringUtils.isEmpty(retryRule.getCode())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code is empty", null);
    		return ret;
    	}
    	if(StringUtils.isEmpty(retryRule.getName())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "nameCn is empty", null);
    		return ret;
    	}
    	
        if (retryRule.getCid() != null) {
        	this.initUpdate(retryRule);
            retryRuleService.update(retryRule);
        } else {
        	this.initCreate(retryRule);
            retryRuleService.create(retryRule);
        }
        ret.setData(retryRule.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="retryRule.delete",author="cjh",calls="deleteVirtual",  descs="cid=#{0}")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
		if(id==null||id==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id="+id+" is empty", null);
			return ret;
		}
    	Long operUserId=AppContext.getCurrentUserId();
    	try {
			ret =this.retryRuleService.deleteVirtual(operUserId, id);
		} catch (RetException e) {
			ret=new RetResult(e.getRetResult());
		}
        return ret;
    }
    
}
