package com.babel.basedata.controller;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.RetryRuleDetailPO;
import com.babel.basedata.service.IRetryRuleDetailService;
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
@RequestMapping("/basedata/retryRuleDetail")
public class RetryRuleDetailController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(RetryRuleDetailController.class);

    @Autowired
    private IRetryRuleDetailService retryRuleDetailService;

    
    @RequestMapping(value = {"index",  ""})
    public ModelAndView index(RetryRuleDetailPO retryRuleDetail){
    	ModelAndView result = new ModelAndView("basedata/ey_retryRuleDetail");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    @RequestMapping(value = {"manage", ""})
    public ModelAndView manage(RetryRuleDetailPO retryRuleDetail){
    	ModelAndView result = new ModelAndView("basedata/ey_retryManage");
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<RetryRuleDetailPO> findByPage(RetryRuleDetailPO retryRuleDetail) {
    	logger.info("-------findByPage-------");
    	PageVO<RetryRuleDetailPO> pageVO = new PageVO<RetryRuleDetailPO>(this.getRequest());
    	PageVO<RetryRuleDetailPO> p= retryRuleDetailService.findPageByRetryRuleDetail(retryRuleDetail, pageVO);
    	this.initDisp(p.getRows());
    	return p;
    }
    
    @RequestMapping(value = {"findByRetryCode"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<RetryRuleDetailPO> findRetryRuleDetailByRuleCode(@RequestParam String ruleCode
    		, @RequestParam(required = false, defaultValue = "CN")String language) {
    	RetResult<RetryRuleDetailPO> ret = new RetResult<RetryRuleDetailPO>();
    	if(StringUtils.isEmpty(ruleCode)){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "ruleCode is empty", null);
    		return ret;
    	}
    	ret = this.retryRuleDetailService.findRetryRuleDetailByRuleCode(ruleCode);
    	return ret;
    }
    

    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<RetryRuleDetailPO> view(RetryRuleDetailPO retryRuleDetail) {
    	RetResult<RetryRuleDetailPO> ret = new RetResult<RetryRuleDetailPO>();
    	if(retryRuleDetail.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
       
        retryRuleDetail = retryRuleDetailService.selectByKey(retryRuleDetail.getCid());
        
        ret.setData(retryRuleDetail);
        return ret;
    }
    

    /**
     * save or update
     * @param retryRuleDetail
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="retryRuleDetail.save",author="cjh",calls="update,create")
    public RetResult<Long> save(RetryRuleDetailPO retryRuleDetail) {
    	logger.info("------retryRuleDetail--cid="+retryRuleDetail.getCid()+" code="+retryRuleDetail.getCode()+" name="+retryRuleDetail.getName());
//        ModelAndView result = new ModelAndView("retryRuleDetail");
    	RetResult<Long> ret=new RetResult<Long>();
    	if(StringUtils.isEmpty(retryRuleDetail.getCode())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code is empty", null);
    		return ret;
    	}
    	if(StringUtils.isEmpty(retryRuleDetail.getName())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "name is empty", null);
    		return ret;
    	}
    	
        if (retryRuleDetail.getCid() != null) {
        	this.initCreate(retryRuleDetail);
            retryRuleDetailService.update(retryRuleDetail);
        } else {
        	this.initUpdate(retryRuleDetail);
            retryRuleDetailService.create(retryRuleDetail);
        }
        ret.setData(retryRuleDetail.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="retryRuleDetail.delete",author="cjh",calls="deleteVirtual")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
    	if(id==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id is empty", null);
    		return ret;
    	}
    	Long operUserId=AppContext.getCurrentUserId();
    	ret =this.retryRuleDetailService.deleteVirtual(operUserId, id);
    	
        return ret;
    }
    
}
