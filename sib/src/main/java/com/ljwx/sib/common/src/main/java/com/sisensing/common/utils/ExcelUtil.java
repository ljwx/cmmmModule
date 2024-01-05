package com.sisensing.common.utils;

import android.content.Context;

import com.blankj.utilcode.util.TimeUtils;
import com.sisensing.common.R;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * @author y.xie
 * @date 2021/5/18 14:51
 * @desc
 */
public class ExcelUtil {
    private static WritableFont arial14font = null;

    private static WritableCellFormat arial14format = null;
    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
            arial14format = new WritableCellFormat(arial14font);
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            arial10format = new WritableCellFormat(arial10font);
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Excel
     *
     * @param fileName 导出excel存放的地址（目录）
     * @param sheetName excel的sheet名
     * @param colName  excel中包含的列名（可以有多个）
     */
    public static void initExcel(String fileName, String sheetName, String[] colName) {
        format();
        WritableWorkbook workbook = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            workbook = Workbook.createWorkbook(file);
            //设置表格的名字
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            //创建标题栏
            sheet.addCell((WritableCell) new Label(0, 0, fileName, arial14format));
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高
            sheet.setRowView(0, 340);
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(Context context,List<T> objList, String fileName, Context c, boolean isValid,boolean exportMill) {
        if (objList != null && objList.size() > 0) {
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);
                in = new FileInputStream(new File(fileName));
                Workbook workbook = Workbook.getWorkbook(in);
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                WritableSheet sheet = writebook.getSheet(0);
                //"index笔数","血糖时间","电流值","血糖值","温度","敏感度","电量","温度预警","电流预警","血糖预警","血糖趋势","血糖状态"
                for (int j = 0; j < objList.size(); j++) {
                    BloodGlucoseEntity entity = (BloodGlucoseEntity) objList.get(j);
                    List<String> list = new ArrayList<>();
                    //血糖时间
                    String time;
                    if (exportMill){
                        time = String.valueOf(entity.getProcessedTimeMill());
                    }else {
                        time = TimeUtils.millis2String(entity.getProcessedTimeMill(), "yyyy/MM/dd HH:mm");
                    }
                    if (isValid){
                        //血糖时间
                        list.add(time);
                        //血糖值
                        int status = entity.getAlarmStatus();
                        if (status==2||status==3||status==4){
                            list.add(context.getString(R.string.error));
                        }else {
                            float value = entity.getGlucoseValue();
                            if (value<2.2f){
                                entity.setGlucoseValue(2.2f);
                            }
                            if (value>25f){
                                entity.setGlucoseValue(25f);
                            }
                            //list.add(String.valueOf(entity.getGlucoseValue()));
                            list.add(GlucoseUtils.getUserConfigValue(entity.getGlucoseValue(), true));
                        }
                    }else {
                        //index笔数
                        list.add(String.valueOf(entity.getIndex()));
                        //血糖时间
                        list.add(time);
                        //电流值
                        list.add(String.valueOf(entity.getCurrentValue()));
                        //血糖值
                        list.add(String.valueOf(entity.getGlucoseValue()));
                        //温度
                        list.add(String.valueOf(entity.getTemperatureValue()));
                        //敏感度
                        list.add(String.valueOf(entity.getSensitivity()));
                        //电量
                        list.add(String.valueOf(entity.getElectric()));
                        //温度预警
                        list.add(String.valueOf(entity.getTemperatureWarning()));
                        //电流预警
                        list.add(String.valueOf(entity.getCurrentWarning()));
                        //血糖预警
                        list.add(String.valueOf(entity.getGlucoseWarning()));
                        //血糖趋势
                        list.add(String.valueOf(entity.getGlucoseTrend()));
                        //血糖状态
                        list.add(String.valueOf(entity.getAlarmStatus()));
                    }
                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 8);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 5);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }

                writebook.write();
                //ToastUtils.showShort("导出Excel成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}
