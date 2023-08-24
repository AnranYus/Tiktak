package com.anryus.publish.exception;

import java.io.File;

public class CanNotUploadException extends RuntimeException{

    public CanNotUploadException() {
        super("Can not upload file");
        //TODO 更多的失败处理逻辑
    }
}
