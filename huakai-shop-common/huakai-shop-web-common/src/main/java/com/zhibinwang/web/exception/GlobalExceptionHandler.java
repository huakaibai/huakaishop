/*
package com.zhibinwang.web.exception;

import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import com.zhibinwang.constants.WebConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

*/
/**
 * @author 花开
 *//*

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(RuntimeException.class)

	public String exceptionHandler(Exception e) {
		log.error("###全局捕获异常##", e);
		//跳转到500页面
		return setResultError("系统错误!");
	}

	@ExceptionHandler(BindException.class)

	public String handler(BindException ex){
		StringBuilder sb = new StringBuilder();
		FieldError fieldError = ex.getFieldError();
		sb.append(fieldError.getDefaultMessage());

		return setResultError(WebConstants.HTTP_RES_CODE_PARAMETERROR_204,sb.toString());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public String handler(ConstraintViolationException ex){
		StringBuilder sb = new StringBuilder();
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			sb.append(constraintViolation.getMessage());
		}
		System.out.println(sb.toString());
		return setResultError(WebConstants.HTTP_RES_CODE_PARAMETERROR_204,sb.toString());
	}
}
*/
