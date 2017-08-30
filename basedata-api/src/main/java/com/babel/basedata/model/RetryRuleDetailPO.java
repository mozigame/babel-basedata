package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_retry_rule_detail")
@XmlRootElement(name="retryRuleDetail")
@JsonInclude(Include.NON_NULL)
public class RetryRuleDetailPO extends BaseEntity {
    @Id
    @SequenceGenerator(name="cid",sequenceName="select _nextval('_tf_retryRuleDetail_cid_seq')")
    private Long cid;

    /**
     * 检查规则id
     */
    @Column(name = "retry_rule_id")
    private Long retryRuleId;

    /**
     * 检查类型:1，时间限制策略，2加密类型策略
     */
    @Column(name = "retry_type")
    private Integer retryType;

    /**
     * 单位：秒
     */
    private Integer period;

    /**
     * 最大次数
周期内最大业务发生次数
     */
    @Column(name = "max_count")
    private Integer maxCount;
    
    /**
     * ip限制，all所有IP，all_out所有外部用户，all_in所有内网用户，ip指定的IP地址，多个ip以“,”隔开
     */
    @Column(name = "limit_ip")
    private String limitIp;
    /**
     * 用户限制:all所有用户,none不对用户进行限制,userName指定的用户,多个用户以“,”隔开
     */
    @Column(name = "limit_user")
    private String limitUser;

    /**
     * 提示信息
     */
    @Column(name = "tips_info")
    private String tipsInfo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 供页面使用，以便于定置或配置一些页面信息
     */
    @Column(name = "param_json")
    private String paramJson;

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
     * 获取检查类型:1，时间限制策略，2加密类型策略
     *
     * @return retry_type - 检查类型:1，时间限制策略，2加密类型策略
     */
    public Integer getRetryType() {
        return retryType;
    }

    /**
     * 设置检查类型:1，时间限制策略，2加密类型策略
     *
     * @param retryType 检查类型:1，时间限制策略，2加密类型策略
     */
    public void setRetryType(Integer retryType) {
        this.retryType = retryType;
    }

    /**
     * 获取单位：秒
     *
     * @return period - 单位：秒
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * 设置单位：秒
     *
     * @param period 单位：秒
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * 获取最大次数
周期内最大业务发生次数
     *
     * @return max_count - 最大次数
周期内最大业务发生次数
     */
    public Integer getMaxCount() {
        return maxCount;
    }

    /**
     * 设置最大次数
周期内最大业务发生次数
     *
     * @param maxCount 最大次数
周期内最大业务发生次数
     */
    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * 获取提示信息
     *
     * @return tips_info - 提示信息
     */
    public String getTipsInfo() {
        return tipsInfo;
    }

    /**
     * 设置提示信息
     *
     * @param tipsInfo 提示信息
     */
    public void setTipsInfo(String tipsInfo) {
        this.tipsInfo = tipsInfo;
    }
    
    
    /**
     * ip限制，all所有IP，all_out所有外部用户，all_in所有内网用户，ip指定的IP地址，多个ip以“,”隔开
     * @return
     */
    public String getLimitIp() {
		return limitIp;
	}

	public void setLimitIp(String limitIp) {
		this.limitIp = limitIp;
	}

	/**
	 * 用户限制:all所有用户,none不对用户进行限制,userName指定的用户,多个用户以“,”隔开
	 * @return
	 */
	public String getLimitUser() {
		return limitUser;
	}

	public void setLimitUser(String limitUser) {
		this.limitUser = limitUser;
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
        RetryRuleDetailPO other = (RetryRuleDetailPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getRetryRuleId() == null ? other.getRetryRuleId() == null : this.getRetryRuleId().equals(other.getRetryRuleId()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getRetryType() == null ? other.getRetryType() == null : this.getRetryType().equals(other.getRetryType()))
            && (this.getPeriod() == null ? other.getPeriod() == null : this.getPeriod().equals(other.getPeriod()))
            && (this.getMaxCount() == null ? other.getMaxCount() == null : this.getMaxCount().equals(other.getMaxCount()))
            && (this.getTipsInfo() == null ? other.getTipsInfo() == null : this.getTipsInfo().equals(other.getTipsInfo()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getParamJson() == null ? other.getParamJson() == null : this.getParamJson().equals(other.getParamJson()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getRetryRuleId() == null) ? 0 : getRetryRuleId().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getRetryType() == null) ? 0 : getRetryType().hashCode());
        result = prime * result + ((getPeriod() == null) ? 0 : getPeriod().hashCode());
        result = prime * result + ((getMaxCount() == null) ? 0 : getMaxCount().hashCode());
        result = prime * result + ((getTipsInfo() == null) ? 0 : getTipsInfo().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getParamJson() == null) ? 0 : getParamJson().hashCode());
        return result;
    }
}