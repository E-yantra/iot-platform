package org.kyantra.interfaces;

import org.kyantra.beans.RoleEnum;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secure {
    RoleEnum[] roles() default RoleEnum.READ;
    String subjectField() default "id";
    String subjectType() default "unit";
}