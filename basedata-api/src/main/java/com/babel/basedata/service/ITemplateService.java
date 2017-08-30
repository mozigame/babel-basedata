package com.babel.basedata.service;

import java.util.List;
import java.util.Map;

import com.babel.basedata.model.LogMsgPO;
import com.babel.basedata.model.TemplatePO;
import com.babel.common.core.data.RetResult;
import com.babel.common.core.exception.RetException;
import com.babel.common.core.page.PageVO;
import com.babel.common.core.service.IBaseService;

public interface ITemplateService  extends IBaseService<TemplatePO> {
	public List<TemplatePO> selectByTemplate(TemplatePO template);
	 
	public TemplatePO findTemplateById(Long id);
	
	public TemplatePO findTemplateByTplId(String sysType, String tplId);
	
	public List<TemplatePO> findTemplateByIds(List<Long> ids);
	
	public PageVO<TemplatePO> findPageByTemplate(TemplatePO template, PageVO<TemplatePO> page);
	
	/**
	 * 按模板发送数据
	 * 发送的数据可以为空：
	 * 	a,为空时自动取tplExample的样例数据
	 *  b,不为空时仍会取tplExample数据，但会用paramMap去覆盖tplExample的数据
	 * @param tplId
	 * @param tos
	 * @param paramMap
	 * @return
	 */
	public RetResult<Long> sendTplMsg(String sysType, String code, String tos, LogMsgPO logMsgPO, Map<String, String> paramMap);
	
	/**
	 * 
	 * @param template
	 * @param tos
	 * @param paramMap 数据为空时，自动取tplExample样例的数据
	 * @return
	 */
	public RetResult<Long> sendMsg(TemplatePO template, String tos, LogMsgPO logMsg, Map<String, String> paramMap);
	
	public RetResult<Long> update(TemplatePO record);
	
	public RetResult<Long> create(TemplatePO record);
	
	public RetResult<Integer> deleteVirtual(Long operUserId, Long cid) throws RetException;
	
	public RetResult<String> importWxTemplate(String sysType,Long operUserId);
	
	
}
