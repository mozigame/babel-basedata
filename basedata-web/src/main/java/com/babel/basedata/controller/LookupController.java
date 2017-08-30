package com.babel.basedata.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.LookupPO;
import com.babel.basedata.service.ILookupService;
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
import com.github.pagehelper.PageInfo;

/**
 * @author liuzh_3nofxnp
 * @since 2015-09-19 17:15
 */
@Controller
@RequestMapping("/basedata/lookup")
public class LookupController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(LookupController.class);

    @Autowired
    private ILookupService lookupService;
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(LookupPO lookup){
    	ModelAndView result = new ModelAndView("basedata/ey_lookup");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="lookup",author="cjh",calls="findPageByLookup",  descs=LogAudit.noRet)
    public PageVO<LookupPO> findByPage(LookupPO lookup) {
    	logger.info("-------findByPage-------");
    	PageVO<LookupPO> pageVO = new PageVO<LookupPO>(this.getRequest());
    	PageVO<LookupPO> p= lookupService.findPageByLookup(lookup, pageVO);
    	this.initDisp(p.getRows());
    	return p;
    }
    
    
    @RequestMapping(value = {"list2"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ModelAndView findByPage2(LookupPO lookup,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "10") int rows) {
//    	System.out.println("-------getList-------");
        ModelAndView result = new ModelAndView();
        List<LookupPO> lookupList = lookupService.selectByLookup(lookup, page, rows);
        result.addObject("pageInfo", new PageInfo<LookupPO>(lookupList));
        result.addObject("queryParam", lookup);
        result.addObject("page", page);
        result.addObject("rows", rows);
        return result;

    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<LookupPO> view(LookupPO lookup) {
    	RetResult<LookupPO> ret = new RetResult<LookupPO>();
    	if(lookup.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
        
        lookup = lookupService.selectByKey(lookup.getCid());
        
        ret.setData(lookup);
        return ret;
    }
    

    /**
     * save or update
     * @param lookup
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="lookup.save",author="cjh",calls="update,create",  descs="cid=#{0.cid},code=#{0.code}")
    public RetResult<Long> save(LookupPO lookup) {
    	logger.info("------lookup--cid="+lookup.getCid()+" code="+lookup.getCode()+" nameCn="+lookup.getNameCn());
//        ModelAndView result = new ModelAndView("lookup");
    	RetResult<Long> ret=new RetResult<Long>();
    	if(StringUtils.isEmpty(lookup.getCode())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "code is empty", null);
    		return ret;
    	}
    	if(StringUtils.isEmpty(lookup.getNameCn())){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "nameCn is empty", null);
    		return ret;
    	}
    	
        if (lookup.getCid() != null) {
        	this.initUpdate(lookup);
            lookupService.update(lookup);
        } else {
        	this.initCreate(lookup);
            lookupService.create(lookup);
        }
        ret.setData(lookup.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="lookup.delete",author="cjh",calls="deleteVirtual",  descs="cid=#{0}")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
		if(id==null||id==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id="+id+" is empty", null);
			return ret;
		}
    	Long operUserId=AppContext.getCurrentUserId();
    	try {
			ret =this.lookupService.deleteVirtual(operUserId, id);
		} catch (RetException e) {
			ret=new RetResult(e.getRetResult());
		}
        return ret;
    }
    
}
