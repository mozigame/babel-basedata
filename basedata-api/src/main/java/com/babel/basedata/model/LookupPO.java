package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_lookup")
@JsonInclude(Include.NON_NULL)
public class LookupPO extends BaseEntity {
    @Id
    @SequenceGenerator(name="cid",sequenceName="select _nextval('_tf_lookup_cid_seq')")
    private Long cid;

    /**
     * 名称
     */
    @Column(name = "name_cn")
    private String nameCn;

    /**
     * 英文名
     */
    @Column(name = "name_en")
    private String nameEn;

    /**
     * 是否可编辑，0不，1是
     */
    @Column(name = "can_modify")
    private Integer canModify;

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
     * 获取名称
     *
     * @return name_cn - 名称
     */
    public String getNameCn() {
        return nameCn;
    }

    /**
     * 设置名称
     *
     * @param nameCn 名称
     */
    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    /**
     * 获取英文名
     *
     * @return name_en - 英文名
     */
    public String getNameEn() {
        return nameEn;
    }

    /**
     * 设置英文名
     *
     * @param nameEn 英文名
     */
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    /**
     * 获取是否可编辑，0不，1是
     *
     * @return can_modify - 是否可编辑，0不，1是
     */
    public Integer getCanModify() {
        return canModify;
    }

    /**
     * 设置是否可编辑，0不，1是
     *
     * @param canModify 是否可编辑，0不，1是
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
        LookupPO other = (LookupPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getNameCn() == null ? other.getNameCn() == null : this.getNameCn().equals(other.getNameCn()))
            && (this.getNameEn() == null ? other.getNameEn() == null : this.getNameEn().equals(other.getNameEn()))
            && (this.getCanModify() == null ? other.getCanModify() == null : this.getCanModify().equals(other.getCanModify()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIfDel() == null ? other.getIfDel() == null : this.getIfDel().equals(other.getIfDel()))
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
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getNameCn() == null) ? 0 : getNameCn().hashCode());
        result = prime * result + ((getNameEn() == null) ? 0 : getNameEn().hashCode());
        result = prime * result + ((getCanModify() == null) ? 0 : getCanModify().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        return result;
    }
}