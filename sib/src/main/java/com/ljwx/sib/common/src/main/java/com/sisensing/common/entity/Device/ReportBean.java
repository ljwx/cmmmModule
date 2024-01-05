package com.sisensing.common.entity.Device;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.Device
 * @Author: l.chenlu
 * @CreateDate: 2021/6/11 11:32
 * @Description:
 */
public class ReportBean {


    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 设备ID
     */
    private long deviceId;
    /**
     * 糖尿病类型
     */
    private int drType;
    /**
     * 主键ID
     */
    private long id;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 备注
     */
    private String remark;
    /**
     * 报告生成时间
     */
    private String reportCreateTime;
    /**
     * 报告地址
     */
    private String reportUrl;
    /**
     * 审核开始时间
     */
    private String reviewerTimeBegin;
    /**
     * 审核结束时间
     */
    private String reviewerTimeEnd;
    /**
     * 审核人id
     */
    private long reviewerUserId;
    /**
     * 审核人姓名
     */
    private String reviewerUserName;
    /**
     * 用户性别
     */
    private String sex;
    /**
     * 来源(0:系统自动触发 1:用户APP触发 2:客服触发)
     */
    private int source;
    /**
     * 报告状态(0：未审核 1：审核中 2：已审核 -1:失效)
     */
    private Integer status;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 所属用户
     */
    private long userId;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getDrType() {
        return drType;
    }

    public void setDrType(int drType) {
        this.drType = drType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReportCreateTime() {
        return reportCreateTime;
    }

    public void setReportCreateTime(String reportCreateTime) {
        this.reportCreateTime = reportCreateTime;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getReviewerTimeBegin() {
        return reviewerTimeBegin;
    }

    public void setReviewerTimeBegin(String reviewerTimeBegin) {
        this.reviewerTimeBegin = reviewerTimeBegin;
    }

    public String getReviewerTimeEnd() {
        return reviewerTimeEnd;
    }

    public void setReviewerTimeEnd(String reviewerTimeEnd) {
        this.reviewerTimeEnd = reviewerTimeEnd;
    }

    public long getReviewerUserId() {
        return reviewerUserId;
    }

    public void setReviewerUserId(long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
    }

    public String getReviewerUserName() {
        return reviewerUserName;
    }

    public void setReviewerUserName(String reviewerUserName) {
        this.reviewerUserName = reviewerUserName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getStatus() {
        if(status==null){
            status = -2;
        }
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
