package com.babel.basedata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.babel.basedata.model.RetryRuleDetailPO;

import tk.mybatis.mapper.common.MapperMy;

public interface RetryRuleDetailMapper extends MapperMy<RetryRuleDetailPO> {
//	List<RetryRuleDetailPO> RetryRuleDetailListByCode(@Param("ruleCodeList") List<String> lookupCodeList);
}