package com.babel.basedata.mapper;

import com.babel.basedata.model.ModelPO;

import tk.mybatis.mapper.common.MapperMy;

public interface ModelMapper extends MapperMy<ModelPO> {
	public void insertModel(ModelPO model);
}