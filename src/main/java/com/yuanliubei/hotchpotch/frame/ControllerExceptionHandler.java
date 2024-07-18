//package com.yuanliubei.hotchpotch.frame;
//
//import com.yuanliubei.hotchpotch.utils.JacksonUtil;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.ConstraintViolationException;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.jsqlparser.util.validation.ValidationException;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.collections4.MapUtils;
//import org.springframework.beans.TypeMismatchException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.validation.BindException;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.HttpMediaTypeNotAcceptableException;
//import org.springframework.web.HttpMediaTypeNotSupportedException;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.ServletRequestBindingException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import org.springframework.web.multipart.MultipartException;
//import org.springframework.web.multipart.support.MissingServletRequestPartException;
//import org.springframework.web.servlet.NoHandlerFoundException;
//
//import java.nio.file.AccessDeniedException;
//import java.util.*;
//
///**
// * @author yuanlb
// * @since 2024/5/24
// */
//@Slf4j
//@ControllerAdvice
//@ResponseBody
//public class ControllerExceptionHandler {
//
//    private static final Map<Class<? extends Throwable>, HttpStatus> exception2ErrorCode = new HashMap<>();
//
//    static {
//        exception2ErrorCode.put(ResultException.class, HttpStatus.INTERNAL_SERVER_ERROR);
//        exception2ErrorCode.put(NoHandlerFoundException.class, HttpStatus.NOT_FOUND);
//        exception2ErrorCode.put(ServletRequestBindingException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(BindException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(ValidationException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(MethodArgumentTypeMismatchException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(TypeMismatchException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(MultipartException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);
//        exception2ErrorCode.put(UnsupportedOperationException.class, HttpStatus.NOT_ACCEPTABLE);
//        exception2ErrorCode.put(HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
//        exception2ErrorCode.put(AsyncRequestTimeoutException.class, HttpStatus.SERVICE_UNAVAILABLE);
//        exception2ErrorCode.put(HttpMediaTypeNotSupportedException.class, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
//        exception2ErrorCode.put(HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);
//        exception2ErrorCode.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);
//    }
//
//    @Autowired
//    private Environment environment;
//
//    private static HttpStatus mapToHttpStatus(Throwable t) {
//        for (Class currentClazz = t.getClass(); !Throwable.class.equals(currentClazz); currentClazz = currentClazz.getSuperclass()) {
//            HttpStatus status = (HttpStatus) exception2ErrorCode.get(currentClazz);
//            if (status != null) {
//                return status;
//            }
//        }
//        return HttpStatus.INTERNAL_SERVER_ERROR;
//    }
//
//
//
//    @ExceptionHandler({Throwable.class})
//    public Result<?> handle(HttpServletRequest request, HttpServletResponse response, Throwable t) {
//        HttpStatus status = mapToHttpStatus(t);
//        response.setStatus(status.value());
//        int code = status.value();
//        String message = t.getMessage();
//        if (t instanceof ResultException bizException) {
//            code = bizException.getResultSource().getCode();
//            message = bizException.getResultSource().getMessage();
//        }
//
//        Map<String, String> extendMessage = new HashMap<>();
//        if (t instanceof ConstraintViolationException) {
//            ConstraintViolationException violationException = (ConstraintViolationException) t;
//            Set<ConstraintViolation<?>> violations = violationException.getConstraintViolations();
//            StringBuilder sb = new StringBuilder();
//            violations.forEach((constraintViolation) -> {
//                String msg = constraintViolation.getMessage();
//                sb.append(msg).append(";");
//                extendMessage.put(constraintViolation.getPropertyPath().toString(), msg);
//            });
//            message = sb.deleteCharAt(sb.length() - 1).toString();
//        } else if (!(t instanceof BindException)) {
//            if (t instanceof MethodArgumentTypeMismatchException mismatchException) {
//                message = "参数错误：请检查 " + mismatchException.getName() + " 的值或类型是否正确。";
//            } else if (t instanceof HttpClientErrorException httpClientErrorException) {
//                message = httpClientErrorException.getResponseBodyAsString();
//            }
//        } else {
//            List<ObjectError> argumentErrors;
//            if (t instanceof MethodArgumentNotValidException argumentNotValidException) {
//                argumentErrors = argumentNotValidException.getBindingResult().getAllErrors();
//            } else {
//                BindException bindException = (BindException) t;
//                argumentErrors = bindException.getBindingResult().getAllErrors();
//            }
//
//            StringBuilder sb = new StringBuilder();
//            argumentErrors.forEach((objectError) -> {
//                String msg = objectError.getDefaultMessage();
//                sb.append(msg).append(";");
//                String key = objectError.getObjectName();
//                if (objectError instanceof FieldError) {
//                    key = ((FieldError) objectError).getField();
//                }
//
//                extendMessage.put(key, msg);
//            });
//            message = sb.deleteCharAt(sb.length() - 1).toString();
//        }
//
//        if (message == null) {
//            if (t instanceof NullPointerException) {
//                message = "NPE...";
//            } else {
//                message = "unknown error message";
//            }
//        }
//
//        if (status.is5xxServerError() || this.isDebugEnable()) {
//            log.error(
//                    String.format("error when process request: %s, user header value %s,  params: %s",
//                            request.getRequestURI(),
//                            JacksonUtil.toJson(getHeaders(request)),
//                            JacksonUtil.toJson(request.getParameterMap())),
//                    t);
//        }
//
//        Result<Object> result = Result.from(code, message)
//                .exception(t);
//        if (MapUtils.isNotEmpty(extendMessage)) {
//            result.data(extendMessage);
//        }
//        return result;
//    }
//
//
//    private static Map<String, String> getHeaders(HttpServletRequest request) {
//        Map<String, String> headerMap = new HashMap<>();
//        Enumeration<String> enumeration = request.getHeaderNames();
//        while (enumeration.hasMoreElements()) {
//            String name = enumeration.nextElement();
//            String value = request.getHeader(name);
//            headerMap.put(name, value);
//        }
//        return headerMap;
//    }
//
//
//    private boolean isDebugEnable() {
//        List<String> activeProfiles = Arrays.asList(this.environment.getActiveProfiles());
//        return CollectionUtils.containsAny(activeProfiles, "dev", "test", "debug");
//    }
//}
