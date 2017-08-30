package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_retry_rule")
@XmlRootElement(name="retryRule")
@JsonInclude(Include.NON_NULL)
public class RetryRulePO extends BaseEntity {
    @Id
    @SequenceGenerator(name="cid",sequenceName="select _nextval('_tf_retryRule_cid_seq')")
    private Long cid;

    /**
     * 规则类型，见lookup tf_check_rule_type
     */
    @Column(name = "rule_type")
    private Integer ruleType;

    /**
     * 是否可修改，如果不可修改，表示只能通过数据变更来做
     */
    @Column(name = "can_modify")
    private Integer canModify;
    
    /**
     * 接口服务名
     */
    @Column(name = "service_code")
    private String serviceCode;

    /**
     * 接口任务id
     */
    @Column(name = "intf_task_id")
    private Long intfTaskId;

    /**
     * 备注
     */
    private String remark;

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
     * 获取规则类型，见lookup tf_check_rule_type
     *
     * @return rule_type - 规则类型，见lookup tf_check_rule_type
     */
    public Integer getRuleType() {
        return ruleType;
    }

    /**
     * 设置规则类型，见lookup tf_check_rule_type
     *
     * @param ruleType 规则类型，见lookup tf_check_rule_type
     */
    public void setRuleType(Integer ruleType) {
        this.ruleType = ruleType;
    }
    
    

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
     * 获取是否可修改，如果不可修改，表示只能通过数据变更来做
     *
     * @return can_modify - 是否可修改，如果不可修改，表示只能通过数据变更来做
     */
    public Integer getCanModify() {
        return canModify;
    }

    /**
     * 设置是否可修改，如果不可修改，表示只能通过数据变更来做
     *
     * @param canModify 是否可修改，如果不可修改，表示只能通过数据变更来做
     */
    public void setCanModify(Integer canModify) {
        this.canModify = canModify;
    }

    /**
     * 获取接口任务id
     *
     * @return intf_task_id - 接口任务id
     */
    public Long getIntfTaskId() {
        return intfTaskId;
    }

    /**
     * 设置接口任务id
     *
     * @param intfTaskId 接口任务id
     */
    public void setIntfTaskId(Long intfTaskId) {
        this.intfTaskId = intfTaskId;
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
        RetryRulePO other = (RetryRulePO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getRuleType() == null ? other.getRuleType() == null : this.getRuleType().equals(other.getRuleType()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getCanModify() == null ? other.getCanModify() == null : this.getCanModify().equals(other.getCanModify()))
            && (this.getIntfTaskId() == null ? other.getIntfTaskId() == null : this.getIntfTaskId().equals(other.getIntfTaskId()))
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
        result = prime * result + ((getRuleType() == null) ? 0 : getRuleType().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getCanModify() == null) ? 0 : getCanModify().hashCode());
        result = prime * result + ((getIntfTaskId() == null) ? 0 : getIntfTaskId().hashCode());
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