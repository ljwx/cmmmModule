package com.sisensing.common.utils;

import com.blankj.utilcode.util.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DebugFileUtils {

    private static final String fileName = "localLog";
    private static File file;

    private static SimpleDateFormat sSimpleDateFormat;

    static {
        file = new File(Utils.getApp().getExternalCacheDir(), fileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(file, "log.txt");

        sSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将文本追加写入到文件
     */
    public static void setAppendFile(String value) {
        FileWriter fw;
        BufferedWriter bw;
        PrintWriter printWriter = null;
        try {
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            printWriter = new PrintWriter(bw);
            printWriter.println(sSimpleDateFormat.format(new Date()) + ":" + value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     * 获取File
     * @return
     */
    public static File getFile(){
        return file;
    }
}
