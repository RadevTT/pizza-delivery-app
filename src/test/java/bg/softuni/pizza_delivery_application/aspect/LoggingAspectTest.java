package bg.softuni.pizza_delivery_application.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    private LoggingAspect loggingAspect;

    @BeforeEach
    void setUp() {
        loggingAspect = new LoggingAspect();

        when(joinPoint.getSignature())
                .thenReturn(signature);

        when(signature.getDeclaringType())
                .thenReturn(TestService.class);

        when(signature.getName())
                .thenReturn("testMethod");
    }

    @Test
    void logExecutionTime_WhenMethodSucceeds_ShouldReturnResult()
            throws Throwable {

        Object expectedResult = "success";

        when(joinPoint.proceed())
                .thenReturn(expectedResult);

        Object actualResult =
                loggingAspect.logExecutionTime(joinPoint);

        assertEquals(expectedResult, actualResult);

        verify(joinPoint).proceed();
    }

    @Test
    void logExecutionTime_WhenMethodReturnsObject_ShouldReturnSameObject()
            throws Throwable {

        Object expectedResult = new Object();

        when(joinPoint.proceed())
                .thenReturn(expectedResult);

        Object actualResult =
                loggingAspect.logExecutionTime(joinPoint);

        assertSame(expectedResult, actualResult);

        verify(joinPoint).proceed();
    }

    @Test
    void logExecutionTime_WhenMethodThrowsException_ShouldRethrowIt()
            throws Throwable {

        IllegalStateException expectedException =
                new IllegalStateException("Test failure");

        when(joinPoint.proceed())
                .thenThrow(expectedException);

        IllegalStateException actualException =
                assertThrows(
                        IllegalStateException.class,
                        () -> loggingAspect.logExecutionTime(joinPoint)
                );

        assertSame(expectedException, actualException);

        verify(joinPoint).proceed();
    }

    private static class TestService {
    }
}