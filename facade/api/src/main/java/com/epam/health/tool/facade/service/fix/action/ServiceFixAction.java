package com.epam.health.tool.facade.service.fix.action;

import com.epam.health.tool.model.ServiceTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceFixAction {
    ServiceTypeEnum value() default ServiceTypeEnum.UNDEFINED;
}
