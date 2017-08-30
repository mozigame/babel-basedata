package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.ProductCdkeyPO;
import com.babel.basedata.service.IProductCdkeyService;
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
@RequestMapping("/basedata/productCdkey")
public class ProductCdkeyController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(ProductCdkeyController.class);

    @Autowired
    private IProductCdkeyService productCdkeyService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(ProductCdkeyPO productCdkey){
    	ModelAndView result = new ModelAndView("basedata/ey_productCdkey");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<ProductCdkeyPO> findByPage(ProductCdkeyPO productCdkey) {
    	logger.info("-------findByPage-------");
    	PageVO<ProductCdkeyPO> pageVO = new PageVO<ProductCdkeyPO>(this.getRequest());
    	pageVO = productCdkeyService.findPageByProductCdkey(productCdkey, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ProductCdkeyPO> view(ProductCdkeyPO productCdkey) {
    	RetResult<ProductCdkeyPO> ret = new RetResult<ProductCdkeyPO>();
        if (productCdkey.getCid() != null) {
            productCdkey = productCdkeyService.selectByKey(productCdkey.getCid());
        }
        ret.setData(productCdkey);
        return ret;
    }
    

    /**
     * save or update
     * @param productCdkey
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Long> save(ProductCdkeyPO productCdkey) {
//    	logger.info("------productCdkey--cid="+productCdkey.getCid()+" code="+productCdkey.getCode()+" nameCn="+productCdkey.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
        if (productCdkey.getCid() != null) {
        	productCdkey.initUpdate();
            productCdkeyService.updateNotNull(productCdkey);
        } else {
        	productCdkey.initCreate();
            productCdkeyService.create(productCdkey);
        }
        ret.setData(productCdkey.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			ret =this.productCdkeyService.deleteVirtual(id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
