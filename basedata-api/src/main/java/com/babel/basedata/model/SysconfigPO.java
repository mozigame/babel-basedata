package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_sysconfig")
@JsonInclude(Include.NON_NULL)
public class SysconfigPO extends BaseEntity {
    @Id
//    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_sysconfig_cid_seq')")
    @GeneratedValue(generator = "JDBC")
    @Column(name = "cid")
    private Long cid;

    /**
     * 配置类型，0系统配置，1全局项目配置，2项目配置，3项目用户，4用户配置
0-1没有子表，2，3，4有子表
     */
    @Column(name = "conf_type")
    private Integer confType;

    /**
     * 父id，为0表示根节点
     */
    private Long pid;
    
    /**
     * 是否作为环境参数
     */
    @Column(name = "if_env")
    private Long ifEnv;

    /**
     * 系统参数值
     */
    private String value;

    /**
     * 默认值
     */
    @Column(name = "value_default")
    private String valueDefault;

    /**
     * 自定义json属性值
     */
    @Column(name = "value_json")
    private String valueJson;

    /**
     * 预留参数值1
     */
    private String value1;

    /**
     * 预留参数值2
     */
    private String value2;


    /**
     * 备注
     */
    private String remark;
    @Column(name = "can_modify")
    private Integer canModify;
    @Column(name = "order_count")
    private Integer orderCount;
    
    @Transient
    private String parentName;
    
    public void loadUserConfig(SysconfigUserPO sysUser){
    	this.value=sysUser.getValue();
    	this.value1=sysUser.getValue1();
    	this.value2=sysUser.getValue2();
    	this.valueJson=sysUser.getValueJson();
    	this.setModifyDate(sysUser.getModifyDate());
    	this.setModifyUser(sysUser.getModifyUser());
    	this.remark=sysUser.getRemark();
    }

    /**
     * @return cid
     */
    public Long getCid() {
        return cid;
    }

    /**
     * @param cid
     */
    public void setCid(Long cid) {
        this.cid = cid;
    }

    /**
     * 获取配置类型，0系统配置，1全局项目配置，2项目配置，3项目用户，4用户配置
0-1没有子表，2，3，4有子表
     *
     * @return conf_type - 配置类型，0系统配置，1全局项目配置，2项目配置，3项目用户，4用户配置
0-1没有子表，2，3，4有子表
     */
    public Integer getConfType() {
        return confType;
    }

    /**
     * 设置配置类型，0系统配置，1全局项目配置，2项目配置，3项目用户，4用户配置
0-1没有子表，2，3，4有子表
     *
     * @param confType 配置类型，0系统配置，1全局项目配置，2项目配置，3项目用户，4用户配置
0-1没有子表，2，3，4有子表
     */
    public void setConfType(Integer confType) {
        this.confType = confType;
    }

    /**
     * 获取父id，为0表示根节点
     *
     * @return pid - 父id，为0表示根节点
     */
    public Long getPid() {
        return pid;
    }

    /**
     * 设置父id，为0表示根节点
     *
     * @param pid 父id，为0表示根节点
     */
    public void setPid(Long pid) {
        this.pid = pid;
    }

    /**
     * 获取系统参数值
     *
     * @return value - 系统参数值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置系统参数值
     *
     * @param value 系统参数值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取默认值
     *
     * @return value_default - 默认值
     */
    public String getValueDefault() {
        return valueDefault;
    }

    /**
     * 设置默认值
     *
     * @param valueDefault 默认值
     */
    public void setValueDefault(String valueDefault) {
        this.valueDefault = valueDefault;
    }

    /**
     * 获取自定义json属性值
     *
     * @return value_json - 自定义json属性值
     */
    public String getValueJson() {
        return valueJson;
    }

    /**
     * 设置自定义json属性值
     *
     * @param valueJson 自定义json属性值
     */
    public void setValueJson(String valueJson) {
        this.valueJson = valueJson;
    }

    /**
     * 获取预留参数值1
     *
     * @return value1 - 预留参数值1
     */
    public String getValue1() {
        return value1;
    }

    /**
     * 设置预留参数值1
     *
     * @param value1 预留参数值1
     */
    public void setValue1(String value1) {
        this.value1 = value1;
    }

    /**
     * 获取预留参数值2
     *
     * @return value2 - 预留参数值2
     */
    public String getValue2() {
        return value2;
    }

    /**
     * 设置预留参数值2
     *
     * @param value2 预留参数值2
     */
    public void setValue2(String value2) {
        this.value2 = value2;
    }


    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    

    public Integer getCanModify() {
		return canModify;
	}

	public void setCanModify(Integer canModify) {
		this.canModify = canModify;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}
	
	

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	

	public Long getIfEnv() {
		return ifEnv;
	}

	public void setIfEnv(Long ifEnv) {
		this.ifEnv = ifEnv;
	}

	@Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysconfigPO other = (SysconfigPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getConfType() == null ? other.getConfType() == null : this.getConfType().equals(other.getConfType()))
            && (this.getPid() == null ? other.getPid() == null : this.getPid().equals(other.getPid()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()))
            && (this.getValueDefault() == null ? other.getValueDefault() == null : this.getValueDefault().equals(other.getValueDefault()))
            && (this.getValueJson() == null ? other.getValueJson() == null : this.getValueJson().equals(other.getValueJson()))
            && (this.getValue1() == null ? other.getValue1() == null : this.getValue1().equals(other.getValue1()))
            && (this.getValue2() == null ? other.getValue2() == null : this.getValue2().equals(other.getValue2()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getConfType() == null) ? 0 : getConfType().hashCode());
        result = prime * result + ((getPid() == null) ? 0 : getPid().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        result = prime * result + ((getValueDefault() == null) ? 0 : getValueDefault().hashCode());
        result = prime * result + ((getValueJson() == null) ? 0 : getValueJson().hashCode());
        result = prime * result + ((getValue1() == null) ? 0 : getValue1().hashCode());
        result = prime * result + ((getValue2() == null) ? 0 : getValue2().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        return result;
    }
}