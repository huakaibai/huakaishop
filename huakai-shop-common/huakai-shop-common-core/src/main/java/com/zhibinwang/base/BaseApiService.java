package com.zhibinwang.base;

import com.zhibinwang.constants.Constants;
import com.zhibinwang.core.bin.HuakaiBeanUtils;
import javafx.scene.Parent;
import org.springframework.stereotype.Component;


import lombok.Data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
*/
@Data

public class BaseApiService<T> {
	
	
	private Class<T>  clazz;


	// 获取子类传递给他的具体泛型类型
	public BaseApiService() {
		// 1获取子类的class(在创建子类对象的时候,会返回父类的构造方法)
		Class<T> clazze = (Class<T>) this.getClass();
		// 2获取当前类的带有泛型的父类类型
		ParameterizedType type = (ParameterizedType) clazze.getGenericSuperclass();
		// 3返回实际参数类型(泛型可以写多个)
		Type[] types = type.getActualTypeArguments();
		// 4 获取第一个参数(泛型的具体类) Person.class
		clazz = (Class<T>) types[0];
	}


	public BaseResponse<T> setResultError(Integer code, String msg) {
		return setResult(code, msg, null);
	}

	// 返回错误，可以传msg
	public BaseResponse<T> setResultError(String msg) {
		return setResult(Constants.HTTP_RES_CODE_500, msg, null);
	}

	// 返回成功，可以传data值
	public BaseResponse<T> setResultSuccess(T data) {
		
		
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
	}

	public BaseResponse<T> setResultSuccessByT(Object data) {

		T t = HuakaiBeanUtils.doToDto(data, clazz);
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, t);
	}

	// 返回成功，沒有data值
	public BaseResponse<T> setResultSuccess() {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
	}

	// 返回成功，沒有data值
	public BaseResponse<T>  setResultSuccess(String msg) {
		return setResult(Constants.HTTP_RES_CODE_200, msg, null);
	}

	// 通用封装
	public BaseResponse<T> setResult(Integer code, String msg, T data) {
		return new BaseResponse<T>(code, msg, data);
	}

	public Boolean toDbResult(int result){
		return result>0?true:false;
	}

	public Boolean isSuccess(BaseResponse<?> baseResp) {
		if (baseResp == null) {
			return false;
		}
		if (!baseResp.getCode().equals(Constants.HTTP_RES_CODE_200)) {
			return false;
		}
		return true;
	}
}
