package com.babel.basedata.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.PassiveReplyMapper;
import com.babel.basedata.model.PassiveReplyPO;
import com.babel.basedata.service.IPassiveReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("passiveReplyService")
public class PassiveReplyServiceImpl extends BaseService<PassiveReplyPO> implements IPassiveReplyService{
	private static final Log logger = LogFactory.getLog(PassiveReplyServiceImpl.class);
	
	@Autowired
	private PassiveReplyMapper passiveReplyMapper;
	
	 @Override
		public PassiveReplyMapper getMapper() {
			return passiveReplyMapper;
		}
	
	@Override
	public PassiveReplyPO findPassiveReplyById(Long id) {
		logger.info("----PassiveReplyPO findPassiveReplyById----id = "+id);
		//SqlHelper.addIgnore(PassiveReplyPO.class, "name");
		return passiveReplyMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<PassiveReplyPO> findPassiveReplyByIds(List<Long> ids) {
		logger.info("----findPassiveReplyByIds----");
		Example example = new Example(PassiveReplyPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return passiveReplyMapper.selectByExample(example);
	}
	
	@Override
    public List<PassiveReplyPO> selectByPassiveReply(PassiveReplyPO passiveReply) {
		logger.info("----selectByPassiveReply----passiveReply = "+passiveReply);
		if(passiveReply == null) {
			passiveReply = new PassiveReplyPO();
		}
        return passiveReplyMapper.select(passiveReply);
    }
	
	public PageVO<PassiveReplyPO> findPageByPassiveReply(PassiveReplyPO passiveReply, PageVO<PassiveReplyPO> page) {
		logger.info("----findPageByPassiveReply----passiveReply = "+passiveReply);
		Example example = new Example(PassiveReplyPO.class);
		Example.Criteria criteria = example.createCriteria();
  		if (!StringUtils.isEmpty(passiveReply.getSysType())) {
			criteria.andEqualTo("sysType", passiveReply.getSysType());
		}
  		if (passiveReply.getMsgType() != null) {
			criteria.andEqualTo("msgType", passiveReply.getMsgType());
		}
  		if (passiveReply.getCode() != null) {
			criteria.andEqualTo("code", passiveReply.getCode());
		}
  		if (passiveReply.getTitle() != null) {
			criteria.andEqualTo("title", passiveReply.getTitle());
		}
  		if (passiveReply.getStatus() != null) {
			criteria.andEqualTo("status", passiveReply.getStatus());
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}

	
	@LogService(title="PassiveReplyServiceImpl.create",author="Jinhe.chen",calls="insert")
	public RetResult<Long> create(PassiveReplyPO passiveReply){
		logger.info("----create----passiveReply = "+passiveReply);
		RetResult<Long> ret = new RetResult<Long>();
		save(passiveReply);
		ret.setData(passiveReply.getCid());
		return ret;
	}
	
	@LogService(title="PassiveReplyServiceImpl.update",author="Jinhe.chen",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(PassiveReplyPO passiveReply){
		logger.info("----update----passiveReply = "+passiveReply);
		RetResult<Long> ret = new RetResult<Long>();
		if(passiveReply.getCid()==null || passiveReply.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+passiveReply.getCid()+" is empty", null);
			return ret;
		}
		passiveReplyMapper.updateByPrimaryKey(passiveReply);
		ret.setData(passiveReply.getCid());
		return ret;
	}
	
	@LogService(title="PassiveReplyServiceImpl.update",author="Jinhe.chen",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		PassiveReplyPO passiveReply = new PassiveReplyPO();
		passiveReply.setCid(cid);
		passiveReply.setModifyUser(operUserId);
		int v = 0;
		try {
			v = this.deleteVirtual(passiveReply);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
