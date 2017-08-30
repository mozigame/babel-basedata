package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.controller.BaseController;
import com.babel.basedata.model.RetryRuleDetailPO;
import com.babel.basedata.model.WhiteListPO;
import com.babel.basedata.service.IWhiteListService;
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
@RequestMapping("/basedata/whiteList")
public class WhiteListController extends BaseController {
	private static final Log logger = LogFactory.getLog(WhiteListController.class);

    @Autowired
    private IWhiteListService whiteListService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(WhiteListPO whiteList){
    	ModelAndView result = new ModelAndView("basedata/ey_whiteList");
    	return result;
    }
    
    @RequestMapping(value = {"manage", ""})
    public ModelAndView manage(RetryRuleDetailPO retryRuleDetail){
    	ModelAndView result = new ModelAndView("basedata/ey_whiteManage");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<WhiteListPO> findByPage(WhiteListPO whiteList) {
    	logger.info("-------findByPage-------");
    	PageVO<WhiteListPO> pageVO = new PageVO<WhiteListPO>(this.getRequest());
    	pageVO = whiteListService.findPageByWhiteList(whiteList, pageVO);
    	return pageVO;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<WhiteListPO> view(WhiteListPO whiteList) {
    	RetResult<WhiteListPO> ret = new RetResult<WhiteListPO>();
        if (whiteList.getCid() != null) {
            whiteList = whiteListService.selectByKey(whiteList.getCid());
        }
        ret.setData(whiteList);
        return ret;
    }
    

    /**
     * save or update
     * @param whiteList
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Long> save(WhiteListPO whiteList) {
//    	logger.info("------whiteList--cid="+whiteList.getCid()+" code="+whiteList.getCode()+" nameCn="+whiteList.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
        if (whiteList.getCid() != null) {
        	this.initUpdate(whiteList);
            ret=whiteListService.update(whiteList);
        } else {
        	this.initCreate(whiteList);
            ret=whiteListService.create(whiteList);
        }
        ret.setData(whiteList.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			ret =this.whiteListService.deleteVirtual(id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
