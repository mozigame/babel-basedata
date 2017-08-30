package com.babel.basedata.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.babel.common.core.entity.IPoolInfoVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_thread_pool")
@XmlRootElement(name="threadPool")
@JsonInclude(Include.NON_NULL)
public class ThreadPoolPO extends BaseEntity implements IPoolInfoVO{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * cid
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_threadPool_cid_seq')")
    @Column(name = "cid")
    private Long cid;
    
	/**
     * 系统类型
     */
    @Column(name = "sys_type")
    private String sysType;
    
	/**
     * 线程类型
     */
    @Column(name = "thread_type")
    private Integer threadType;
    
    /**
     * 线程类型
     */
    @Column(name = "limit_time")
    private Integer limitTime;
    
	/**
     * 维护线程的最少数量
     */
    @Column(name = "core_pool_size")
    private Integer corePoolSize;
    
	/**
     * 维护线程的最大数量
     */
    @Column(name = "max_pool_size")
    private Integer maxPoolSize;
    
	/**
     * 所使用的缓冲队列
     */
    @Column(name = "queue_capacity")
    private Integer queueCapacity;
    
	/**
     * 维护线程所允许的空闲时间
     */
    @Column(name = "keep_alive_seconds")
    private Integer keepAliveSeconds;
    
	/**
     * 是否所有线程可退出
     */
    @Column(name = "allow_core_thread_time_out")
    private Integer allowCoreThreadTimeOut;
    
	/**
     * 最近的错误信息
     */
    @Column(name = "last_error")
    private String lastError;
    
	/**
     * 最近出错时间
     */
    @Column(name = "last_error_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastErrorDate;
    
	/**
     * 最近的错误次数
     */
    @Column(name = "last_error_count")
    private Integer lastErrorCount;
    
	/**
     * 最近首次出错时间
     */
    @Column(name = "last_error_first_date")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastErrorFirstDate;
    /**
     * 是否可修改
     */
    @Column(name = "can_modify")
    private Integer canModify;
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
	 * @return the limitTime
	 */
	public Integer getLimitTime() {
		return limitTime;
	}

	/**
	 * @param limitTime the limitTime to set
	 */
	public void setLimitTime(Integer limitTime) {
		this.limitTime = limitTime;
	}

	/**
     * 获取线程类型
     *
     * @return threadType - 线程类型
     */
    public Integer getThreadType() {
        return threadType;
    }

   	/**
     * 设置线程类型
     *
     * @param threadType 线程类型
     */
    public void setThreadType(Integer threadType) {
        this.threadType = threadType;
    }
	
    /**
     * 获取维护线程的最少数量
     *
     * @return corePoolSize - 维护线程的最少数量
     */
    public Integer getCorePoolSize() {
        return corePoolSize;
    }

   	/**
     * 设置维护线程的最少数量
     *
     * @param corePoolSize 维护线程的最少数量
     */
    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
	
    /**
     * 获取维护线程的最大数量
     *
     * @return maxPoolSize - 维护线程的最大数量
     */
    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

   	/**
     * 设置维护线程的最大数量
     *
     * @param maxPoolSize 维护线程的最大数量
     */
    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
	
    /**
     * 获取所使用的缓冲队列
     *
     * @return queueCapacity - 所使用的缓冲队列
     */
    public Integer getQueueCapacity() {
        return queueCapacity;
    }

   	/**
     * 设置所使用的缓冲队列
     *
     * @param queueCapacity 所使用的缓冲队列
     */
    public void setQueueCapacity(Integer queueCapacity) {
        this.queueCapacity = queueCapacity;
    }
	
    /**
     * 获取维护线程所允许的空闲时间
     *
     * @return keepAliveSeconds - 维护线程所允许的空闲时间
     */
    public Integer getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

   	/**
     * 设置维护线程所允许的空闲时间
     *
     * @param keepAliveSeconds 维护线程所允许的空闲时间
     */
    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }
	
    /**
     * 获取是否所有线程可退出
     *
     * @return allowCoreThreadTimeOut - 是否所有线程可退出
     */
    public Integer getAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

   	/**
     * 设置是否所有线程可退出
     *
     * @param allowCoreThreadTimeOut 是否所有线程可退出
     */
    public void setAllowCoreThreadTimeOut(Integer allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }
	
    /**
     * 获取最近的错误信息
     *
     * @return lastError - 最近的错误信息
     */
    public String getLastError() {
        return lastError;
    }

   	/**
     * 设置最近的错误信息
     *
     * @param lastError 最近的错误信息
     */
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }
	
    /**
     * 获取最近出错时间
     *
     * @return lastErrorDate - 最近出错时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    public Date getLastErrorDate() {
        return lastErrorDate;
    }

   	/**
     * 设置最近出错时间
     *
     * @param lastErrorDate 最近出错时间
     */
    public void setLastErrorDate(Date lastErrorDate) {
        this.lastErrorDate = lastErrorDate;
    }
	
    /**
     * 获取最近的错误次数
     *
     * @return lastErrorCount - 最近的错误次数
     */
    public Integer getLastErrorCount() {
        return lastErrorCount;
    }

   	/**
     * 设置最近的错误次数
     *
     * @param lastErrorCount 最近的错误次数
     */
    public void setLastErrorCount(Integer lastErrorCount) {
        this.lastErrorCount = lastErrorCount;
    }
	
    /**
     * 获取最近首次出错时间
     *
     * @return lastErrorFirstDate - 最近首次出错时间
     */
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    public Date getLastErrorFirstDate() {
        return lastErrorFirstDate;
    }

   	/**
     * 设置最近首次出错时间
     *
     * @param lastErrorFirstDate 最近首次出错时间
     */
    public void setLastErrorFirstDate(Date lastErrorFirstDate) {
        this.lastErrorFirstDate = lastErrorFirstDate;
    }
    
    
	
    /**
	 * @return the canModify
	 */
	public Integer getCanModify() {
		return canModify;
	}

	/**
	 * @param canModify the canModify to set
	 */
	public void setCanModify(Integer canModify) {
		this.canModify = canModify;
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
        ThreadPoolPO other = (ThreadPoolPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getSysType() == null ? other.getSysType() == null : this.getSysType().equals(other.getSysType()))
            && (this.getThreadType() == null ? other.getThreadType() == null : this.getThreadType().equals(other.getThreadType()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getCorePoolSize() == null ? other.getCorePoolSize() == null : this.getCorePoolSize().equals(other.getCorePoolSize()))
            && (this.getMaxPoolSize() == null ? other.getMaxPoolSize() == null : this.getMaxPoolSize().equals(other.getMaxPoolSize()))
            && (this.getQueueCapacity() == null ? other.getQueueCapacity() == null : this.getQueueCapacity().equals(other.getQueueCapacity()))
            && (this.getKeepAliveSeconds() == null ? other.getKeepAliveSeconds() == null : this.getKeepAliveSeconds().equals(other.getKeepAliveSeconds()))
            && (this.getAllowCoreThreadTimeOut() == null ? other.getAllowCoreThreadTimeOut() == null : this.getAllowCoreThreadTimeOut().equals(other.getAllowCoreThreadTimeOut()))
            && (this.getLastError() == null ? other.getLastError() == null : this.getLastError().equals(other.getLastError()))
            && (this.getLastErrorDate() == null ? other.getLastErrorDate() == null : this.getLastErrorDate().equals(other.getLastErrorDate()))
            && (this.getLastErrorCount() == null ? other.getLastErrorCount() == null : this.getLastErrorCount().equals(other.getLastErrorCount()))
            && (this.getLastErrorFirstDate() == null ? other.getLastErrorFirstDate() == null : this.getLastErrorFirstDate().equals(other.getLastErrorFirstDate()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
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
        	result = prime * result + ((getThreadType() == null) ? 0 : getThreadType().hashCode());
        	result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        	result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        	result = prime * result + ((getCorePoolSize() == null) ? 0 : getCorePoolSize().hashCode());
        	result = prime * result + ((getMaxPoolSize() == null) ? 0 : getMaxPoolSize().hashCode());
        	result = prime * result + ((getQueueCapacity() == null) ? 0 : getQueueCapacity().hashCode());
        	result = prime * result + ((getKeepAliveSeconds() == null) ? 0 : getKeepAliveSeconds().hashCode());
        	result = prime * result + ((getAllowCoreThreadTimeOut() == null) ? 0 : getAllowCoreThreadTimeOut().hashCode());
        	result = prime * result + ((getLastError() == null) ? 0 : getLastError().hashCode());
        	result = prime * result + ((getLastErrorDate() == null) ? 0 : getLastErrorDate().hashCode());
        	result = prime * result + ((getLastErrorCount() == null) ? 0 : getLastErrorCount().hashCode());
        	result = prime * result + ((getLastErrorFirstDate() == null) ? 0 : getLastErrorFirstDate().hashCode());
        	result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        	result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        	result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        	result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        	result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        	result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        	result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        return result;
    }
}