package edu.epam.izhevsk.junit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.AdditionalMatchers.leq;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;

public class PaymentControllerTest {
    @Mock
    AccountService accountService;
    @Mock
    DepositService depositService;
    @InjectMocks
    PaymentController controller;

    @Before
    public void init() throws InsufficientFundsException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(depositService.deposit(gt(100L) ,Mockito.anyLong())).thenThrow(new InsufficientFundsException());
        Mockito.when(accountService.isUserAuthenticated(100L)).thenReturn(true);
//        Mockito.when(depositService.deposit(leq(100L),anyLong())).thenAnswer();
    }

    @Test
    public void testDeposit() {
        Assertions.assertDoesNotThrow(() -> controller.deposit(50L, 100L));
        verify(accountService).isUserAuthenticated(100L);
    }

    @Test
    public void testDepositSecurity() {
        Assert.assertThrows(SecurityException.class, () -> controller.deposit(100L, 101L));
    }

    @Test
    public void testDepositAmount() {
        Assert.assertThrows(InsufficientFundsException.class, () -> controller.deposit(500L, 100L));
    }

}
