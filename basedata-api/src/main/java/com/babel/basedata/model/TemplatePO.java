package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_template")
@XmlRootElement(name="template")
@JsonInclude(Include.NON_NULL)
public class TemplatePO extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * cid
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_template_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
    /**
     * 模板标题
     */
    private String title;
    
    /**
     * 样例
     */
    private String example;
    /**
     * 主行业
     */
    @Column(name = "primary_industry")
    private String primaryIndustry;
    /**
     * 副行业
     */
    @Column(name = "deputy_industry")
    private String deputyIndustry;
    
	/**
     * 系统类型
     */
    @Column(name = "sys_type")
    private String sysType;
    
    /**
     * 消息编码
     */
    @Column(name = "msg_code")
    private String msgCode;
    
	/**
     * 消息类型
     */
    @Column(name = "msg_type")
    private Integer msgType;
    
	/**
     * 模板id
     */
    @Column(name = "tpl_id")
    private String tplId;
    
	/**
     * 模板数据
     */
    @Column(name = "tpl_data")
    private String tplData;
    
	/**
     * 模板样例
     */
    @Column(name = "tpl_example")
    private String tplExample;
    
    
    @Column(name = "param_name")
    private String paramName;
    
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the example
	 */
	public String getExample() {
		return example;
	}

	/**
	 * @param example the example to set
	 */
	public void setExample(String example) {
		this.example = example;
	}

	/**
	 * @return the primaryIndustry
	 */
	public String getPrimaryIndustry() {
		return primaryIndustry;
	}

	/**
	 * @param primaryIndustry the primaryIndustry to set
	 */
	public void setPrimaryIndustry(String primaryIndustry) {
		this.primaryIndustry = primaryIndustry;
	}

	/**
	 * @return the deputyIndustry
	 */
	public String getDeputyIndustry() {
		return deputyIndustry;
	}

	/**
	 * @param deputyIndustry the deputyIndustry to set
	 */
	public void setDeputyIndustry(String deputyIndustry) {
		this.deputyIndustry = deputyIndustry;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	/**
     * 获取模板编码
     *
     * @return tplId - 模板编码
     */
    public String getTplId() {
        return tplId;
    }

   	/**
     * 设置模板编码
     *
     * @param tplId 模板编码
     */
    public void setTplId(String tplId) {
        this.tplId = tplId;
    }
	
    /**
     * 获取模板数据
     *
     * @return tplData - 模板数据
     */
    public String getTplData() {
        return tplData;
    }

   	/**
     * 设置模板数据
     *
     * @param tplData 模板数据
     */
    public void setTplData(String tplData) {
        this.tplData = tplData;
    }
	
    /**
     * 获取模板样例
     *
     * @return tplExample - 模板样例
     */
    public String getTplExample() {
        return tplExample;
    }

   	/**
     * 设置模板样例
     *
     * @param tplExample 模板样例
     */
    public void setTplExample(String tplExample) {
        this.tplExample = tplExample;
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
    
    
	
    public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
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
        TemplatePO other = (TemplatePO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getSysType() == null ? other.getSysType() == null : this.getSysType().equals(other.getSysType()))
            && (this.getMsgType() == null ? other.getMsgType() == null : this.getMsgType().equals(other.getMsgType()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getTplId() == null ? other.getTplId() == null : this.getTplId().equals(other.getTplId()))
            && (this.getTplData() == null ? other.getTplData() == null : this.getTplData().equals(other.getTplData()))
            && (this.getTplExample() == null ? other.getTplExample() == null : this.getTplExample().equals(other.getTplExample()))
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
        	result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        	result = prime * result + ((getTplId() == null) ? 0 : getTplId().hashCode());
        	result = prime * result + ((getTplData() == null) ? 0 : getTplData().hashCode());
        	result = prime * result + ((getTplExample() == null) ? 0 : getTplExample().hashCode());
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