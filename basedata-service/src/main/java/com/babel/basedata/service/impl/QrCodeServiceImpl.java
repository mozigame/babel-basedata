package com.babel.basedata.service.impl;

import java.util.List;

import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.basedata.logger.LogService;
import com.babel.basedata.mapper.QrCodeMapper;
import com.babel.basedata.model.QrCodePO;
import com.babel.basedata.service.IQrCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("qrCodeService")
public class QrCodeServiceImpl extends BaseService<QrCodePO> implements IQrCodeService{
	private static final Log logger = LogFactory.getLog(QrCodeServiceImpl.class);
	
	@Autowired
	private QrCodeMapper qrCodeMapper;
	
	 @Override
		public QrCodeMapper getMapper() {
			return qrCodeMapper;
		}
	
	@Override
	public QrCodePO findQrCodeById(Long id) {
		logger.info("----QrCodePO findQrCodeById----id = "+id);
		//SqlHelper.addIgnore(QrCodePO.class, "name");
		return qrCodeMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<QrCodePO> findQrCodeByIds(List<Long> ids) {
		logger.info("----findQrCodeByIds----");
		Example example = new Example(QrCodePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return qrCodeMapper.selectByExample(example);
	}
	
	@Override
    public List<QrCodePO> selectByQrCode(QrCodePO qrCode) {
		logger.info("----selectByQrCode----qrCode = "+qrCode);
		if(qrCode == null) {
			qrCode = new QrCodePO();
		}
		qrCode.setStatus(1);
		qrCode.setIfDel(0);
        return qrCodeMapper.select(qrCode);
    }
	
	public PageVO<QrCodePO> findPageByQrCode(QrCodePO qrCode, PageVO<QrCodePO> page) {
		logger.info("----findPageByQrCode----qrCode = "+qrCode);
		Example example = new Example(QrCodePO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("ifDel", 0);
  		if (qrCode.getSysType() != null) {
			criteria.andEqualTo("sysType", qrCode.getSysType());
		}
  		if (qrCode.getQrType() != null) {
			criteria.andEqualTo("qrType", qrCode.getQrType());
		}
  		if (qrCode.getAgingType() != null) {
			criteria.andEqualTo("agingType", qrCode.getAgingType());
		}
  		if (qrCode.getTitle() != null) {
			criteria.andEqualTo("title", qrCode.getTitle());
		}
  		if (qrCode.getStatus() != null) {
			criteria.andEqualTo("status", qrCode.getStatus());
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}
	
	public PageVO<QrCodePO> findPageByMapperXML(QrCodePO qrCode, PageVO<QrCodePO> page) {
		logger.info("----findPageByMapperXML----");
		List<QrCodePO> list=null;
		int totalSize=0;
		try {
			totalSize = qrCodeMapper.findQrCodeListByPageCount(qrCode);
			list = qrCodeMapper.findQrCodeListByPage(qrCode, page);
		} catch (Exception e) {
			logger.error("-----findQrCodeListByPage--", e);
		}
		PageVO<QrCodePO> pageRet=new PageVO<QrCodePO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
		return pageRet;
	}
	
	@LogService(title="QrCodeServiceImpl.create",author="Jinhe.chen",calls="insert")
	public RetResult<Long> create(QrCodePO qrCode){
		logger.info("----create----qrCode = "+qrCode);
		RetResult<Long> ret = new RetResult<Long>();
		qrCode.initCreate();
		save(qrCode);
		ret.setData(qrCode.getCid());
		return ret;
	}
	
	@LogService(title="QrCodeServiceImpl.update",author="Jinhe.chen",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(QrCodePO qrCode){
		logger.info("----update----qrCode = "+qrCode);
		RetResult<Long> ret = new RetResult<Long>();
		if(qrCode.getCid()==null || qrCode.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+qrCode.getCid()+" is empty", null);
			return ret;
		}
		qrCode.initUpdate();
		qrCode.setIfDel(0);
		qrCodeMapper.updateByPrimaryKey(qrCode);
		ret.setData(qrCode.getCid());
		return ret;
	}
	
	@LogService(title="QrCodeServiceImpl.update",author="Jinhe.chen",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		QrCodePO qrCode = new QrCodePO();
		qrCode.setCid(cid);
		qrCode.setModifyUser(operUserId);
		int v = 0;
		try {
			v = this.deleteVirtual(qrCode);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
