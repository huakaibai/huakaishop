package com.zhibinwang.core.exception;
import com.zhibinwang.constants.Constants;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.zhibinwang.base.BaseApiService;
import com.zhibinwang.base.BaseResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * @author 花开
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends BaseApiService<JSONObject> {
	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public BaseResponse<JSONObject> exceptionHandler(Exception e) {
		log.error("###全局捕获异常##", e);
		return setResultError("系统错误!");
	}

	@ExceptionHandler(BindException.class)
	@ResponseBody
	public BaseResponse<JSONObject> handler(BindException ex){
		StringBuilder sb = new StringBuilder();
		FieldError fieldError = ex.getFieldError();
		sb.append(fieldError.getDefaultMessage());

		return setResultError(Constants.HTTP_RES_CODE_PARAMETERROR_204,sb.toString());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public BaseResponse<JSONObject> handler(ConstraintViolationException ex){
		StringBuilder sb = new StringBuilder();
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		for (ConstraintViolation<?> constraintViolation : constraintViolations) {
			sb.append(constraintViolation.getMessage());
		}
		System.out.println(sb.toString());
		return setResultError(Constants.HTTP_RES_CODE_PARAMETERROR_204,sb.toString());
	}
}
