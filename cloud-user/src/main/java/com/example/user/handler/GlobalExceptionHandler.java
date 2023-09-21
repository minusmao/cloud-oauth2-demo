package com.example.user.handler;

import com.example.common.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 *
 * @author minus
 * @since 2022-11-21 23:17
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 拼装异常响应信息
     *
     * @param message 异常信息
     * @return 异常响应信息 Map
     */
    private Map<String, String> exceptionResult(String message) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return map;
    }

    /**
     * 拼装异常响应信息（多条）
     *
     * @param messages 异常信息列表
     * @return 异常响应信息 Map
     */
    private Map<String, List<String>> exceptionResult(List<String> messages) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("message", messages);
        return map;
    }

    // 系统异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Map<String, String> handler(Exception e) {
        logger.error("运行时异常：{}", e.getMessage());
        return exceptionResult(e.getMessage());
    }

    // 运行时异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public Map<String, String> handler(RuntimeException e) {
        logger.error("运行时异常：{}", e.getMessage());
        return exceptionResult(e.getMessage());
    }

    // 客户端错误输入异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, String> handler(BadRequestException e) {
        logger.error("客户端错误输入异常：{}", e.getMessage());
        return exceptionResult(e.getMessage());
    }

    // 方法参数校验异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, List<String>> handler(ConstraintViolationException e) {
        logger.error("方法参数校验异常：{}", e.getMessage());
        List<String> errorMessages = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        return exceptionResult(errorMessages);
    }

    // 实体参数校验异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handler(MethodArgumentNotValidException e) {
        logger.error("实体参数校验异常：{}", e.getMessage());
        return exceptionResult(e.getMessage());
    }

    // 操作失败异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OperationFailureException.class)
    public Map<String, String> handler(OperationFailureException e) {
        logger.error("操作失败异常：{}", e.getMessage());
        return exceptionResult(e.getMessage());
    }

    // 身份认证异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Map<String, String> handler(UnauthorizedException e) {
        logger.error("身份认证异常：{}", e.getMessage());
        return exceptionResult(e.getMessage());
    }

    // 权限鉴权异常
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public Map<String, String> handler(ForbiddenException e) {
        logger.error("权限鉴权异常：{}", e.getMessage());
        return exceptionResult(e.getMessage());
    }

    // 资源未找到异常
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String, String> handler(ResourceNotFoundException e) {
        logger.error("资源未找到异常：{}", e.getMessage());
        return exceptionResult(e.getMessage());
    }

}
