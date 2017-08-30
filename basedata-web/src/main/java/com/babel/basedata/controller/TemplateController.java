package com.babel.basedata.controller;

import java.util.Map;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.LogMsgPO;
import com.babel.basedata.model.TemplatePO;
import com.babel.basedata.service.ITemplateService;
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
@RequestMapping("/basedata/template")
public class TemplateController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(TemplateController.class);

    @Autowired
    private ITemplateService templateService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(TemplatePO template){
    	ModelAndView result = new ModelAndView("basedata/ey_template");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<TemplatePO> findByPage(TemplatePO template) {
    	logger.info("-------findByPage-------");
    	PageVO<TemplatePO> pageVO = new PageVO<TemplatePO>(this.getRequest());
    	pageVO = templateService.findPageByTemplate(template, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<TemplatePO> view(TemplatePO template) {
    	RetResult<TemplatePO> ret = new RetResult<TemplatePO>();
        if (template.getCid() != null) {
            template = templateService.selectByKey(template.getCid());
        }
        ret.setData(template);
        return ret;
    }
    
    /**
     * save or update
     * @param template
     * @return
     */
    @RequestMapping(value = "sendMsg", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="sendMsg",  descs="tos=#{0.tos}")
    public RetResult<Long> sendMsg(TemplatePO template) {
//    	logger.info("------template--cid="+template.getCid()+" code="+template.getCode()+" nameCn="+template.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
    	String tos=this.getRequest().getParameter("tos");
    	Map<String, String> paramMap=null;
    	LogMsgPO logMsgPO=null;
    	ret=this.templateService.sendMsg(template, tos, logMsgPO, null);
        return ret;
    }
    
    @RequestMapping(value = "importWxTpl", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="importWxTemplate",  descs="sysType=#{0}")
    public RetResult<String> importWxTemplate(String sysType) {
    	RetResult<String> retResult=this.templateService.importWxTemplate(sysType, this.getCurrentUserId());
    	return retResult;
    }

    /**
     * save or update
     * @param template
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Long> save(TemplatePO template) {
//    	logger.info("------template--cid="+template.getCid()+" code="+template.getCode()+" nameCn="+template.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
        if (template.getCid() != null) {
        	template.initUpdate();
            templateService.updateNotNull(template);
        } else {
        	template.initCreate();
            templateService.create(template);
        }
        ret.setData(template.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			Long operUserId=AppContext.getCurrentUserId();
			ret =this.templateService.deleteVirtual(operUserId, id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
