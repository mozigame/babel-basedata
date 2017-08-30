package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_sysconfig_user")
@JsonInclude(Include.NON_NULL)
public class SysconfigUserPO extends BaseEntity {
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_sysconfigUser_cid_seq')")
    private Long cid;

    /**
     * 如果没有用户，则user_id=0
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 系统配置id
     */
    @Column(name = "sysconfig_id")
    private Long sysconfigId;

    /**
     * 值
     */
    private String value;

    /**
     * 自定义json属性值
     */
    @Column(name = "value_json")
    private String valueJson;

    /**
     * 参数值1
     */
    private String value1;

    private String value2;

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
     * 获取如果没有用户，则user_id=0
     *
     * @return user_id - 如果没有用户，则user_id=0
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置如果没有用户，则user_id=0
     *
     * @param userId 如果没有用户，则user_id=0
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取系统配置id
     *
     * @return sysconfig_id - 系统配置id
     */
    public Long getSysconfigId() {
        return sysconfigId;
    }

    /**
     * 设置系统配置id
     *
     * @param sysconfigId 系统配置id
     */
    public void setSysconfigId(Long sysconfigId) {
        this.sysconfigId = sysconfigId;
    }

    /**
     * 获取值
     *
     * @return value - 值
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置值
     *
     * @param value 值
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 获取自定义json属性值
     *
     * @return value_json - 自定义json属性值
     */
    public String getValueJson() {
        return valueJson;
    }

    /**
     * 设置自定义json属性值
     *
     * @param valueJson 自定义json属性值
     */
    public void setValueJson(String valueJson) {
        this.valueJson = valueJson;
    }

    /**
     * 获取参数值1
     *
     * @return value1 - 参数值1
     */
    public String getValue1() {
        return value1;
    }

    /**
     * 设置参数值1
     *
     * @param value1 参数值1
     */
    public void setValue1(String value1) {
        this.value1 = value1;
    }

    /**
     * @return value2
     */
    public String getValue2() {
        return value2;
    }

    /**
     * @param value2
     */
    public void setValue2(String value2) {
        this.value2 = value2;
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
        SysconfigUserPO other = (SysconfigUserPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getSysconfigId() == null ? other.getSysconfigId() == null : this.getSysconfigId().equals(other.getSysconfigId()))
            && (this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue()))
            && (this.getValueJson() == null ? other.getValueJson() == null : this.getValueJson().equals(other.getValueJson()))
            && (this.getValue1() == null ? other.getValue1() == null : this.getValue1().equals(other.getValue1()))
            && (this.getValue2() == null ? other.getValue2() == null : this.getValue2().equals(other.getValue2()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateDate() == null ? other.getCreateDate() == null : this.getCreateDate().equals(other.getCreateDate()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getModifyDate() == null ? other.getModifyDate() == null : this.getModifyDate().equals(other.getModifyDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getSysconfigId() == null) ? 0 : getSysconfigId().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        result = prime * result + ((getValueJson() == null) ? 0 : getValueJson().hashCode());
        result = prime * result + ((getValue1() == null) ? 0 : getValue1().hashCode());
        result = prime * result + ((getValue2() == null) ? 0 : getValue2().hashCode());
        result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        return result;
    }
}