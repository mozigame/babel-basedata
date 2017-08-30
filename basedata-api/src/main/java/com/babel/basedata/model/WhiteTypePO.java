package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_white_type")
@XmlRootElement(name="whiteType")
@JsonInclude(Include.NON_NULL)
public class WhiteTypePO extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_whiteType_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
	/**
     * 1为白名单，2为黑名单
     */
    @Column(name = "type")
    private Integer type;
    
	/**
     * 数据类型：1 IP地址，2邮箱，3，用户账号，4，手机号
     */
    @Column(name = "data_type")
    private Integer dataType;
    
	/**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    
    /**
     * 获取
     *
     * @return cid - 
     */
    public Long getCid() {
        return cid;
    }

   	/**
     * 设置
     *
     * @param cid 
     */
    public void setCid(Long cid) {
        this.cid = cid;
    }
	
    /**
     * 获取1为白名单，2为黑名单
     *
     * @return type - 1为白名单，2为黑名单
     */
    public Integer getType() {
        return type;
    }

   	/**
     * 设置1为白名单，2为黑名单
     *
     * @param type 1为白名单，2为黑名单
     */
    public void setType(Integer type) {
        this.type = type;
    }
	
    /**
     * 获取数据类型：1 IP地址，2邮箱，3，用户账号，4，手机号
     *
     * @return dataType - 数据类型：1 IP地址，2邮箱，3，用户账号，4，手机号
     */
    public Integer getDataType() {
        return dataType;
    }

   	/**
     * 设置数据类型：1 IP地址，2邮箱，3，用户账号，4，手机号
     *
     * @param dataType 数据类型：1 IP地址，2邮箱，3，用户账号，4，手机号
     */
    public void setDataType(Integer dataType) {
        this.dataType = dataType;
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
        WhiteTypePO other = (WhiteTypePO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getDataType() == null ? other.getDataType() == null : this.getDataType().equals(other.getDataType()))
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
        	result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        	result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        	result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        	result = prime * result + ((getDataType() == null) ? 0 : getDataType().hashCode());
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