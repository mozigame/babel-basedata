package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_white_list")
@XmlRootElement(name="whiteList")
@JsonInclude(Include.NON_NULL)
public class WhiteListPO extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_whiteList_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
	/**
     * 白名单类型，同一种类型，优先看白名单，即有白名单时，黑名单失效。
     */
    @Column(name = "white_type_id")
    private Long whiteTypeId;
    
	/**
     * 白名单内容，根据类型来，需检查数据是否正确
     */
    @Column(name = "content")
    private String content;
    
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
     * 获取白名单类型，同一种类型，优先看白名单，即有白名单时，黑名单失效。
     *
     * @return whiteTypeId - 白名单类型，同一种类型，优先看白名单，即有白名单时，黑名单失效。
     */
    public Long getWhiteTypeId() {
        return whiteTypeId;
    }

   	/**
     * 设置白名单类型，同一种类型，优先看白名单，即有白名单时，黑名单失效。
     *
     * @param whiteTypeId 白名单类型，同一种类型，优先看白名单，即有白名单时，黑名单失效。
     */
    public void setWhiteTypeId(Long whiteTypeId) {
        this.whiteTypeId = whiteTypeId;
    }
	
    /**
     * 获取白名单内容，根据类型来，需检查数据是否正确
     *
     * @return content - 白名单内容，根据类型来，需检查数据是否正确
     */
    public String getContent() {
        return content;
    }

   	/**
     * 设置白名单内容，根据类型来，需检查数据是否正确
     *
     * @param content 白名单内容，根据类型来，需检查数据是否正确
     */
    public void setContent(String content) {
        this.content = content;
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
        WhiteListPO other = (WhiteListPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getWhiteTypeId() == null ? other.getWhiteTypeId() == null : this.getWhiteTypeId().equals(other.getWhiteTypeId()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        	result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        	result = prime * result + ((getWhiteTypeId() == null) ? 0 : getWhiteTypeId().hashCode());
        	result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        	result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        	result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        	result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        	result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        	result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        	result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        	result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        return result;
    }
}