package com.spe.smartdocjp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.URI;
import java.time.Instant;


// 全局异常处理器, 拦截 Controller 层及 Filter 链中抛出的异常，统一转换为 RFC 7807 格式。
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理文件上传大小超限异常, 上传文件超过properties中配置的 max-file-size。响应状态：413 Payload Too Large
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ProblemDetail handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "上传文件大小超过了系统允许的最大限制（10MB）。"
        );
        problemDetail.setTitle("File Upload Limit Exceeded");
        problemDetail.setType(URI.create("https://api.spe.smartdoc.com/errors/upload-size-exceeded"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }


    // 处理其他所有未预期的异常, 响应状态：500 Internal Server Error 防止原始堆栈信息泄露给前端，提高安全性。
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception exc) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "服务器内部发生意外错误，请联系管理员。"
        );
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://api.spe.smartdoc.com/help"));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}