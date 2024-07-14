package com.devsu.service;

import Customer.dto.ResponseData;
import Customer.dto.movement.MovementRequestDTO;
import Customer.enums.ErrorMessagesEnum;
import Customer.enums.MovementsTypeEnum;
import Customer.enums.SuccessCodesEnum;
import Customer.enums.SuccessMessagesEnum;
import Customer.exceptions.CustomValidationException;
import Customer.model.T_Account;
import Customer.repository.AccountRepository;
import Customer.repository.MovementRepository;
import Customer.service.impl.MovementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MovementRepository movementRepository;

    @InjectMocks
    private MovementServiceImpl movementService;

    private MovementRequestDTO depositRequest;
    private MovementRequestDTO withdrawalRequest;
    private T_Account account;

    @BeforeEach
    void setUp() {
        depositRequest = new MovementRequestDTO("123456789",
                BigDecimal.valueOf(100.00),
                MovementsTypeEnum.DEPOSIT.getMessage()
        );


        withdrawalRequest = new MovementRequestDTO("123456789",
                BigDecimal.valueOf(50.00),
                MovementsTypeEnum.WITHDRAWAL.getMessage()
        );

        account = new T_Account();
        account.setAccountNumber("123456789");
        account.setAccountBalance(BigDecimal.valueOf(500.00));

        when(accountRepository.findByAccountNumber("123456789")).thenReturn(Optional.of(account));

    }

    @Test
    void testHandleDeposit_success() throws CustomValidationException {
        ResponseData response = movementService.handleMovement(depositRequest);

        assertEquals(SuccessMessagesEnum.TRANSACTION_SUCCESSFUL.getMessage(), response.getMessage());
        assertEquals(SuccessCodesEnum.SUCCESS_CODE.getMessage(), response.getCode());
        assertEquals(SuccessMessagesEnum.STATUS_OK.getMessage(), response.getStatus());

        assertEquals(BigDecimal.valueOf(600.00), account.getAccountBalance()); // Updated account balance

        verify(accountRepository, times(1)).findByAccountNumber("123456789");
        verify(movementRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testHandleWithdrawal_success() throws CustomValidationException {
        ResponseData response = movementService.handleMovement(withdrawalRequest);

        assertEquals(SuccessMessagesEnum.TRANSACTION_SUCCESSFUL.getMessage(), response.getMessage());
        assertEquals(SuccessCodesEnum.SUCCESS_CODE.getMessage(), response.getCode());
        assertEquals(SuccessMessagesEnum.STATUS_OK.getMessage(), response.getStatus());

        assertEquals(BigDecimal.valueOf(450.00), account.getAccountBalance()); // Updated account balance

        verify(accountRepository, times(1)).findByAccountNumber("123456789");
        verify(movementRepository, times(1)).save(any());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testHandleWithdrawal_insufficientBalance() {
        withdrawalRequest.setValue(BigDecimal.valueOf(600.00));

        CustomValidationException exception = assertThrows(
                CustomValidationException.class,
                () -> movementService.handleMovement(withdrawalRequest)
        );

        assertEquals(ErrorMessagesEnum.INSUFFICIENT_BALANCE.getMessage(), exception.getMessage());

        verify(accountRepository, times(1)).findByAccountNumber("123456789");
        verify(movementRepository, never()).save(any());
        verify(accountRepository, never()).save(any());
    }


}
