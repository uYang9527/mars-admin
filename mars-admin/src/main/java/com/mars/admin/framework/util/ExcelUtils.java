package com.mars.admin.framework.util;

import com.mars.admin.modules.system.entity.SysLoginInfo;
import com.mars.admin.modules.system.entity.SysOperLog;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel导出工具类
 *
 * @author Mars
 */
public class ExcelUtils {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 导出操作日志到Excel
     *
     * @param response HTTP响应
     * @param logList 操作日志列表
     * @throws IOException IO异常
     */
    public static void exportOperLogToExcel(HttpServletResponse response, List<SysOperLog> logList) throws IOException {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("操作日志");

        // 创建表头样式
        CellStyle headerStyle = createHeaderStyle(workbook);

        // 创建数据样式
        CellStyle dataStyle = createDataStyle(workbook);

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"序号", "模块标题", "业务类型", "操作人员", "主机地址", "操作地点", "操作状态", "操作时间", "消耗时间(ms)"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 填充数据
        for (int i = 0; i < logList.size(); i++) {
            SysOperLog log = logList.get(i);
            Row row = sheet.createRow(i + 1);

            // 序号
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(i + 1);
            cell0.setCellStyle(dataStyle);

            // 模块标题
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(log.getTitle() != null ? log.getTitle() : "");
            cell1.setCellStyle(dataStyle);

            // 业务类型
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(getBusinessTypeText(log.getBusinessType()));
            cell2.setCellStyle(dataStyle);

            // 操作人员
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(log.getOperName() != null ? log.getOperName() : "");
            cell3.setCellStyle(dataStyle);

            // 主机地址
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(log.getOperIp() != null ? log.getOperIp() : "");
            cell4.setCellStyle(dataStyle);

            // 操作地点
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(log.getOperLocation() != null ? log.getOperLocation() : "");
            cell5.setCellStyle(dataStyle);

            // 操作状态
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(log.getStatus() != null && log.getStatus() == 0 ? "正常" : "异常");
            cell6.setCellStyle(dataStyle);

            // 操作时间
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(log.getOperTime() != null ? log.getOperTime().format(DATETIME_FORMATTER) : "");
            cell7.setCellStyle(dataStyle);

            // 消耗时间
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(log.getCostTime() != null ? log.getCostTime().toString() : "0");
            cell8.setCellStyle(dataStyle);
        }

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 设置响应头
        setExcelResponseHeader(response, "操作日志_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");

        // 写入响应
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * 导出登录日志到Excel
     *
     * @param response HTTP响应
     * @param logList 登录日志列表
     * @throws IOException IO异常
     */
    public static void exportLoginLogToExcel(HttpServletResponse response, List<SysLoginInfo> logList) throws IOException {
        // 创建工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("登录日志");

        // 创建表头样式
        CellStyle headerStyle = createHeaderStyle(workbook);

        // 创建数据样式
        CellStyle dataStyle = createDataStyle(workbook);

        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"序号", "用户账号", "登录IP", "登录地点", "浏览器", "操作系统", "登录状态", "提示消息", "登录时间"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 填充数据
        for (int i = 0; i < logList.size(); i++) {
            SysLoginInfo log = logList.get(i);
            Row row = sheet.createRow(i + 1);

            // 序号
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(i + 1);
            cell0.setCellStyle(dataStyle);

            // 用户账号
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(log.getUserName() != null ? log.getUserName() : "");
            cell1.setCellStyle(dataStyle);

            // 登录IP
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(log.getIpaddr() != null ? log.getIpaddr() : "");
            cell2.setCellStyle(dataStyle);

            // 登录地点
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(log.getLoginLocation() != null ? log.getLoginLocation() : "");
            cell3.setCellStyle(dataStyle);

            // 浏览器
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(log.getBrowser() != null ? log.getBrowser() : "");
            cell4.setCellStyle(dataStyle);

            // 操作系统
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(log.getOs() != null ? log.getOs() : "");
            cell5.setCellStyle(dataStyle);

            // 登录状态
            Cell cell6 = row.createCell(6);
            cell6.setCellValue("0".equals(log.getStatus()) ? "成功" : "失败");
            cell6.setCellStyle(dataStyle);

            // 提示消息
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(log.getMsg() != null ? log.getMsg() : "");
            cell7.setCellStyle(dataStyle);

            // 登录时间
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(log.getLoginTime() != null ? log.getLoginTime().format(DATETIME_FORMATTER) : "");
            cell8.setCellStyle(dataStyle);
        }

        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 设置响应头
        setExcelResponseHeader(response, "登录日志_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");

        // 写入响应
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * 创建表头样式
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // 设置背景色
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // 设置对齐方式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置字体
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        return style;
    }

    /**
     * 创建数据样式
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // 设置边框
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        // 设置对齐方式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置字体
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);

        return style;
    }

    /**
     * 设置Excel文件下载响应头
     */
    private static void setExcelResponseHeader(HttpServletResponse response, String fileName) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");

        try {
            // 对文件名进行URL编码
            String encodedFileName = java.net.URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

            // 使用RFC 5987标准设置文件名，同时兼容不同浏览器
            response.setHeader("Content-Disposition",
                "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
        } catch (Exception e) {
            // 如果编码失败，使用默认文件名
            response.setHeader("Content-Disposition", "attachment; filename=\"export.xlsx\"");
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }

    /**
     * 获取业务类型文本
     */
    private static String getBusinessTypeText(Integer type) {
        if (type == null) return "未知";
        switch (type) {
            case 0: return "其它";
            case 1: return "新增";
            case 2: return "修改";
            case 3: return "删除";
            default: return "未知";
        }
    }
}
