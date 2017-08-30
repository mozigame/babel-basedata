package com.babel.basedata.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_module")
@XmlRootElement(name="module")
@JsonInclude(Include.NON_NULL)
public class ModulePO extends BaseEntity {
	@Id
    @SequenceGenerator(name="cid",sequenceName="select _nextval('_tf_model_cid_seq')")
    private Long cid;
    /**
     * 父模块id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 应用类型,1web,2gui,3mobile,4第三方
     */
    @Column(name = "app_type")
    private Integer appType;

    /**
     * 功能url
     */
    private String url;

    /**
     * 模块类型：1菜单目录，2插件包，3面板，4按钮，文档框等
     */
    private Integer mtype;

    /**
     * 是否显示，1显示，0不显示
     */
    @Column(name = "show_type")
    private Integer showType;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 样式
     */
    private String css;

    /**
     * 菜单颜色或样式
     */
    private String color;

    /**
     * 顺序号
     */
    @Column(name = "order_count")
    private Integer orderCount;

    /**
     * 自定义json参数处理
     */
    @Column(name = "json_value")
    private String jsonValue;
    
    
    @Transient
    private String parentName;
    

    public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	/**
     * 备注
     */
    private String remark;

    /**
     * 获取父模块id
     *
     * @return parent_id - 父模块id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父模块id
     *
     * @param parentId 父模块id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取应用类型,1web,2gui,3mobile,4第三方
     *
     * @return app_type - 应用类型,1web,2gui,3mobile,4第三方
     */
    public Integer getAppType() {
        return appType;
    }

    /**
     * 设置应用类型,1web,2gui,3mobile,4第三方
     *
     * @param appType 应用类型,1web,2gui,3mobile,4第三方
     */
    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    /**
     * 获取功能url
     *
     * @return url - 功能url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置功能url
     *
     * @param url 功能url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取模块类型：1菜单目录，2插件包，3面板，4按钮，文档框等
     *
     * @return mtype - 模块类型：1菜单目录，2插件包，3面板，4按钮，文档框等
     */
    public Integer getMtype() {
        return mtype;
    }

    /**
     * 设置模块类型：1菜单目录，2插件包，3面板，4按钮，文档框等
     *
     * @param mtype 模块类型：1菜单目录，2插件包，3面板，4按钮，文档框等
     */
    public void setMtype(Integer mtype) {
        this.mtype = mtype;
    }

    /**
     * 获取是否显示，1显示，0不显示
     *
     * @return show_type - 是否显示，1显示，0不显示
     */
    public Integer getShowType() {
        return showType;
    }

    /**
     * 设置是否显示，1显示，0不显示
     *
     * @param showType 是否显示，1显示，0不显示
     */
    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    /**
     * 获取菜单图标
     *
     * @return icon - 菜单图标
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置菜单图标
     *
     * @param icon 菜单图标
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取样式
     *
     * @return css - 样式
     */
    public String getCss() {
        return css;
    }

    /**
     * 设置样式
     *
     * @param css 样式
     */
    public void setCss(String css) {
        this.css = css;
    }

    /**
     * 获取菜单颜色或样式
     *
     * @return color - 菜单颜色或样式
     */
    public String getColor() {
        return color;
    }

    /**
     * 设置菜单颜色或样式
     *
     * @param color 菜单颜色或样式
     */
    public void setColor(String color) {
        this.color = color;
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
     * 获取自定义json参数处理
     *
     * @return json_value - 自定义json参数处理
     */
    public String getJsonValue() {
        return jsonValue;
    }

    /**
     * 设置自定义json参数处理
     *
     * @param jsonValue 自定义json参数处理
     */
    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
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
    
    

    public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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
        ModulePO other = (ModulePO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getAppType() == null ? other.getAppType() == null : this.getAppType().equals(other.getAppType()))
            && (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getMtype() == null ? other.getMtype() == null : this.getMtype().equals(other.getMtype()))
            && (this.getShowType() == null ? other.getShowType() == null : this.getShowType().equals(other.getShowType()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getCss() == null ? other.getCss() == null : this.getCss().equals(other.getCss()))
            && (this.getColor() == null ? other.getColor() == null : this.getColor().equals(other.getColor()))
            && (this.getOrderCount() == null ? other.getOrderCount() == null : this.getOrderCount().equals(other.getOrderCount()))
            && (this.getJsonValue() == null ? other.getJsonValue() == null : this.getJsonValue().equals(other.getJsonValue()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
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
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getAppType() == null) ? 0 : getAppType().hashCode());
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getMtype() == null) ? 0 : getMtype().hashCode());
        result = prime * result + ((getShowType() == null) ? 0 : getShowType().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getCss() == null) ? 0 : getCss().hashCode());
        result = prime * result + ((getColor() == null) ? 0 : getColor().hashCode());
        result = prime * result + ((getOrderCount() == null) ? 0 : getOrderCount().hashCode());
        result = prime * result + ((getJsonValue() == null) ? 0 : getJsonValue().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getIfDel() == null) ? 0 : getIfDel().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateDate() == null) ? 0 : getCreateDate().hashCode());
        result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        result = prime * result + ((getModifyDate() == null) ? 0 : getModifyDate().hashCode());
        return result;
    }
}