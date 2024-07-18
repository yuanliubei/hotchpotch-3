package com.yuanliubei.hotchpotch.frame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yuanliubei.hotchpotch.utils.JacksonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.util.validation.ValidationException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLSyntaxErrorException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author yuanlb
 * @since 2024/7/18
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Environment environment;

    public GlobalExceptionHandler(Environment environment) {
        this.environment = environment;
    }

    private static final Map<Class<? extends Throwable>, HttpStatus> exception2ErrorCode = new HashMap<>();

    static {
        exception2ErrorCode.put(ResultException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        exception2ErrorCode.put(NoHandlerFoundException.class, HttpStatus.NOT_FOUND);
        exception2ErrorCode.put(ServletRequestBindingException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(BindException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(ValidationException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(MethodArgumentTypeMismatchException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(TypeMismatchException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(MultipartException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);
        exception2ErrorCode.put(UnsupportedOperationException.class, HttpStatus.NOT_ACCEPTABLE);
        exception2ErrorCode.put(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
        exception2ErrorCode.put(AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE);
        exception2ErrorCode.put(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        exception2ErrorCode.put(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);
        exception2ErrorCode.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);
    }

    private static HttpStatus mapToHttpStatus(Throwable t) {
        for (Class currentClazz = t.getClass(); !Throwable.class.equals(currentClazz); currentClazz = currentClazz.getSuperclass()) {
            HttpStatus status = (HttpStatus) exception2ErrorCode.get(currentClazz);
            if (status != null) {
                return status;
            }
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }


    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> processException(BindException e) {
        log.error("BindException:{}", e.getMessage());
        String msg = e.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return Result.error(msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> processException(ConstraintViolationException e) {
        log.error("ConstraintViolationException:{}", e.getMessage());
        String msg = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("；"));
        return Result.error(CommonResultCode.ERROR_PARAM_ERROR, msg);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> processException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException:{}", e.getMessage());
        String msg = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("；"));
        return Result.error(CommonResultCode.ERROR_PARAM_ERROR, msg);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> processException(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return Result.error(CommonResultCode.NOT_FIND);
    }

    /**
     * MissingServletRequestParameterException
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> processException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return Result.error(CommonResultCode.ERROR_PARAM_ERROR);
    }

    /**
     * MethodArgumentTypeMismatchException
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> processException(MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return Result.error(CommonResultCode.ERROR_PARAM_ERROR, "类型错误");
    }

    /**
     * ServletException
     */
    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> processException(ServletException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数异常，异常原因：{}", e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleJsonProcessingException(JsonProcessingException e) {
        log.error("Json转换异常，异常原因：{}", e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    /**
     * HttpMessageNotReadableException
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> processException(HttpMessageNotReadableException e) {
        log.error(e.getMessage(), e);
        String errorMessage = "请求体不可为空";
        Throwable cause = e.getCause();
        if (cause != null) {
            errorMessage = convertMessage(cause);
        }
        return Result.error(errorMessage);
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> processException(TypeMismatchException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleBadSqlGrammarException(BadSqlGrammarException e) {
        log.error(e.getMessage(), e);
        String errorMsg = e.getMessage();
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> processSQLSyntaxErrorException(SQLSyntaxErrorException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getMessage());
    }


    @ExceptionHandler(ResultException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleBizException(ResultException e, HttpServletRequest request) {
        log.error(
                String.format("error when process request: %s, user header value %s,  params: %s",
                        request.getRequestURI(),
                        JacksonUtil.toJson(getHeaders(request)),
                        JacksonUtil.toJson(request.getParameterMap())),
                e);
        Result<Object> result = Result.error(e.getMessage());
        if (isDebugEnable()) {
            result.exception(e);
        }
        return result;
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Result<?> handleException(Exception e,HttpServletRequest request) throws Exception {
//        // 将 Spring Security 异常继续抛出，以便交给自定义处理器处理
//        if (e instanceof org.springframework.security.access.AccessDeniedException
//                || e instanceof AuthenticationException) {
//            throw e;
//        }
//        log.error(
//                String.format("error when process request: %s, user header value %s,  params: %s",
//                        request.getRequestURI(),
//                        JacksonUtil.toJson(getHeaders(request)),
//                        JacksonUtil.toJson(request.getParameterMap())),
//                e);
//        e.printStackTrace();
//        Result<Object> result = Result.error(e.getLocalizedMessage());
//        if (isDebugEnable()) {
//            result.exception(e);
//        }
//        return result;
//    }

    /**
     * 传参类型错误时，用于消息转换
     *
     * @param throwable 异常
     * @return 错误信息
     */
    private String convertMessage(Throwable throwable) {
        String error = throwable.toString();
        String regulation = "\\[\"(.*?)\"]+";
        Pattern pattern = Pattern.compile(regulation);
        Matcher matcher = pattern.matcher(error);
        String group = "";
        if (matcher.find()) {
            String matchString = matcher.group();
            matchString = matchString.replace("[", "").replace("]", "");
            matchString = "%s字段类型错误".formatted(matchString.replaceAll("\"", ""));
            group += matchString;
        }
        return group;
    }


    /**
     * 获取请求头
     * @param request
     * @return
     */
    private static Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            headerMap.put(name, value);
        }
        return headerMap;
    }

    /**
     * 判断是否开启debug模式
     * @return
     */
    private boolean isDebugEnable() {
        List<String> activeProfiles = Arrays.asList(this.environment.getActiveProfiles());
        return CollectionUtils.containsAny(activeProfiles, "dev", "test", "debug");
    }
}
