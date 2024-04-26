package com.project.harupuppy.global.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = KoreanNickNameValidator.class)
public @interface KoreanNickname {
    String message() default "닉네임은 한글 8자리로만 설정할 수 있습니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

