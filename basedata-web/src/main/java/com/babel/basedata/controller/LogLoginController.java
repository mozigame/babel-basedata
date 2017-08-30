package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.LogLoginPO;
import com.babel.basedata.service.ILogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * @author jinhe.chen
 * @since 2016-07-06
 */
@Controller
@RequestMapping("/basedata/loglogin")
public class LogLoginController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(LogLoginController.class);

    @Autowired
    private ILogLoginService logLoginService;
    
    @RequestMapping(value = {"index", ""})
    public ModelAndView index(LogLoginPO logLogin){
    	ModelAndView result = new ModelAndView("basedata/ey_logLogin");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<LogLoginPO> findByPage(LogLoginPO logLogin) {
    	logger.info("-------findByPage-------");
    	PageVO<LogLoginPO> pageVO = new PageVO<LogLoginPO>(this.getRequest());
    	pageVO= logLoginService.findPageByLogLogin(logLogin, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }
    


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<LogLoginPO> view(LogLoginPO logLogin) {
    	RetResult<LogLoginPO> ret = new RetResult<LogLoginPO>();
    	if(logLogin.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
        logLogin = logLoginService.selectByKey(logLogin.getCid());
        
        ret.setData(logLogin);
        return ret;
    }
    


    
}
