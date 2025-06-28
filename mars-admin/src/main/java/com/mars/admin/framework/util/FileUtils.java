package com.mars.admin.framework.util;

import org.springframework.util.StringUtils;

/**
 * 文件工具类
 *
 * @author Mars.wq
 */
public class FileUtils {

    /**
     * 获取文件扩展名
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getFileExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * 获取文件名（不含扩展名）
     *
     * @param fileName 文件名
     * @return 文件名（不含扩展名）
     */
    public static String getFileNameWithoutExtension(String fileName) {
        if (!StringUtils.hasText(fileName)) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    /**
     * 格式化文件大小
     *
     * @param size 文件大小（字节）
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }

    /**
     * 判断是否为图片文件
     *
     * @param fileName 文件名
     * @return 是否为图片文件
     */
    public static boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName);
        return "jpg".equals(extension) || "jpeg".equals(extension) || 
               "png".equals(extension) || "gif".equals(extension) || 
               "bmp".equals(extension) || "webp".equals(extension);
    }

    /**
     * 判断是否为文档文件
     *
     * @param fileName 文件名
     * @return 是否为文档文件
     */
    public static boolean isDocumentFile(String fileName) {
        String extension = getFileExtension(fileName);
        return "doc".equals(extension) || "docx".equals(extension) || 
               "xls".equals(extension) || "xlsx".equals(extension) || 
               "ppt".equals(extension) || "pptx".equals(extension) || 
               "pdf".equals(extension) || "txt".equals(extension);
    }

    /**
     * 判断文件类型是否被允许
     *
     * @param fileName 文件名
     * @param allowedExtensions 允许的扩展名数组
     * @return 是否被允许
     */
    public static boolean isAllowedFile(String fileName, String[] allowedExtensions) {
        if (allowedExtensions == null || allowedExtensions.length == 0) {
            return true;
        }
        String extension = getFileExtension(fileName);
        for (String allowedExt : allowedExtensions) {
            if (allowedExt.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
} 