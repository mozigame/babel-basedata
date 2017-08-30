package com.babel.basedata.controller;

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
import com.babel.basedata.model.UserRolePO;
import com.babel.basedata.service.IUserRoleService;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;

/**
 * @author liuzh_3nofxnp
 * @since 2015-09-19 17:15
 */
@Controller
@RequestMapping("/basedata/userRole")
public class UserRoleController extends WebBaseController {
	private static final Log logger = LogFactory.getLog(UserRoleController.class);

    @Autowired
    private IUserRoleService userRoleService;
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(UserRolePO userRole){
    	ModelAndView result = new ModelAndView("basedata/ey_userRole");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="userRole",author="cjh",calls="findPageByUserRole",  descs=LogAudit.noRet)
    public PageVO<UserRolePO> findByPage(UserRolePO userRole) {
    	logger.info("-------findByPage-------");
    	PageVO<UserRolePO> pageVO = new PageVO<UserRolePO>(this.getRequest());
    	PageVO<UserRolePO> p= userRoleService.findPageByUserRole(userRole, pageVO);
    	this.initDisp(p.getRows());
    	return p;
    }
    

    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<UserRolePO> view(UserRolePO userRole) {
    	RetResult<UserRolePO> ret = new RetResult<UserRolePO>();
    	if(userRole.getCid()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid is empty", null);
    		return ret;
    	}
        
        userRole = userRoleService.selectByKey(userRole.getCid());
        
        ret.setData(userRole);
        return ret;
    }
    

    /**
     * save or update
     * @param userRole
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="userRole.save",author="cjh",calls="update,create",  descs="cid=#{0.cid},code=#{0.code}")
    public RetResult<Long> save(UserRolePO userRole) {
    	logger.info("------userRole--cid="+userRole.getCid()+" userId="+userRole.getUserId()+" jobType="+userRole.getJobType());
//        ModelAndView result = new ModelAndView("userRole");
    	RetResult<Long> ret=new RetResult<Long>();
    	
    	if(userRole.getUserId()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "userId is null", null);
    		return ret;
    	}
    	if(userRole.getRoleId()==null){
    		ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "roleId is null", null);
    		return ret;
    	}
    	
        if (userRole.getCid() != null) {
        	this.initUpdate(userRole);
            userRoleService.update(userRole);
        } else {
        	this.initCreate(userRole);
            userRoleService.create(userRole);
        }
        ret.setData(userRole.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="userRole.delete",author="cjh",calls="deleteVirtual",  descs="cid=#{0}")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	RetResult<Integer> ret = new RetResult<Integer>();
		if(id==null||id==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "id="+id+" is empty", null);
			return ret;
		}
    	Long operUserId=AppContext.getCurrentUserId();
    	try {
			ret =this.userRoleService.deleteVirtual(operUserId, id);
		} catch (Exception e) {
//			ret=new RetResult(e.getRetResult());
		}
        return ret;
    }
    
}
