package com.onedongua.abutton.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {

    /**
     * 复制文件或文件夹
     *
     * @param source      源文件或文件夹
     * @param destination 目标文件或文件夹
     * @throws IOException 如果复制失败
     */
    public static void copy(File source, File destination) throws IOException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination must not be null");
        }

        if (!source.exists()) {
            throw new IOException("Source does not exist: " + source.getAbsolutePath());
        }

        if (source.isDirectory()) {
            if (!destination.exists()) {
                if (!destination.mkdirs()) {
                    throw new IOException("Failed to create directory: " + destination.getAbsolutePath());
                }
            }
            File[] files = source.listFiles();
            if (files != null) {
                for (File file : files) {
                    copy(file, new File(destination, file.getName()));
                }
            }
        } else {
            // 使用 FileInputStream 和 FileOutputStream 来复制文件
            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(destination)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }

    /**
     * 复制文件或文件夹
     *
     * @param sourcePath      源文件或文件夹路径
     * @param destinationPath 目标文件或文件夹路径
     * @throws IOException 如果复制失败
     */
    public static void copy(String sourcePath, String destinationPath) throws IOException {
        if (sourcePath == null || destinationPath == null) {
            throw new IllegalArgumentException("Source and destination paths must not be null");
        }
        copy(new File(sourcePath), new File(destinationPath));
    }

    /**
     * 移动文件或文件夹
     *
     * @param source      源文件或文件夹
     * @param destination 目标文件或文件夹
     * @throws IOException 如果移动失败
     */
    public static void move(File source, File destination) throws IOException {
        copy(source, destination);
        delete(source);
    }

    /**
     * 移动文件或文件夹
     *
     * @param sourcePath      源文件或文件夹路径
     * @param destinationPath 目标文件或文件夹路径
     * @throws IOException 如果移动失败
     */
    public static void move(String sourcePath, String destinationPath) throws IOException {
        if (sourcePath == null || destinationPath == null) {
            throw new IllegalArgumentException("Source and destination paths must not be null");
        }
        move(new File(sourcePath), new File(destinationPath));
    }

    /**
     * 删除文件或文件夹
     *
     * @param file 文件或文件夹
     * @throws IOException 如果删除失败
     */
    public static void delete(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File must not be null");
        }

        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File child : files) {
                    delete(child);
                }
            }
        }

        if (!file.delete()) {
            throw new IOException("Failed to delete file or directory: " + file.getAbsolutePath());
        }
    }

    /**
     * 删除文件或文件夹
     *
     * @param filePath 文件或文件夹路径
     * @throws IOException 如果删除失败
     */
    public static void delete(String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path must not be null");
        }
        delete(new File(filePath));
    }

}
