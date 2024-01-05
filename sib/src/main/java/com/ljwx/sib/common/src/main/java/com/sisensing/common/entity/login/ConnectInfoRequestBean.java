package com.sisensing.common.entity.login;

/**
 * @author y.xie
 * @date 2021/6/30 16:24
 * @desc 终端连接信息上传
 */
public class ConnectInfoRequestBean {

    private String connInfo;
    private String module;
    private String reportTime;

    public String getConnInfo() {
        return connInfo;
    }

    public void setConnInfo(String connInfo) {
        this.connInfo = connInfo;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }
}
