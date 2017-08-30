package com.babel.basedata.controller;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.UserPO;
import com.babel.basedata.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
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
@RequestMapping("/basedata/user")
public class UserController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(UserController.class);

    @Autowired
    private IUserService userService;
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(UserPO user){
    	ModelAndView result = new ModelAndView("basedata/ey_user");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<UserPO> findByPage(UserPO user) {
    	logger.info("-------findByPage-------");
    	PageVO<UserPO> pageVO = new PageVO<UserPO>(this.getRequest());
    	pageVO= userService.findPageByUser(user, pageVO);
    	this.initDisp(pageVO.getRows());
    	return pageVO;
    }
    
    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<UserPO> view(UserPO user) {
    	RetResult<UserPO> ret = new RetResult<UserPO>();
        if (user.getCid() != null) {
            user = userService.selectByKey(user.getCid());
        }
        ret.setData(user);
        return ret;
    }
    
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			public void setAsText(String value) {
				try {
					if (value == null || value.trim().equals("")) {  
			            setValue(null);
			        } else if(value.contains(":")) {
			        	setValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value));
					} else {
						setValue(new SimpleDateFormat("yyyy-MM-dd").parse(value));
					}
				} catch (java.text.ParseException e) {
					setValue(null);
				}
			}
		});
	}
    /**
     * save or update
     * @param user
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="user.save",author="csh",calls="updateNotNull,create")
    public RetResult<Long> save(UserPO user) {
//    	logger.info("------user--cid="+user.getCid()+" code="+user.getCode()+" nameCn="+user.getNameCn());
//        ModelAndView result = new ModelAndView("user");
    	RetResult<Long> ret=new RetResult<Long>();
        if (user.getCid() != null) {
        	this.initUpdate(user);
            userService.updateNotNull(user);
        } else {
        	this.initCreate(user);
            userService.create(user);
        }
        ret.setData(user.getCid());
        return ret;
    }
    
    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="user.delete",author="csh",calls="deleteVirtual")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	Long operUserId=AppContext.getCurrentUserId();
    	RetResult<Integer> ret =this.userService.deleteVirtual(operUserId, id);
        return ret;
    }
    
    @RequestMapping(value = {"name/list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Collection<UserPO> findNameByPage(UserPO user) {
    	logger.info("-------findByPage-------");
    	PageVO<UserPO> pageVO = new PageVO<UserPO>(this.getRequest());
    	pageVO= userService.findPageByUser(user, pageVO);
    	return pageVO.getRows();
    }
    
}
