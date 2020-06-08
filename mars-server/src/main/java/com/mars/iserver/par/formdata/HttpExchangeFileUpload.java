package com.mars.iserver.par.formdata;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase;

/**
 * 实现设置文件工厂的方法
 */
public class HttpExchangeFileUpload extends FileUploadBase {

    /**
     * 文件工厂
     */
    private FileItemFactory fileItemFactory;

    /**
     * 获取文件工厂
     * @return 文件工厂
     */
    @Override
    public FileItemFactory getFileItemFactory() {
        return fileItemFactory;
    }

    /**
     * 设置文件工厂
     * @param factory The factory class for new file items.
     */
    @Override
    public void setFileItemFactory(FileItemFactory factory) {
        this.fileItemFactory = factory;
    }
}
