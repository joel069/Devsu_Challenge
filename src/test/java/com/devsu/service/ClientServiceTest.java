package com.devsu.service;

import Customer.dto.ResponseData;
import Customer.dto.client.UserDniDTO;
import Customer.enums.SuccessCodesEnum;
import Customer.enums.SuccessMessagesEnum;
import Customer.exceptions.CustomValidationException;
import Customer.exceptions.GlobalExceptionHandler;
import Customer.model.T_Person;
import Customer.repository.PersonRepository;
import Customer.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private ClientServiceImpl clientService;


    @Test
    void testDeleteClient_success() throws CustomValidationException {
        String dni = "0102030405";
        T_Person person = new T_Person();
        person.setPersonDni(dni);
        when(personRepository.findByPersonDni(dni)).thenReturn(Optional.of(person));

        UserDniDTO userDniDTO = new UserDniDTO(dni);
        ResponseData response = clientService.deleteClient(userDniDTO);

        assertNotNull(response);
        assertEquals(SuccessMessagesEnum.SUCCESSFULLY_DELETED.getMessage(), response.getMessage());
        assertEquals(SuccessCodesEnum.SUCCESS_CODE.getMessage(), response.getCode());
        assertEquals(SuccessMessagesEnum.STATUS_OK.getMessage(), response.getStatus());

        verify(personRepository, times(1)).findByPersonDni(dni);
        verify(personRepository, times(1)).delete(person);
    }

    @Test
    void testDeleteClient_clientNotFound() {
        String dni = "0102030405";
        when(personRepository.findByPersonDni(dni)).thenReturn(Optional.empty());

        UserDniDTO userDniDTO = new UserDniDTO(dni);
        assertThrows(GlobalExceptionHandler.clientNotFoundException().getClass(), () -> {
            clientService.deleteClient(userDniDTO);
        });

        verify(personRepository, times(1)).findByPersonDni(dni);
        verify(personRepository, never()).delete(any());
    }


}
