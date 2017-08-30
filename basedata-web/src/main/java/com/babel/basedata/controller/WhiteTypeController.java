package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.controller.BaseController;
import com.babel.basedata.model.WhiteTypePO;
import com.babel.basedata.service.IWhiteTypeService;
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
@RequestMapping("/basedata/whiteType")
public class WhiteTypeController extends BaseController {
	private static final Log logger = LogFactory.getLog(WhiteTypeController.class);

    @Autowired
    private IWhiteTypeService whiteTypeService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(WhiteTypePO whiteType){
    	ModelAndView result = new ModelAndView("basedata/ey_whiteType");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<WhiteTypePO> findByPage(WhiteTypePO whiteType) {
    	logger.info("-------findByPage-------");
    	PageVO<WhiteTypePO> pageVO = new PageVO<WhiteTypePO>(this.getRequest());
    	pageVO = whiteTypeService.findPageByWhiteType(whiteType, pageVO);
    	return pageVO;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<WhiteTypePO> view(WhiteTypePO whiteType) {
    	RetResult<WhiteTypePO> ret = new RetResult<WhiteTypePO>();
        if (whiteType.getCid() != null) {
            whiteType = whiteTypeService.selectByKey(whiteType.getCid());
        }
        ret.setData(whiteType);
        return ret;
    }
    

    /**
     * save or update
     * @param whiteType
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Long> save(WhiteTypePO whiteType) {
//    	logger.info("------whiteType--cid="+whiteType.getCid()+" code="+whiteType.getCode()+" nameCn="+whiteType.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
        if (whiteType.getCid() != null) {
        	this.initUpdate(whiteType);
            whiteTypeService.update(whiteType);
        } else {
        	this.initCreate(whiteType);
            whiteTypeService.create(whiteType);
        }
        ret.setData(whiteType.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			ret =this.whiteTypeService.deleteVirtual(id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
