package com.babel.basedata.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntitySimple;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_product_cdkey")
@XmlRootElement(name="productCdkey")
@JsonInclude(Include.NON_NULL)
public class ProductCdkeyPO extends BaseEntitySimple {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * cid
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_productCdkey_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
	/**
     * 产品聚道编码
     */
    @Column(name = "appid")
    private String appid;
    
	/**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;
    
	/**
     * 相关数据
     */
    @Column(name = "data")
    private String data;
    
	/**
     * 兑换码
     */
    @Column(name = "cdkey")
    private String cdkey;
    
    /**
     * 兑换码价格
     */
    @Column(name = "price")
    private Integer price;
    
	/**
     * 兑换码类型
     */
    @Column(name = "cdkey_type")
    private Integer cdkeyType;
    @Transient
    private String qrCodeUrl;
    @Transient
    private String qrCode;
    
	/**
     * 到期时间
     */
    @Column(name = "due_date")
    private Date dueDate;
    
	/**
     * 使用时间
     */
    @Column(name = "use_date")
    private Date useDate;
    
	/**
     * 是否已用
     */
    @Column(name = "if_use")
    private Integer ifUse;
    
    private Integer status;
    
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
     * 获取产品聚道编码
     *
     * @return appid - 产品聚道编码
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
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
     * 获取订单号
     *
     * @return orderNo - 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

   	/**
     * 设置订单号
     *
     * @param orderNo 订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
	
    /**
     * 获取相关数据
     *
     * @return data - 相关数据
     */
    public String getData() {
        return data;
    }

   	/**
     * 设置相关数据
     *
     * @param data 相关数据
     */
    public void setData(String data) {
        this.data = data;
    }
	
    /**
     * 获取兑换码
     *
     * @return cdkey - 兑换码
     */
    public String getCdkey() {
        return cdkey;
    }

   	/**
     * 设置兑换码
     *
     * @param cdkey 兑换码
     */
    public void setCdkey(String cdkey) {
        this.cdkey = cdkey;
    }
	
    /**
     * 获取兑换码类型
     *
     * @return cdkeyType - 兑换码类型
     */
    public Integer getCdkeyType() {
        return cdkeyType;
    }

   	/**
     * 设置兑换码类型
     *
     * @param cdkeyType 兑换码类型
     */
    public void setCdkeyType(Integer cdkeyType) {
        this.cdkeyType = cdkeyType;
    }
	
    /**
     * 获取到期时间
     *
     * @return dueDate - 到期时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    public Date getDueDate() {
        return dueDate;
    }

   	/**
     * 设置到期时间
     *
     * @param dueDate 到期时间
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
	
    /**
     * 获取使用时间
     *
     * @return useDate - 使用时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    public Date getUseDate() {
        return useDate;
    }

   	/**
     * 设置使用时间
     *
     * @param useDate 使用时间
     */
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
	
    /**
     * 获取是否已用
     *
     * @return ifUser - 是否已用
     */
    public Integer getIfUse() {
        return ifUse;
    }

   	/**
     * 设置是否已用
     *
     * @param ifUser 是否已用
     */
    public void setIfUse(Integer ifUse) {
        this.ifUse = ifUse;
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

	/**
	 * @return the qrCodeUrl
	 */
	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	/**
	 * @param qrCodeUrl the qrCodeUrl to set
	 */
	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	/**
	 * @return the qrCode
	 */
	public String getQrCode() {
		return qrCode;
	}

	/**
	 * @param qrCode the qrCode to set
	 */
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
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
        ProductCdkeyPO other = (ProductCdkeyPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getAppid() == null ? other.getAppid() == null : this.getAppid().equals(other.getAppid()))
            && (this.getOrderNo() == null ? other.getOrderNo() == null : this.getOrderNo().equals(other.getOrderNo()))
            && (this.getData() == null ? other.getData() == null : this.getData().equals(other.getData()))
            && (this.getCdkey() == null ? other.getCdkey() == null : this.getCdkey().equals(other.getCdkey()))
            && (this.getCdkeyType() == null ? other.getCdkeyType() == null : this.getCdkeyType().equals(other.getCdkeyType()))
            && (this.getDueDate() == null ? other.getDueDate() == null : this.getDueDate().equals(other.getDueDate()))
            && (this.getUseDate() == null ? other.getUseDate() == null : this.getUseDate().equals(other.getUseDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfUse() == null ? other.getIfUse() == null : this.getIfUse().equals(other.getIfUse()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        	result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        	result = prime * result + ((getAppid() == null) ? 0 : getAppid().hashCode());
        	result = prime * result + ((getOrderNo() == null) ? 0 : getOrderNo().hashCode());
        	result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
        	result = prime * result + ((getCdkey() == null) ? 0 : getCdkey().hashCode());
        	result = prime * result + ((getCdkeyType() == null) ? 0 : getCdkeyType().hashCode());
        	result = prime * result + ((getDueDate() == null) ? 0 : getDueDate().hashCode());
        	result = prime * result + ((getUseDate() == null) ? 0 : getUseDate().hashCode());
        	result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        	result = prime * result + ((getIfUse() == null) ? 0 : getIfUse().hashCode());
        	result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        	result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        return result;
    }
}