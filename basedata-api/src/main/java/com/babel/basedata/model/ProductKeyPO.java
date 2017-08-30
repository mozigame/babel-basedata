package com.babel.basedata.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_product_key")
@XmlRootElement(name="productKey")
@JsonInclude(Include.NON_NULL)
public class ProductKeyPO extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * cid
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_productKey_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
	/**
     * 产品id
     */
    @Column(name = "appid")
    private String appid;
    
	/**
     * 密钥
     */
    @Column(name = "secret_key")
    private String secretKey;
    
    @Column(name = "cdkey_type")
    private String cdkeyType;
    
    private Integer price;
    
	/**
     * 开始时间
     */
    @Column(name = "begin_date")
    private Date beginDate;
    
	/**
     * 结束时间
     */
    @Column(name = "end_date")
    private Date endDate;
    
	/**
     * 白名单
     */
    @Column(name = "whitelist")
    private String whitelist;
    
	/**
     * 接口角色
     */
    @Column(name = "intf_role_id")
    private Long intfRoleId;
    
	/**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
    
    /**
     * 获取cid
     *
     * @return cid - cid
     */
    public Long getCid() {
        return cid;
    }

   	/**
     * 设置cid
     *
     * @param cid cid
     */
    public void setCid(Long cid) {
        this.cid = cid;
    }
	
    
	
    

	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @param appid the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/**
     * 获取密钥
     *
     * @return secretKey - 密钥
     */
    public String getSecretKey() {
        return secretKey;
    }

   	/**
     * 设置密钥
     *
     * @param secretKey 密钥
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
	
    
    /**
	 * @return the cdkeyType
	 */
	public String getCdkeyType() {
		return cdkeyType;
	}

	/**
	 * @param cdkeyType the cdkeyType to set
	 */
	public void setCdkeyType(String cdkeyType) {
		this.cdkeyType = cdkeyType;
	}

	/**
     * 获取开始时间
     *
     * @return beginDate - 开始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    public Date getBeginDate() {
        return beginDate;
    }

   	/**
     * 设置开始时间
     *
     * @param beginDate 开始时间
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
	
    /**
     * 获取结束时间
     *
     * @return endDate - 结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    public Date getEndDate() {
        return endDate;
    }

   	/**
     * 设置结束时间
     *
     * @param endDate 结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
	
    /**
     * 获取白名单
     *
     * @return whitelist - 白名单
     */
    public String getWhitelist() {
        return whitelist;
    }

   	/**
     * 设置白名单
     *
     * @param whitelist 白名单
     */
    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }
	
    /**
     * 获取接口角色
     *
     * @return intfRoleId - 接口角色
     */
    public Long getIntfRoleId() {
        return intfRoleId;
    }

   	/**
     * 设置接口角色
     *
     * @param intfRoleId 接口角色
     */
    public void setIntfRoleId(Long intfRoleId) {
        this.intfRoleId = intfRoleId;
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
    
    
	
    /**
	 * @return the price
	 */
	public Integer getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Integer price) {
		this.price = price;
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
        ProductKeyPO other = (ProductKeyPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getAppid() == null ? other.getAppid() == null : this.getAppid().equals(other.getAppid()))
            && (this.getSecretKey() == null ? other.getSecretKey() == null : this.getSecretKey().equals(other.getSecretKey()))
            && (this.getBeginDate() == null ? other.getBeginDate() == null : this.getBeginDate().equals(other.getBeginDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getWhitelist() == null ? other.getWhitelist() == null : this.getWhitelist().equals(other.getWhitelist()))
            && (this.getIntfRoleId() == null ? other.getIntfRoleId() == null : this.getIntfRoleId().equals(other.getIntfRoleId()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        	result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        	result = prime * result + ((getAppid() == null) ? 0 : getAppid().hashCode());
        	result = prime * result + ((getSecretKey() == null) ? 0 : getSecretKey().hashCode());
        	result = prime * result + ((getBeginDate() == null) ? 0 : getBeginDate().hashCode());
        	result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        	result = prime * result + ((getWhitelist() == null) ? 0 : getWhitelist().hashCode());
        	result = prime * result + ((getIntfRoleId() == null) ? 0 : getIntfRoleId().hashCode());
        	result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        	result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        	result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        	result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        	result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        	result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        	result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}