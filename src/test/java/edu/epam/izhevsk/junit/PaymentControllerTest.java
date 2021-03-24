package edu.epam.izhevsk.junit;

import org.hamcrest.CustomMatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.mockito.AdditionalMatchers.*;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.regex.Matcher;

import static org.mockito.Mockito.*;

public class PaymentControllerTest {
    @InjectMocks
    AccountService accountService = Mockito.mock(AccountService.class);
    DepositService depositService = Mockito.mock(DepositService.class);
    PaymentController controller = new PaymentController(accountService, depositService);

    @Before
    public void setMocks(){
        Mockito.when(accountService.isUserAuthenticated(not(eq(100L)))).thenReturn(false);
        Mockito.when(accountService.isUserAuthenticated(100L)).thenReturn(true);
        try {
            Mockito.when(depositService.deposit(gt(100L) ,Mockito.anyLong())).thenThrow(new InsufficientFundsException());
        } catch (InsufficientFundsException e) {
            Assert.fail("depositService.deposit throw an exception");
        }
    }

    @Test
    public void testDeposit() {
        try {
            controller.deposit(50L,100L);
        }catch (SecurityException | InsufficientFundsException e){
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testDepositSecurity(){
        try {
            controller.deposit(50L,10L);
            Assert.fail("Must be SecurityException");
        }catch (SecurityException | InsufficientFundsException e){

        }
    }

    @Test
    public void testDepositAmount(){
        try {
            controller.deposit(500L,100L);
            Assert.fail("Must be InsufficientFundsException");
        }catch (InsufficientFundsException e){

        }
    }
}
