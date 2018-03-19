package com.epam.health.tool.aspect;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by Vasilina_Terehova on 3/7/2018.
 */
public class AuthenticationAspect {
    @Pointcut("execution(* com.epam.health_tool.controller.ClusterHealthCheckService.greeting())")
    public void addAuthenticaiton() {
        //todo: get cluster name, make authentication according to it
    }
}
