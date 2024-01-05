package com.sisensing.common.entity.personalcenter;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.personalcenter.entity
 * @Author: l.chenlu
 * @CreateDate: 2021/6/7 17:33
 * @Description:
 */
public class AppUpdateEntity {

    /**
     * 更新内容
     */
    private String content;
    /**
     * 安装包下载地址
     */
    private String downloadUrl;
    /**
     * 是否强制更新
     */
    private boolean force;
    /**
     * 是否存在更新
     */
    private boolean hasUpdate;
    /**
     * 版本号
     */
    private int version;
    /**
     * 版本名称
     */
    private String versionName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

}
