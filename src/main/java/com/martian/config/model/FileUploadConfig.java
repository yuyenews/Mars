package com.martian.config.model;

/**
 * 文件上传配置
 */
public class FileUploadConfig {

    /**
     * 单个文件最大值
     */
    private int fileSizeMax = 2*1024*1024;

    /**
     * 文件最大值
     */
    private int sizeMax= 10*1024*1024;

    public int getFileSizeMax() {
        return fileSizeMax;
    }

    public void setFileSizeMax(int fileSizeMax) {
        this.fileSizeMax = fileSizeMax;
    }

    public int getSizeMax() {
        return sizeMax;
    }

    public void setSizeMax(int sizeMax) {
        this.sizeMax = sizeMax;
    }
}
