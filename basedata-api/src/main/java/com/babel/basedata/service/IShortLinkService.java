package com.babel.basedata.service;

import java.util.List;

import com.babel.basedata.model.ShortLinkPO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface IShortLinkService  extends IBaseService<ShortLinkPO> {
	public List<ShortLinkPO> selectByShortLink(ShortLinkPO shortLink);
	 
	public ShortLinkPO findShortLinkById(Long id);
	
	public List<ShortLinkPO> findShortLinkByIds(List<Long> ids);
	
	public PageVO<ShortLinkPO> findPageByShortLink(ShortLinkPO shortLink, PageVO<ShortLinkPO> page);
	
	public PageVO<ShortLinkPO> findPageByMapperXML(ShortLinkPO shortLink, PageVO<ShortLinkPO> page);
	
	public RetResult<Long> update(ShortLinkPO record);
	
	public RetResult<Long> create(ShortLinkPO record);
	
	public RetResult<Integer> deleteVirtual(Long cid) throws RetException;
	
	public String getShortLinkUrl(String shortCode);
	
	public String getShortLink(int shortType, String shortCode);
	
	public void loadLinkRedisUrl();
	
}
