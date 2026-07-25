package bg.softuni.pizza_delivery_application.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(LoggingAspect.class);


    @Around("within(bg.softuni.pizza_delivery_application.service.impl..*)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {

        String className =
                joinPoint.getSignature()
                        .getDeclaringType()
                        .getSimpleName();

        String methodName =
                joinPoint.getSignature()
                        .getName();

        long startTime = System.currentTimeMillis();

        LOGGER.info(
                "Executing service method: {}.{}",
                className,
                methodName
        );

        try {
            Object result = joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis() - startTime;

            LOGGER.info(
                    "Service method completed: {}.{}, executionTime={} ms",
                    className,
                    methodName,
                    executionTime
            );

            return result;

        } catch (Throwable exception) {

            long executionTime =
                    System.currentTimeMillis() - startTime;

            LOGGER.error(
                    "Service method failed: {}.{}, executionTime={} ms, reason={}",
                    className,
                    methodName,
                    executionTime,
                    exception.getMessage()
            );

            throw exception;
        }
    }
}