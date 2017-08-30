package com.babel.basedata.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.impl.BaseService;
import com.babel.common.core.util.RedisUtil;
import com.babel.basedata.mapper.ShortLinkMapper;
import com.babel.basedata.model.ShortLinkPO;
import com.babel.basedata.service.IShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

@Service("shortLinkService")
public class ShortLinkServiceImpl extends BaseService<ShortLinkPO> implements IShortLinkService{
	private static final Logger logger = Logger.getLogger(ShortLinkServiceImpl.class);
	
	@Autowired
	private ShortLinkMapper shortLinkMapper;
	
	 @Override
		public ShortLinkMapper getMapper() {
			return shortLinkMapper;
		}
	
	@Override
	public ShortLinkPO findShortLinkById(Long id) {
		logger.info("----ShortLinkPO findShortLinkById----id = "+id);
		//SqlHelper.addIgnore(ShortLinkPO.class, "name");
		return shortLinkMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<ShortLinkPO> findShortLinkByIds(List<Long> ids) {
		logger.info("----findShortLinkByIds----");
		Example example = new Example(ShortLinkPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("cid", ids);
		return shortLinkMapper.selectByExample(example);
	}
	
	@Override
    public List<ShortLinkPO> selectByShortLink(ShortLinkPO shortLink) {
		logger.info("----selectByShortLink----shortLink = "+shortLink);
		if(shortLink == null) {
			shortLink = new ShortLinkPO();
		}
		shortLink.setIfDel(0);
        return shortLinkMapper.select(shortLink);
    }
	
	public PageVO<ShortLinkPO> findPageByShortLink(ShortLinkPO shortLink, PageVO<ShortLinkPO> page) {
		logger.info("----findPageByShortLink----shortLink = "+shortLink);
		Example example = new Example(ShortLinkPO.class);
		Example.Criteria criteria = example.createCriteria();
		if (shortLink.getShortType() != null) {
			criteria.andLike("shortType", "%" + shortLink.getShortType() + "%");
		}
		if (shortLink.getInfoType() != null) {
			criteria.andLike("infoType", "%" + shortLink.getInfoType() + "%");
		}
		if (shortLink.getCode() != null) {
			criteria.andLike("code", "%" + shortLink.getCode() + "%");
		}
		if (shortLink.getIfDel() != null) {
			criteria.andLike("ifDel", "%" + shortLink.getIfDel() + "%");
		}
		String orderClause = page.getOrderClause();
		if(StringUtil.isNotEmpty(orderClause)) {
			example.setOrderByClause(orderClause);
		}
		return selectPageByExample(example, page);
	}
	
	public PageVO<ShortLinkPO> findPageByMapperXML(ShortLinkPO shortLink, PageVO<ShortLinkPO> page) {
		logger.info("----findPageByMapperXML----");
		List<ShortLinkPO> list=null;
		int totalSize=0;
		try {
			totalSize = shortLinkMapper.findShortLinkListByPageCount(shortLink);
			list = shortLinkMapper.findShortLinkListByPage(shortLink, page);
		} catch (Exception e) {
			logger.error("-----findShortLinkListByPage--", e);
		}
		PageVO<ShortLinkPO> pageRet=new PageVO<ShortLinkPO>(list, totalSize, page.getPageSize(), page.getCurrentPage());
		return pageRet;
	}
	
//	@LogService(title="ShortLinkServiceImpl.create",author="Jinhe.chen",calls="insert")
	public RetResult<Long> create(ShortLinkPO shortLink){
		logger.info("----create----shortLink = "+shortLink);
		RetResult<Long> ret = new RetResult<Long>();
		shortLink.initCreate();
		save(shortLink);
		ret.setData(shortLink.getCid());
		return ret;
	}
	
	public List<ShortLinkPO> findShortLinkList(int shortType, Date startDate, Date endDate){
		Example example = new Example(ShortLinkPO.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("shortType", shortType);
		criteria.andEqualTo("ifDel", 0);
		if (startDate != null) {
			criteria.andGreaterThanOrEqualTo("createDate", startDate);
		}
		if (endDate != null) {
			criteria.andLessThan("createDate", startDate);
		}
		return this.shortLinkMapper.selectByExample(example);
	}
	
	public void loadLinkRedis(int shortType, Date startDate, Date endDate){
		String redisKey="K_SHORT_LINK_"+shortType;
		List<ShortLinkPO> list=this.findShortLinkList(shortType, startDate, endDate);
		for(ShortLinkPO sLink:list){
			RedisUtil.putRedis(redisKey, sLink.getCode(), sLink.getData());
		}
		RedisUtil.expire(redisKey, 3600*24);
	}
	
	/**
	 * 载入redis数据
	 */
	public void loadLinkRedisUrl(){
		int shortType=1;
		Date startDate=DateUtils.addDays(new Date(), -2);
		Date endDate=new Date();
		this.loadLinkRedis(shortType, startDate, endDate);
	}
	
	public String getShortLinkUrl(String shortCode){
		int shortType=1;
		String data=RedisUtil.getShortUrl(shortType, shortCode);
		if(data==null){
			data=this.getShortLink(shortType, shortCode);
			if(data!=null){
				String redisKey="K_SHORT_LINK_"+shortType;
				RedisUtil.putRedis(redisKey, shortCode, data);;
			}
		}
		return data;
		
		
	}
	
	public String getShortLink(int shortType, String shortCode){
		ShortLinkPO shortLink=new ShortLinkPO();
		shortLink.setShortType(shortType);
		shortLink.setCode(shortCode);
		shortLink.setIfDel(0);
		List<ShortLinkPO> list=this.shortLinkMapper.select(shortLink);
		if(list.size()>0){
			return list.get(0).getData();
		}
		return null;
	}
	
	
//	@LogService(title="ShortLinkServiceImpl.update",author="Jinhe.chen",calls="updateByPrimaryKey",descs="cid=#{0.cid}")
	public RetResult<Long> update(ShortLinkPO shortLink){
		logger.info("----update----shortLink = "+shortLink);
		RetResult<Long> ret = new RetResult<Long>();
		if(shortLink.getCid()==null || shortLink.getCid().longValue()==0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+shortLink.getCid()+" is empty", null);
			return ret;
		}
		shortLink.initUpdate();
		shortLink.setIfDel(0);
		shortLinkMapper.updateByPrimaryKey(shortLink);
		ret.setData(shortLink.getCid());
		return ret;
	}
	
//	@LogService(title="ShortLinkServiceImpl.update",author="Jinhe.chen",calls="deleteVirtual",descs="cid=#{0.cid}")
	public RetResult<Integer> deleteVirtual(Long cid) throws RetException{
		logger.info("----deleteVirtual----cid = "+cid);
		RetResult<Integer> ret = new RetResult<Integer>();
		if(cid == null || cid == 0){
			ret.initError(RetResult.msg_codes.ERR_DATA_INPUT, "cid = "+cid+" is empty", null);
			return ret;
		}
		ShortLinkPO shortLink = new ShortLinkPO();
		shortLink.setCid(cid);
		int v = 0;
		try {
			v = this.deleteVirtual(shortLink);
		} catch (Exception e) {
			ret.initError(RetResult.msg_codes.ERR_UNKNOWN, "cid = "+cid+",error = "+e.getMessage(), e);
			logger.error("-----deleteVirtual----", e);
			throw new RetException(ret);
		}
		ret.setData(v);
		return ret;
	}

}
