package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.LogMsgPO;
import com.babel.basedata.service.ILogMsgService;
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
@RequestMapping("/basedata/logmsg")
public class LogMsgController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(LogMsgController.class);

    @Autowired
    private ILogMsgService logMsgService;
    
    @RequestMapping(value = {"index", ""})
    public ModelAndView index(LogMsgPO logMsg){
    	ModelAndView result = new ModelAndView("basedata/ey_logMsg");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<LogMsgPO> findByPage(LogMsgPO logMsg) {
    	logger.info("-------findByPage-------");
    	PageVO<LogMsgPO> pageVO = new PageVO<LogMsgPO>(this.getRequest());
    	pageVO= logMsgService.findPageByLogMsg(logMsg, pageVO, null, null);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }
    


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<LogMsgPO> view(LogMsgPO logMsg) {
    	RetResult<LogMsgPO> ret = new RetResult<LogMsgPO>();
    	if(logMsg.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
        logMsg = logMsgService.selectByKey(logMsg.getCid());
        
        ret.setData(logMsg);
        return ret;
    }
    


    
}
