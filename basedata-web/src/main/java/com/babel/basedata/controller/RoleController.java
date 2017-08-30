package com.babel.basedata.controller;

import java.util.Collection;
import java.util.List;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.logger.LogAudit;
import com.babel.common.core.page.PageVO;
import com.babel.common.web.context.AppContext;
import com.babel.common.web.controller.WebBaseController;
import com.babel.basedata.model.RolePO;
import com.babel.basedata.service.IRoleService;
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
@RequestMapping("/basedata/role")
public class RoleController extends WebBaseController{
	private static final Log logger = LogFactory.getLog(RoleController.class);

    @Autowired
    private IRoleService roleService;
    
    @RequestMapping(value = {"index", "index.html", ""})
    public ModelAndView index(RolePO role){
    	ModelAndView result = new ModelAndView("basedata/ey_role");
    	result.addObject("env", super.getEnvMap());
    	return result;
    }
    

    @RequestMapping(value = {"list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PageVO<RolePO> findByPage(RolePO role) {
    	logger.info("-------findByPage-------");
    	PageVO<RolePO> pageVO = new PageVO<RolePO>(this.getRequest());
    	return roleService.findPageByRole(role, pageVO);
    }
    
    
    @RequestMapping(value = {"list2"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ModelAndView findByPage2(RolePO role,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false, defaultValue = "10") int rows) {
//    	System.out.println("-------getList-------");
        ModelAndView result = new ModelAndView();
        List<RolePO> roleList = roleService.selectByRole(role, page, rows);
        result.addObject("pageInfo", new PageInfo<RolePO>(roleList));
        result.addObject("queryParam", role);
        result.addObject("page", page);
        result.addObject("rows", rows);
        return result;

    }


    @RequestMapping(value = "view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public RetResult<RolePO> view(RolePO role) {
    	RetResult<RolePO> ret = new RetResult<RolePO>();
        if (role.getCid() != null) {
            role = roleService.selectByKey(role.getCid());
        }
        ret.setData(role);
        return ret;
    }
    

    /**
     * save or update
     * @param role
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="role.save",author="cjh",calls="update,create")
    public RetResult<Long> save(RolePO role) {
    	logger.info("------save--cid="+role.getCid()+" code="+role.getCode());
    	
    	RetResult<Long> ret=new RetResult<Long>();
        if (role.getCid() != null) {
        	 this.initUpdate(role);
            roleService.update(role);
        } else {
        	this.initCreate(role);
            roleService.create(role);
        }
        ret.setData(role.getCid());
        return ret;
    }

    @RequestMapping(value = {"delete"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    @LogAudit(title="role.delete",author="cjh",calls="deleteVirtual")
    public RetResult<Integer> delete(@RequestParam("id") Long id) {
    	Long operUserId=AppContext.getCurrentUserId();
    	RetResult<Integer> ret =this.roleService.deleteVirtual(operUserId, id);
        return ret;
    }
    
    
    
    @RequestMapping(value = {"name/list"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Collection<RolePO> findNameByPage(RolePO role) {
    	logger.info("-------findNameByPage-------");
    	PageVO<RolePO> pageVO = new PageVO<RolePO>(this.getRequest());
    	pageVO= roleService.findPageByRole(role, pageVO);
    	return pageVO.getRows();
    }
}
