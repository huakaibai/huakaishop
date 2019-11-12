package com.zhibinwang.core.validate;

import com.zhibinwang.core.token.LoginType;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 花开
 * @create 2019-11-08 23:13
 * @desc 登陆类型校验
 **/
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = VaLoginType.LoginTypeValidator.class)
public @interface VaLoginType {

    // 这个地方修改错误提示字符，其他地方不要修改
    String message() default "登陆类型不合法";

    Class<?>[] groups() default { }; // 用于分组校验

    Class<? extends Payload>[] payload() default { };

    class LoginTypeValidator implements ConstraintValidator<VaLoginType, String> {

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {

            if (StringUtils.isEmpty(value)){
                return  false;
            }
            // 判断登陆类型是否在枚举值中
            boolean validEnum = EnumUtils.isValidEnum(LoginType.class, value);
            return validEnum;
        }
    }
}
