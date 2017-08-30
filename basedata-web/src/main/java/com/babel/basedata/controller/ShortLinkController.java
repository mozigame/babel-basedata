package com.babel.basedata.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.data.RetResult.msg_codes;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.util.RedisUtil;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.ShortLinkPO;
import com.babel.basedata.service.IShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author liuzh_3nofxnp
 * @since 2015-09-19 17:15
 */
@Controller
@RequestMapping("/basedata/shortLink")
public class ShortLinkController extends WebBaseController {
	private static final Logger logger = Logger.getLogger(ShortLinkController.class);

    @Autowired
    private IShortLinkService shortLinkService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(ShortLinkPO shortLink){
    	ModelAndView result = new ModelAndView("basedata/ey_shortLink");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<ShortLinkPO> findByPage(ShortLinkPO shortLink) {
    	logger.info("-------findByPage-------");
    	PageVO<ShortLinkPO> pageVO = new PageVO<ShortLinkPO>(this.getRequest());
    	pageVO = shortLinkService.findPageByShortLink(shortLink, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ShortLinkPO> view(ShortLinkPO shortLink) {
    	RetResult<ShortLinkPO> ret = new RetResult<ShortLinkPO>();
        if (shortLink.getCid() != null) {
            shortLink = shortLinkService.selectByKey(shortLink.getCid());
        }
        ret.setData(shortLink);
        return ret;
    }
    
    @RequestMapping(value = "generate", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ShortLinkPO> generate(ShortLinkPO shortLink) {
    	RetResult<ShortLinkPO> ret = new RetResult<ShortLinkPO>();
    	if(StringUtils.isEmpty(shortLink.getData())){
    		return ret.initError(msg_codes.ERR_DATA_INPUT, "data is empty", null);
    	}
    	if(shortLink.getShortType()==null){
    		return ret.initError(msg_codes.ERR_DATA_INPUT, "shortType is empty", null);
    	}
    	if(shortLink.getCid()!=null && shortLink.getCid()>0){//如果cid不为空表示修改，直接保存即可
    		this.save(shortLink);
    		return ret;
    	}
    	String urlSrc=shortLink.getData();
    	String shortCode=RedisUtil.genShortUrlKey(shortLink.getShortType(), urlSrc);
//    	//保存短链接到数据库
//    	ShortLinkPO shortLink=new ShortLinkPO();
    	shortLink.setCode(shortCode);
//    	shortLink.setData(urlSrc);
//    	shortLink.setInfoType(logMsg.getMsgCode());
//    	shortLink.setShortType(logMsg.getMsgType());//url
    	this.shortLinkService.create(shortLink);
    	
        return ret;
    }

    /**
     * save or update
     * @param shortLink
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Long> save(ShortLinkPO shortLink) {
    	logger.info("------shortLink--cid="+shortLink.getCid()+" code="+shortLink.getCode()+" data="+shortLink.getData());
    	RetResult<Long> ret=new RetResult<Long>();
        if (shortLink.getCid() != null) {
        	shortLink.initUpdate();
            shortLinkService.updateNotNull(shortLink);
        } else {
        	shortLink.initCreate();
            shortLinkService.create(shortLink);
        }
        ret.setData(shortLink.getCid());
        //更新缓存
        RedisUtil.putRedis(null, "K_SHORT_LINK_"+shortLink.getShortType(), shortLink.getCode(), shortLink.getData());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			ret =this.shortLinkService.deleteVirtual(id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
