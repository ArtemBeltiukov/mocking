package edu.epam.izhevsk.junit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.eq;

public class PaymentControllerTest {
    @InjectMocks
    AccountService accountService = Mockito.mock(AccountService.class);
    DepositService depositService = Mockito.mock(DepositService.class);
    PaymentController controller = new PaymentController(accountService, depositService);

    @Before
    public void setMocks() throws InsufficientFundsException {
        Mockito.when(accountService.isUserAuthenticated(not(eq(100L)))).thenReturn(false);
        Mockito.when(accountService.isUserAuthenticated(100L)).thenReturn(true);
        Mockito.when(depositService.deposit(gt(100L) ,Mockito.anyLong())).thenThrow(new InsufficientFundsException());
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
