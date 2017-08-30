package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_passive_reply")
@XmlRootElement(name="passiveReply")
@JsonInclude(Include.NON_NULL)
public class PassiveReplyPO extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * cid
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_passiveReply_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
	/**
     * 系统类型
     */
    @Column(name = "sys_type")
    private String sysType;
    
	/**
     * 消息类型
     */
    @Column(name = "msg_type")
    private Integer msgType;
    
	/**
     * 标题
     */
    @Column(name = "title")
    private String title;
    
	/**
     * 内容描述
     */
    @Column(name = "description")
    private String description;
    
	/**
     * 图片url
     */
    @Column(name = "pic_url")
    private String picUrl;
    
	/**
     * 详情url
     */
    @Column(name = "url")
    private String url;
    
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
     * 获取系统类型
     *
     * @return sysType - 系统类型
     */
    public String getSysType() {
        return sysType;
    }

   	/**
     * 设置系统类型
     *
     * @param sysType 系统类型
     */
    public void setSysType(String sysType) {
        this.sysType = sysType;
    }
	
    /**
     * 获取消息类型
     *
     * @return msgType - 消息类型
     */
    public Integer getMsgType() {
        return msgType;
    }

   	/**
     * 设置消息类型
     *
     * @param msgType 消息类型
     */
    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
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
     * 获取内容描述
     *
     * @return description - 内容描述
     */
    public String getDescription() {
        return description;
    }

   	/**
     * 设置内容描述
     *
     * @param description 内容描述
     */
    public void setDescription(String description) {
        this.description = description;
    }
	
    /**
     * 获取图片url
     *
     * @return picUrl - 图片url
     */
    public String getPicUrl() {
        return picUrl;
    }

   	/**
     * 设置图片url
     *
     * @param picUrl 图片url
     */
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
	
    /**
     * 获取详情url
     *
     * @return url - 详情url
     */
    public String getUrl() {
        return url;
    }

   	/**
     * 设置详情url
     *
     * @param url 详情url
     */
    public void setUrl(String url) {
        this.url = url;
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
        PassiveReplyPO other = (PassiveReplyPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getSysType() == null ? other.getSysType() == null : this.getSysType().equals(other.getSysType()))
            && (this.getMsgType() == null ? other.getMsgType() == null : this.getMsgType().equals(other.getMsgType()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getPicUrl() == null ? other.getPicUrl() == null : this.getPicUrl().equals(other.getPicUrl()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
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
        	result = prime * result + ((getSysType() == null) ? 0 : getSysType().hashCode());
        	result = prime * result + ((getMsgType() == null) ? 0 : getMsgType().hashCode());
        	result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        	result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        	result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        	result = prime * result + ((getPicUrl() == null) ? 0 : getPicUrl().hashCode());
        	result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
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