package com.babel.basedata.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.controller.WebBaseController;
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
import com.babel.basedata.model.LogDbPO;
import com.babel.basedata.service.ILogDbService;

/**
 * @author liuzh_3nofxnp
 * @since 2015-09-19 17:15
 */
@Controller
@RequestMapping("/basedata/logdb")
public class LogDbController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(LogDbController.class);

    @Autowired
    private ILogDbService logDbService;
    
    @RequestMapping(value = {"index", ""})
    public ModelAndView index(LogDbPO logDb){
    	ModelAndView result = new ModelAndView("basedata/ey_logDb");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="findByPage", descs="logLevel:#logDb.logLevel")
    public PageVO<LogDbPO> findByPage(LogDbPO logDb
                                ) {
    	logger.info("-------findByPage-------");
    	PageVO<LogDbPO> pageVO = new PageVO<LogDbPO>(this.getRequest());
    	pageVO.setOrder(this.getRequest().getParameter("order"));
    	pageVO.setSort(this.getRequest().getParameter("sort"));
    	return logDbService.findPageByLogDb(logDb, pageVO);
    }
    
    @RequestMapping(value = {"reportIntfCall"})
    public ModelAndView reportIntfCall(LogDbPO logDb){
    	ModelAndView result = new ModelAndView("basedata/r_logDb_dayCallCount");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    @RequestMapping(value = {"reportIntfTopCount"})
    public ModelAndView reportIntfTopCount(LogDbPO logDb){
    	ModelAndView result = new ModelAndView("basedata/r_logDb_dayCallTopCount");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    @RequestMapping(value = {"reportIntfTopInfo"})
    public ModelAndView reportIntfTopInfo(LogDbPO logDb){
    	ModelAndView result = new ModelAndView("basedata/r_logDb_dayCallTopInfo");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    @RequestMapping(value = {"reportIntfCallInfo"})
    public ModelAndView reportIntfCallInfo(LogDbPO logDb){
    	ModelAndView result = new ModelAndView("basedata/r_logDb_dayCallInfo");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    
    
    @RequestMapping(value = {"dayCallCount"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Map<String, Object>> staticIntfCallCount(Date startDate, Date endDate, HashMap<String, Object> paramMap) {
    	if(paramMap.isEmpty()){
	    	paramMap.putAll(this.getParamMap());
    	}
    	logger.info("-------staticIntfCallCount-------paramMap="+paramMap);
    	
    	return logDbService.staticIntfCallCount(startDate, endDate, paramMap);
//    	return new RetResult<>();
    	
    }
    
    @RequestMapping(value = {"dayIntfCallTopCount"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Map<String, Object>> staticIntfCodeTopCount(Date startDate, Date endDate,Integer topN, HashMap<String, Object> paramMap) {
    	if(paramMap.isEmpty()){
	    	paramMap.putAll(this.getParamMap());
    	}
    	logger.info("-------staticIntfCodeTopCount-------paramMap="+paramMap);
    	return  logDbService.staticIntfCodeTopCount(startDate, endDate, topN, paramMap);
    }
    
    /**
     * 统计主要信息
     * @param startDate
     * @param endDate
     * @param topN
     * @param paramMap
     * @return
     */
    @RequestMapping(value = {"staticUuidMaxDepth"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Map<String, Object>> staticUuidMaxDepth(Date startDate, Date endDate,Integer topN, HashMap<String, Object> paramMap) {
    	if(paramMap.isEmpty()){
	    	paramMap.putAll(this.getParamMap());
    	}
    	logger.info("-------staticUuidMaxDepth-------paramMap="+paramMap);
    	return  logDbService.staticUuidMaxDepth(startDate, endDate, topN, paramMap);
    }
    
    /**
     * 统计主要信息
     * @param startDate
     * @param endDate
     * @param topN
     * @param paramMap
     * @return
     */
    @RequestMapping(value = {"staticIntfCodeMaxRunTime"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Map<String, Object>> staticIntfCodeMaxRunTime(Date startDate, Date endDate,Integer topN, HashMap<String, Object> paramMap) {
    	if(paramMap.isEmpty()){
	    	paramMap.putAll(this.getParamMap());
    	}
    	logger.info("-------staticIntfCodeMaxRunTime-------paramMap="+paramMap);
    	return  logDbService.staticIntfCodeMaxRunTime(startDate, endDate, topN, paramMap);
    }
    
    /**
     * 统计时段内发生的所有每个接口的调用次数，最新调用时间，最大执行时间等
     * @param startDate
     * @param endDate
     * @param topN
     * @param paramMap
     * @return
     */
    @RequestMapping(value = {"stataicIntfCodeShowAll"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Map<String, Object>> stataicIntfCodeShowAll(Date startDate, Date endDate,Integer topN, HashMap<String, Object> paramMap) {
    	if(paramMap.isEmpty()){
	    	paramMap.putAll(this.getParamMap());
    	}
    	logger.info("-------staticIntfCodeMaxRunTime-------paramMap="+paramMap);
    	return  logDbService.stataicIntfCodeShowAll(startDate, endDate, topN, paramMap);
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<LogDbPO> view(LogDbPO logDb) {
    	RetResult<LogDbPO> ret = new RetResult<LogDbPO>();
    	if(logDb.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
        logDb = logDbService.selectByKey(logDb.getCid());
        
        ret.setData(logDb);
        return ret;
    }
    


    
}
