package com.epam.health.tool.facade.common.resolver.impl;

import com.epam.health.tool.model.ClusterTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ClusterSpecificComponent {
    ClusterTypeEnum value() default ClusterTypeEnum.NONE;
}
