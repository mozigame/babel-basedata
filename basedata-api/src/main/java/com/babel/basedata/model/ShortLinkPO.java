package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntitySimple;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_short_link")
@XmlRootElement(name="shortLink")
@JsonInclude(Include.NON_NULL)
public class ShortLinkPO extends BaseEntitySimple {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_shortLink_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
    @Column(name = "code")
    private String code;
    
	/**
     * 1:url,2数据参数,3json
     */
    @Column(name = "short_type")
    private Integer shortType;
    
	/**
     * 业务信息类型，如btl-pay,btl-refund
     */
    @Column(name = "info_type")
    private String infoType;
    
	/**
     * 数据:长链接，参数，json
     */
    @Column(name = "data")
    private String data;
    
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
     * 获取1:url,2数据参数,3json
     *
     * @return shortType - 1:url,2数据参数,3json
     */
    public Integer getShortType() {
        return shortType;
    }

   	/**
     * 设置1:url,2数据参数,3json
     *
     * @param shortType 1:url,2数据参数,3json
     */
    public void setShortType(Integer shortType) {
        this.shortType = shortType;
    }
    
    
	
    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
     * 获取业务信息类型，如btl-pay,btl-refund
     *
     * @return infoType - 业务信息类型，如btl-pay,btl-refund
     */
    public String getInfoType() {
        return infoType;
    }

   	/**
     * 设置业务信息类型，如btl-pay,btl-refund
     *
     * @param infoType 业务信息类型，如btl-pay,btl-refund
     */
    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }
	
    /**
     * 获取数据:长链接，参数，json
     *
     * @return data - 数据:长链接，参数，json
     */
    public String getData() {
        return data;
    }

   	/**
     * 设置数据:长链接，参数，json
     *
     * @param data 数据:长链接，参数，json
     */
    public void setData(String data) {
        this.data = data;
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
        ShortLinkPO other = (ShortLinkPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getShortType() == null ? other.getShortType() == null : this.getShortType().equals(other.getShortType()))
            && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
//            && (this.getData() == null ? other.getData() == null : this.getData().equals(other.getData()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        	result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        	result = prime * result + ((getShortType() == null) ? 0 : getShortType().hashCode());
        	result = prime * result + ((getInfoType() == null) ? 0 : getInfoType().hashCode());
        	result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
//        	result = prime * result + ((getData() == null) ? 0 : getData().hashCode());
        	result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        	result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        	result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}