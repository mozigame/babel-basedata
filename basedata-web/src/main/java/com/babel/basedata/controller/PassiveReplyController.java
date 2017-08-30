package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.PassiveReplyPO;
import com.babel.basedata.service.IPassiveReplyService;
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
@RequestMapping("/basedata/passiveReply")
public class PassiveReplyController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(PassiveReplyController.class);

    @Autowired
    private IPassiveReplyService passiveReplyService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(PassiveReplyPO passiveReply){
    	ModelAndView result = new ModelAndView("basedata/ey_passiveReply");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<PassiveReplyPO> findByPage(PassiveReplyPO passiveReply) {
    	logger.info("-------findByPage-------");
    	PageVO<PassiveReplyPO> pageVO = new PageVO<PassiveReplyPO>(this.getRequest());
    	pageVO = passiveReplyService.findPageByPassiveReply(passiveReply, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<PassiveReplyPO> view(PassiveReplyPO passiveReply) {
    	RetResult<PassiveReplyPO> ret = new RetResult<PassiveReplyPO>();
        if (passiveReply.getCid() != null) {
            passiveReply = passiveReplyService.selectByKey(passiveReply.getCid());
        }
        ret.setData(passiveReply);
        return ret;
    }
    

    /**
     * save or update
     * @param passiveReply
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Long> save(PassiveReplyPO passiveReply) {
//    	logger.info("------passiveReply--cid="+passiveReply.getCid()+" code="+passiveReply.getCode()+" nameCn="+passiveReply.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
        if (passiveReply.getCid() != null) {
        	passiveReply.initUpdate();
            passiveReplyService.updateNotNull(passiveReply);
        } else {
        	passiveReply.initCreate();
            passiveReplyService.create(passiveReply);
        }
        ret.setData(passiveReply.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			Long operUserId=AppContext.getCurrentUserId();
			ret =this.passiveReplyService.deleteVirtual(operUserId, id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
