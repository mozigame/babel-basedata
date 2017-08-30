package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_func_retry")
@JsonInclude(Include.NON_NULL)
public class FuncRetryPO extends BaseEntity {
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_funcRetry_cid_seq')")
    private Long cid;

    /**
     * 检查规则id
     */
    @Column(name = "retry_rule_id")
    private Long retryRuleId;

    /**
     * 参数定义
     */
    @Column(name = "param_define")
    private String paramDefine;

    /**
     * 供页面使用，以便于定置或配置一些页面信息
     */
    @Column(name = "param_json")
    private String paramJson;

    /**
     * 备注
     */
    private String remark;
    
    @Transient
    private String retryRule_disp;
    
    @Transient
    private String retryRule_code;

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
     * 获取检查规则id
     *
     * @return retry_rule_id - 检查规则id
     */
    public Long getRetryRuleId() {
        return retryRuleId;
    }

    /**
     * 设置检查规则id
     *
     * @param retryRuleId 检查规则id
     */
    public void setRetryRuleId(Long retryRuleId) {
        this.retryRuleId = retryRuleId;
    }

    /**
     * 获取参数定义
     *
     * @return param_define - 参数定义
     */
    public String getParamDefine() {
        return paramDefine;
    }

    /**
     * 设置参数定义
     *
     * @param paramDefine 参数定义
     */
    public void setParamDefine(String paramDefine) {
        this.paramDefine = paramDefine;
    }

    /**
     * 获取供页面使用，以便于定置或配置一些页面信息
     *
     * @return param_json - 供页面使用，以便于定置或配置一些页面信息
     */
    public String getParamJson() {
        return paramJson;
    }

    /**
     * 设置供页面使用，以便于定置或配置一些页面信息
     *
     * @param paramJson 供页面使用，以便于定置或配置一些页面信息
     */
    public void setParamJson(String paramJson) {
        this.paramJson = paramJson;
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
    
    

    public String getRetryRule_disp() {
		return retryRule_disp;
	}

	public void setRetryRule_disp(String retryRule_disp) {
		this.retryRule_disp = retryRule_disp;
	}
	
	

	public String getRetryRule_code() {
		return retryRule_code;
	}

	public void setRetryRule_code(String retryRule_code) {
		this.retryRule_code = retryRule_code;
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
        FuncRetryPO other = (FuncRetryPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getRetryRuleId() == null ? other.getRetryRuleId() == null : this.getRetryRuleId().equals(other.getRetryRuleId()))
            && (this.getParamDefine() == null ? other.getParamDefine() == null : this.getParamDefine().equals(other.getParamDefine()))
            && (this.getParamJson() == null ? other.getParamJson() == null : this.getParamJson().equals(other.getParamJson()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getRetryRuleId() == null) ? 0 : getRetryRuleId().hashCode());
        result = prime * result + ((getParamDefine() == null) ? 0 : getParamDefine().hashCode());
        result = prime * result + ((getParamJson() == null) ? 0 : getParamJson().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}