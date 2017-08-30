package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.ProductKeyPO;
import com.babel.basedata.service.IProductKeyService;
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
@RequestMapping("/basedata/productKey")
public class ProductKeyController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(ProductKeyController.class);

    @Autowired
    private IProductKeyService productKeyService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(ProductKeyPO productKey){
    	ModelAndView result = new ModelAndView("basedata/ey_productKey");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<ProductKeyPO> findByPage(ProductKeyPO productKey) {
    	logger.info("-------findByPage-------");
    	PageVO<ProductKeyPO> pageVO = new PageVO<ProductKeyPO>(this.getRequest());
    	pageVO = productKeyService.findPageByProductKey(productKey, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ProductKeyPO> view(ProductKeyPO productKey) {
    	RetResult<ProductKeyPO> ret = new RetResult<ProductKeyPO>();
        if (productKey.getCid() != null) {
            productKey = productKeyService.selectByKey(productKey.getCid());
        }
        ret.setData(productKey);
        return ret;
    }
    

    /**
     * save or update
     * @param productKey
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Long> save(ProductKeyPO productKey) {
//    	logger.info("------productKey--cid="+productKey.getCid()+" code="+productKey.getCode()+" nameCn="+productKey.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
        if (productKey.getCid() != null) {
        	productKey.initUpdate();
            productKeyService.updateNotNull(productKey);
        } else {
        	productKey.initCreate();
            productKeyService.create(productKey);
        }
        ret.setData(productKey.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			Long operUserId=AppContext.getCurrentUserId();
			ret =this.productKeyService.deleteVirtual(operUserId, id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
