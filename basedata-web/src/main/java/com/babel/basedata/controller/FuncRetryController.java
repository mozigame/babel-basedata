package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
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
import com.babel.basedata.model.FuncRetryPO;
import com.babel.basedata.service.IFuncRetryService;

/**
 * @author liuzh_3nofxnp
 * @since 2015-09-19 17:15
 */
@Controller
@RequestMapping("/basedata/funcRetry")
public class FuncRetryController extends WebBaseController{
	private static final Log logger = LogFactory.getLog(FuncRetryController.class);

    @Autowired
    private IFuncRetryService funcRetryService;
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(FuncRetryPO funcRetry){
    	ModelAndView result = new ModelAndView("basedata/ey_funcRetry");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<FuncRetryPO> findByPage(FuncRetryPO funcRetry) {
    	logger.info("-------findByPage-------");
    	PageVO<FuncRetryPO> pageVO = new PageVO<FuncRetryPO>(this.getRequest());
    	pageVO= funcRetryService.findPageByFuncRetry(funcRetry, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }
    
    



    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<FuncRetryPO> view(FuncRetryPO funcRetry) {
    	RetResult<FuncRetryPO> ret = new RetResult<FuncRetryPO>();
        if (funcRetry.getCid() != null) {
            funcRetry = funcRetryService.selectByKey(funcRetry.getCid());
        }
        ret.setData(funcRetry);
        return ret;
    }
    

    /**
     * save or update
     * @param funcRetry
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="funcRetry.save",author="cjh",calls="update,create")
    public RetResult<Long> save(FuncRetryPO funcRetry) {
//    	logger.info("------funcRetry--cid="+funcRetry.getCid()+" code="+funcRetry.getCode()+" nameCn="+funcRetry.getNameCn());
//        ModelAndView result = new ModelAndView("funcRetry");
    	RetResult<Long> ret=new RetResult<Long>();
        if (funcRetry.getCid() != null) {
        	this.initUpdate(funcRetry);
            funcRetryService.update(funcRetry);
        } else {
        	this.initCreate(funcRetry);
            funcRetryService.create(funcRetry);
        }
        ret.setData(funcRetry.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="funcRetry.delete",author="cjh",calls="deleteVirtual")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	Long operUserId=AppContext.getCurrentUserId();
    	RetResult<Integer> ret =this.funcRetryService.deleteVirtual(operUserId, id);
        return ret;
    }
    
}
