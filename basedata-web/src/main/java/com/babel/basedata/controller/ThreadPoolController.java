package com.babel.basedata.controller;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.ThreadPoolPO;
import com.babel.basedata.service.IThreadPoolService;
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
@RequestMapping("/basedata/threadPool")
public class ThreadPoolController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(ThreadPoolController.class);

    @Autowired
    private IThreadPoolService threadPoolService;
    
		
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(ThreadPoolPO threadPool){
    	ModelAndView result = new ModelAndView("basedata/ey_threadPool");
    	return result;
    }
    
    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="thread list")
    public PageVO<ThreadPoolPO> findByPage(ThreadPoolPO threadPool) {
    	logger.info("-------findByPage-------");
    	PageVO<ThreadPoolPO> pageVO = new PageVO<ThreadPoolPO>(this.getRequest());
    	pageVO = threadPoolService.findPageByThreadPool(threadPool, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<ThreadPoolPO> view(ThreadPoolPO threadPool) {
    	RetResult<ThreadPoolPO> ret = new RetResult<ThreadPoolPO>();
        if (threadPool.getCid() != null) {
            threadPool = threadPoolService.selectByKey(threadPool.getCid());
        }
        ret.setData(threadPool);
        return ret;
    }
    

    /**
     * save or update
     * @param threadPool
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Long> save(ThreadPoolPO threadPool) {
//    	logger.info("------threadPool--cid="+threadPool.getCid()+" code="+threadPool.getCode()+" nameCn="+threadPool.getNameCn());
    	RetResult<Long> ret=new RetResult<Long>();
        if (threadPool.getCid() != null) {
        	threadPool.initUpdate();
            threadPoolService.updateNotNull(threadPool);
        } else {
        	threadPool.initCreate();
            threadPoolService.create(threadPool);
        }
        ret.setData(threadPool.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
        RetResult<Integer> ret = new RetResult<Integer>();
		try {
			Long operUserId=AppContext.getCurrentUserId();
			ret =this.threadPoolService.deleteVirtual(operUserId, id);
		} catch (RetException e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN , e.getMessage(), e);
		}
        return ret;
    }
    
}
