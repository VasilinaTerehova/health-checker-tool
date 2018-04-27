package com.epam.health.tool.facade.resolver.action;

import com.epam.facade.model.HealthCheckActionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HealthCheckAction {
    HealthCheckActionType value() default HealthCheckActionType.NONE;
}
