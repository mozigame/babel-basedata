package com.babel.basedata.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.babel.common.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Table(name = "tf_log_login")
@JsonInclude(Include.NON_NULL)
public class LogLoginPO extends BaseEntity {
    /**
     * 登入日志
     */
    @Id
    @SequenceGenerator(name="",sequenceName="select _nextval('_tf_logLogin_cid_seq')")
    private Long cid;

    /**
     * 服务器id
     */
    @Column(name = "server_id")
    private Long serverId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户账号
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 登入具有的角色权限
     */
    private String roles;

    /**
     * 登入时间
     */
    @Column(name = "login_date")
    private Date loginDate;

    /**
     * 登入类型：0web,1微信,2mobile,3接口,4其他
     */
    @Column(name = "login_type")
    private Integer loginType;

    /**
     * IP地址
     */
    private String ip;

    /**
     * gps经度
     */
    @Column(name = "pos_lat")
    private Float posLat;

    /**
     * gps纬度
     */
    @Column(name = "pos_lon")
    private Float posLon;

    /**
     * 登入地点,如果有gps看gps，否则看ip地址
     */
    private String address;

    /**
     * 来源信息：搜索引擎，域名等
     */
    @Column(name = "refer_info")
    private String referInfo;

    /**
     * 其他信息，mac,浏览器版本，系统版本等
     */
    private String other;

    /**
     * 获取登入日志
     *
     * @return cid - 登入日志
     */
    public Long getCid() {
        return cid;
    }

    /**
     * 设置登入日志
     *
     * @param cid 登入日志
     */
    public void setCid(Long cid) {
        this.cid = cid;
    }

    /**
     * 获取服务器id
     *
     * @return server_id - 服务器id
     */
    public Long getServerId() {
        return serverId;
    }

    /**
     * 设置服务器id
     *
     * @param serverId 服务器id
     */
    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取用户账号
     *
     * @return user_name - 用户账号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户账号
     *
     * @param userName 用户账号
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取登入具有的角色权限
     *
     * @return roles - 登入具有的角色权限
     */
    public String getRoles() {
        return roles;
    }

    /**
     * 设置登入具有的角色权限
     *
     * @param roles 登入具有的角色权限
     */
    public void setRoles(String roles) {
        this.roles = roles;
    }

    /**
     * 获取登入时间
     *
     * @return login_date - 登入时间
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * 设置登入时间
     *
     * @param loginDate 登入时间
     */
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    /**
     * 获取登入类型：0web,1微信,2mobile,3接口,4其他
     *
     * @return login_type - 登入类型：0web,1微信,2mobile,3接口,4其他
     */
    public Integer getLoginType() {
        return loginType;
    }

    /**
     * 设置登入类型：0web,1微信,2mobile,3接口,4其他
     *
     * @param loginType 登入类型：0web,1微信,2mobile,3接口,4其他
     */
    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    /**
     * 获取IP地址
     *
     * @return ip - IP地址
     */
    public String getIp() {
        return ip;
    }

    /**
     * 设置IP地址
     *
     * @param ip IP地址
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * 获取gps经度
     *
     * @return pos_lat - gps经度
     */
    public Float getPosLat() {
        return posLat;
    }

    /**
     * 设置gps经度
     *
     * @param posLat gps经度
     */
    public void setPosLat(Float posLat) {
        this.posLat = posLat;
    }

    /**
     * 获取gps纬度
     *
     * @return pos_lon - gps纬度
     */
    public Float getPosLon() {
        return posLon;
    }

    /**
     * 设置gps纬度
     *
     * @param posLon gps纬度
     */
    public void setPosLon(Float posLon) {
        this.posLon = posLon;
    }

    /**
     * 获取登入地点,如果有gps看gps，否则看ip地址
     *
     * @return address - 登入地点,如果有gps看gps，否则看ip地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置登入地点,如果有gps看gps，否则看ip地址
     *
     * @param address 登入地点,如果有gps看gps，否则看ip地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取来源信息：搜索引擎，域名等
     *
     * @return refer_info - 来源信息：搜索引擎，域名等
     */
    public String getReferInfo() {
        return referInfo;
    }

    /**
     * 设置来源信息：搜索引擎，域名等
     *
     * @param referInfo 来源信息：搜索引擎，域名等
     */
    public void setReferInfo(String referInfo) {
        this.referInfo = referInfo;
    }

    /**
     * 获取其他信息，mac,浏览器版本，系统版本等
     *
     * @return other - 其他信息，mac,浏览器版本，系统版本等
     */
    public String getOther() {
        return other;
    }

    /**
     * 设置其他信息，mac,浏览器版本，系统版本等
     *
     * @param other 其他信息，mac,浏览器版本，系统版本等
     */
    public void setOther(String other) {
        this.other = other;
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
        LogLoginPO other = (LogLoginPO) that;
        return (this.getCid() == null ? other.getCid() == null : this.getCid().equals(other.getCid()))
            && (this.getServerId() == null ? other.getServerId() == null : this.getServerId().equals(other.getServerId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getRoles() == null ? other.getRoles() == null : this.getRoles().equals(other.getRoles()))
            && (this.getLoginDate() == null ? other.getLoginDate() == null : this.getLoginDate().equals(other.getLoginDate()))
            && (this.getLoginType() == null ? other.getLoginType() == null : this.getLoginType().equals(other.getLoginType()))
            && (this.getIp() == null ? other.getIp() == null : this.getIp().equals(other.getIp()))
            && (this.getPosLat() == null ? other.getPosLat() == null : this.getPosLat().equals(other.getPosLat()))
            && (this.getPosLon() == null ? other.getPosLon() == null : this.getPosLon().equals(other.getPosLon()))
            && (this.getAddress() == null ? other.getAddress() == null : this.getAddress().equals(other.getAddress()))
            && (this.getReferInfo() == null ? other.getReferInfo() == null : this.getReferInfo().equals(other.getReferInfo()))
            && (this.getOther() == null ? other.getOther() == null : this.getOther().equals(other.getOther()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCid() == null) ? 0 : getCid().hashCode());
        result = prime * result + ((getServerId() == null) ? 0 : getServerId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getRoles() == null) ? 0 : getRoles().hashCode());
        result = prime * result + ((getLoginDate() == null) ? 0 : getLoginDate().hashCode());
        result = prime * result + ((getLoginType() == null) ? 0 : getLoginType().hashCode());
        result = prime * result + ((getIp() == null) ? 0 : getIp().hashCode());
        result = prime * result + ((getPosLat() == null) ? 0 : getPosLat().hashCode());
        result = prime * result + ((getPosLon() == null) ? 0 : getPosLon().hashCode());
        result = prime * result + ((getAddress() == null) ? 0 : getAddress().hashCode());
        result = prime * result + ((getReferInfo() == null) ? 0 : getReferInfo().hashCode());
        result = prime * result + ((getOther() == null) ? 0 : getOther().hashCode());
        return result;
    }
}