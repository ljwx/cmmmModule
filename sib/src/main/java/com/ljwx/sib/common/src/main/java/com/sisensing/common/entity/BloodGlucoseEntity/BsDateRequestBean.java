package com.sisensing.common.entity.BloodGlucoseEntity;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/3/18 13:35
 * @desc 上传血糖数据到服务器的请求实体类
 */
public class BsDateRequestBean {

    /**
     * glucoses : [{"bl":0,"cv":0,"cw":0,"gv":0,"gw":0,"i":0,"it":0,"s":0,"st":0,"sv":0,"t":0,"tv":0,"tw":0,"v":0}]
     * userId : 0
     */

    private List<GlucosesDTO> glucoses;
    private String userId;

    public List<GlucosesDTO> getGlucoses() {
        return glucoses;
    }

    public void setGlucoses(List<GlucosesDTO> glucoses) {
        this.glucoses = glucoses;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class GlucosesDTO {
        /**
         * bl : 0 电量值
         * cv : 0 电流值
         * cw : 0 电流报警 0.正常 1.过低 2.过高
         * gv : 0 指血值
         * gw : 0 血糖报警 0.正常 1.过低 2.过高
         * i : 0 有效血糖索引值
         * it : 0 截距
         * s : 0 状态(0:平稳 1:缓慢上升 -1:缓慢下降 2:较
         * st : 0 灵敏度
         * sv : 0 状态值
         * t : 0 时间戳
         * tv : 0 温度值
         * tw : 0 温度报警 0.正常 1.过低 2.过高
         * v : 0 血糖值（算法处理后的值）
         * ast:血糖状态
         */

        private float bl;
        private float cv;
        private int cw;
        private float gv;
        private int gw;
        private int i;
        private float it;
        private int s;
        private float st;
        private int sv;
        private long t;
        private float tv;
        private int tw;
        private float v;
        private int ast;

        public float getBl() {
            return bl;
        }

        public void setBl(float bl) {
            this.bl = bl;
        }

        public float getCv() {
            return cv;
        }

        public void setCv(float cv) {
            this.cv = cv;
        }

        public int getCw() {
            return cw;
        }

        public void setCw(int cw) {
            this.cw = cw;
        }

        public float getGv() {
            return gv;
        }

        public void setGv(float gv) {
            this.gv = gv;
        }

        public int getGw() {
            return gw;
        }

        public void setGw(int gw) {
            this.gw = gw;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public float getIt() {
            return it;
        }

        public void setIt(float it) {
            this.it = it;
        }

        public int getS() {
            return s;
        }

        public void setS(int s) {
            this.s = s;
        }

        public float getSt() {
            return st;
        }

        public void setSt(float st) {
            this.st = st;
        }

        public int getSv() {
            return sv;
        }

        public void setSv(int sv) {
            this.sv = sv;
        }

        public long getT() {
            return t;
        }

        public void setT(long t) {
            this.t = t;
        }

        public float getTv() {
            return tv;
        }

        public void setTv(float tv) {
            this.tv = tv;
        }

        public int getTw() {
            return tw;
        }

        public void setTw(int tw) {
            this.tw = tw;
        }

        public float getV() {
            return v;
        }

        public void setV(float v) {
            this.v = v;
        }

        public int getAst() {
            return ast;
        }

        public void setAst(int ast) {
            this.ast = ast;
        }
    }
}
