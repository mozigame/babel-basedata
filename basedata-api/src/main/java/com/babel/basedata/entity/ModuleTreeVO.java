package com.babel.basedata.entity;



import com.babel.basedata.model.ModulePO;

public class ModuleTreeVO extends ModulePO{
	
	private boolean checked;
	
	public void load(ModulePO m){
		this.setCid(m.getCid());
		this.setAppType(m.getAppType());
		this.setName(m.getName());
		this.setCode(m.getCode());
		this.setColor(m.getCode());
		this.setIfDel(m.getIfDel());
		this.setStatus(m.getStatus());
		this.setShowType(m.getShowType());
		this.setUrl(m.getUrl());
		this.setParentId(m.getParentId());
		this.setMtype(m.getMtype());
		this.setIcon(m.getIcon());
		this.setCss(m.getCss());
		this.setOrderCount(m.getOrderCount());
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
}
