package de.telran.mycryptowallet.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author Alexander Isai on 03.02.2024.
 */
@Component
@Aspect
public class ValidationAspect {

    @Before("@annotation(validate)")
    public void validate(JoinPoint joinPoint, Validate validate){

    }
}
