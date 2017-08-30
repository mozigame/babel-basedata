package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_lookup_item")
@XmlRootElement(name="lookupItem")
@JsonInclude(Include.NON_NULL)
public class LookupItemPO extends BaseEntity {
	private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_lookupItem_cid_seq')")
    private Long cid;
    

    /**
     * lookup id
     */
    @Column(name = "lookup_id")
    private Long lookupId;

    /**
     * 语言编码
     */
    private String language;

    /**
     * 子项编码
     */
    @Column(name = "item_code")
    private String itemCode;

    /**
     * 子项名
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 子项值
     */
    @Column(name = "item_value")
    private String itemValue;

    /**
     * 顺序号
     */
    @Column(name = "order_count")
    private Integer orderCount;

    /**
     * 颜色值
     */
    private String color;

    /**
     * 自定义属性
     */
    @Column(name = "attr_json")
    private String attrJson;

    /**
     * 备用值属性1
     */
    private String attr1;

    /**
     * 备用值属性2
     */
    private String attr2;

    /**
     * 备用值属性3
     */
    private String attr3;

    /**
     * 备用值属性4
     */
    private String attr4;

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
     * 获取lookup id
     *
     * @return lookup_id - lookup id
     */
    public Long getLookupId() {
        return lookupId;
    }

    /**
     * 设置lookup id
     *
     * @param lookupId lookup id
     */
    public void setLookupId(Long lookupId) {
        this.lookupId = lookupId;
    }

    /**
     * 获取语言编码
     *
     * @return language - 语言编码
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 设置语言编码
     *
     * @param language 语言编码
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 获取子项编码
     *
     * @return item_code - 子项编码
     */
    public String getItemCode() {
        return itemCode;
    }

    /**
     * 设置子项编码
     *
     * @param itemCode 子项编码
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    /**
     * 获取子项名
     *
     * @return item_name - 子项名
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * 设置子项名
     *
     * @param itemName 子项名
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * 获取子项值
     *
     * @return item_value - 子项值
     */
    public String getItemValue() {
        return itemValue;
    }

    /**
     * 设置子项值
     *
     * @param itemValue 子项值
     */
    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    /**
     * 获取顺序号
     *
     * @return order_count - 顺序号
     */
    public Integer getOrderCount() {
        return orderCount;
    }

    /**
     * 设置顺序号
     *
     * @param orderCount 顺序号
     */
    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    /**
     * 获取颜色值
     *
     * @return color - 颜色值
     */
    public String getColor() {
        return color;
    }

    /**
     * 设置颜色值
     *
     * @param color 颜色值
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * 获取自定义属性
     *
     * @return attr_json - 自定义属性
     */
    public String getAttrJson() {
        return attrJson;
    }

    /**
     * 设置自定义属性
     *
     * @param attrJson 自定义属性
     */
    public void setAttrJson(String attrJson) {
        this.attrJson = attrJson;
    }

    /**
     * 获取备用值属性1
     *
     * @return attr1 - 备用值属性1
     */
    public String getAttr1() {
        return attr1;
    }

    /**
     * 设置备用值属性1
     *
     * @param attr1 备用值属性1
     */
    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    /**
     * 获取备用值属性2
     *
     * @return attr2 - 备用值属性2
     */
    public String getAttr2() {
        return attr2;
    }

    /**
     * 设置备用值属性2
     *
     * @param attr2 备用值属性2
     */
    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    /**
     * 获取备用值属性3
     *
     * @return attr3 - 备用值属性3
     */
    public String getAttr3() {
        return attr3;
    }

    /**
     * 设置备用值属性3
     *
     * @param attr3 备用值属性3
     */
    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    /**
     * 获取备用值属性4
     *
     * @return attr4 - 备用值属性4
     */
    public String getAttr4() {
        return attr4;
    }

    /**
     * 设置备用值属性4
     *
     * @param attr4 备用值属性4
     */
    public void setAttr4(String attr4) {
        this.attr4 = attr4;
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
        LookupItemPO other = (LookupItemPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getLookupId() == null ? other.getLookupId() == null : this.getLookupId().equals(other.getLookupId()))
            && (this.getLanguage() == null ? other.getLanguage() == null : this.getLanguage().equals(other.getLanguage()))
            && (this.getItemCode() == null ? other.getItemCode() == null : this.getItemCode().equals(other.getItemCode()))
            && (this.getItemName() == null ? other.getItemName() == null : this.getItemName().equals(other.getItemName()))
            && (this.getItemValue() == null ? other.getItemValue() == null : this.getItemValue().equals(other.getItemValue()))
            && (this.getOrderCount() == null ? other.getOrderCount() == null : this.getOrderCount().equals(other.getOrderCount()))
            && (this.getColor() == null ? other.getColor() == null : this.getColor().equals(other.getColor()))
            && (this.getAttrJson() == null ? other.getAttrJson() == null : this.getAttrJson().equals(other.getAttrJson()))
            && (this.getAttr1() == null ? other.getAttr1() == null : this.getAttr1().equals(other.getAttr1()))
            && (this.getAttr2() == null ? other.getAttr2() == null : this.getAttr2().equals(other.getAttr2()))
            && (this.getAttr3() == null ? other.getAttr3() == null : this.getAttr3().equals(other.getAttr3()))
            && (this.getAttr4() == null ? other.getAttr4() == null : this.getAttr4().equals(other.getAttr4()))
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
        result = prime * result + ((getLookupId() == null) ? 0 : getLookupId().hashCode());
        result = prime * result + ((getLanguage() == null) ? 0 : getLanguage().hashCode());
        result = prime * result + ((getItemCode() == null) ? 0 : getItemCode().hashCode());
        result = prime * result + ((getItemName() == null) ? 0 : getItemName().hashCode());
        result = prime * result + ((getItemValue() == null) ? 0 : getItemValue().hashCode());
        result = prime * result + ((getOrderCount() == null) ? 0 : getOrderCount().hashCode());
        result = prime * result + ((getColor() == null) ? 0 : getColor().hashCode());
        result = prime * result + ((getAttrJson() == null) ? 0 : getAttrJson().hashCode());
        result = prime * result + ((getAttr1() == null) ? 0 : getAttr1().hashCode());
        result = prime * result + ((getAttr2() == null) ? 0 : getAttr2().hashCode());
        result = prime * result + ((getAttr3() == null) ? 0 : getAttr3().hashCode());
        result = prime * result + ((getAttr4() == null) ? 0 : getAttr4().hashCode());
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