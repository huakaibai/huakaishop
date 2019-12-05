package com.zhibinwang.core.validate;

import org.springframework.util.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author 花开
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = Phone.PhoneValidator.class)
public @interface Phone {

    // 这个地方修改错误提示字符，其他地方不要修改
    String message() default "手机号码格式错误";

    Class<?>[] groups() default { }; // 用于分组校验

    Class<? extends Payload>[] payload() default { };


//自定义实现类
    class PhoneValidator implements ConstraintValidator<Phone, String> {
      static final  String regex = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            if (StringUtils.isEmpty(s)){
                return  false;
            }

         	return Pattern.matches(regex, s);

        }
    }
}
