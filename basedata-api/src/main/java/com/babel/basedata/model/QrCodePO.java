package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_qr_code")
@XmlRootElement(name="qrCode")
@JsonInclude(Include.NON_NULL)
public class QrCodePO extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_qrCode_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
	/**
     * 
     */
    @Column(name = "sys_type")
    private String sysType;
    
	/**
     * 1微信推广
2推广二维码
3其他二维码

     */
    @Column(name = "qr_type")
    private Integer qrType;
    
	/**
     * 1长久
2临时
     */
    @Column(name = "aging_type")
    private Integer agingType;
    
	/**
     * 标题
     */
    @Column(name = "title")
    private String title;
    
	/**
     * 二维码数据
     */
    @Column(name = "data")
    private String data;
    
	/**
     * 聚道id
     */
    @Column(name = "partner_id")
    private Long partnerId;
    
	/**
     * 
     */
    @Column(name = "ticket")
    private String ticket;
    
	/**
     * url地址
     */
    @Column(name = "url")
    private String url;
    
	/**
     * 
     */
    @Column(name = "remark")
    private String remark;
    
    /**
     * 商户机构名称
     */
    @Transient
    private String partnerName;
    
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
     * 获取
     *
     * @return sysType - 
     */
    public String getSysType() {
        return sysType;
    }

   	/**
     * 设置
     *
     * @param sysType 
     */
    public void setSysType(String sysType) {
        this.sysType = sysType;
    }
	
    /**
     * 获取1微信推广
2推广二维码
3其他二维码

     *
     * @return qrType - 1微信推广
2推广二维码
3其他二维码

     */
    public Integer getQrType() {
        return qrType;
    }

   	/**
     * 设置1微信推广
2推广二维码
3其他二维码

     *
     * @param qrType 1微信推广
2推广二维码
3其他二维码

     */
    public void setQrType(Integer qrType) {
        this.qrType = qrType;
    }
	
    /**
     * 获取1长久
2临时
     *
     * @return agingType - 1长久
2临时
     */
    public Integer getAgingType() {
        return agingType;
    }

   	/**
     * 设置1长久
2临时
     *
     * @param agingType 1长久
2临时
     */
    public void setAgingType(Integer agingType) {
        this.agingType = agingType;
    }
	
    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

   	/**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }
	
    /**
     * 获取二维码数据
     *
     * @return data - 二维码数据
     */
    public String getData() {
        return data;
    }

   	/**
     * 设置二维码数据
     *
     * @param data 二维码数据
     */
    public void setData(String data) {
        this.data = data;
    }
	
    /**
     * 获取聚道id
     *
     * @return partnerId - 聚道id
     */
    public Long getPartnerId() {
        return partnerId;
    }

   	/**
     * 设置聚道id
     *
     * @param partnerId 聚道id
     */
    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }
	
    /**
     * 获取
     *
     * @return ticket - 
     */
    public String getTicket() {
        return ticket;
    }

   	/**
     * 设置
     *
     * @param ticket 
     */
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
	
    /**
     * 获取url地址
     *
     * @return url - url地址
     */
    public String getUrl() {
        return url;
    }

   	/**
     * 设置url地址
     *
     * @param url url地址
     */
    public void setUrl(String url) {
        this.url = url;
    }
	
    /**
     * 获取
     *
     * @return remark - 
     */
    public String getRemark() {
        return remark;
    }

   	/**
     * 设置
     *
     * @param remark 
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    
	
    

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
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
        QrCodePO other = (QrCodePO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getSysType() == null ? other.getSysType() == null : this.getSysType().equals(other.getSysType()))
            && (this.getQrType() == null ? other.getQrType() == null : this.getQrType().equals(other.getQrType()))
            && (this.getAgingType() == null ? other.getAgingType() == null : this.getAgingType().equals(other.getAgingType()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getData() == null ? other.getData() == null : this.getData().equals(other.getData()))
            && (this.getPartnerId() == null ? other.getPartnerId() == null : this.getPartnerId().equals(other.getPartnerId()))
            && (this.getTicket() == null ? other.getTicket() == null : this.getTicket().equals(other.getTicket()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        	result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        	result = prime * result + ((getSysType() == null) ? 0 : getSysType().hashCode());
        	result = prime * result + ((getQrType() == null) ? 0 : getQrType().hashCode());
        	result = prime * result + ((getAgingType() == null) ? 0 : getAgingType().hashCode());
        	result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        	result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
        	result = prime * result + ((getPartnerId() == null) ? 0 : getPartnerId().hashCode());
        	result = prime * result + ((getTicket() == null) ? 0 : getTicket().hashCode());
        	result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        	result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        	result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        	result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        	result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        	result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        	result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        	result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}