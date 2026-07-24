package com.fengshui.annotation;

import com.fengshui.enums.UserRole;
import java.lang.annotation.*;

/**
 * Annotation để chỉ định các role được phép truy cập method/controller
 * Ví dụ: @RequireRole({UserRole.ADMIN, UserRole.WAREHOUSE})
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireRole {
    UserRole[] value() default {};
}
