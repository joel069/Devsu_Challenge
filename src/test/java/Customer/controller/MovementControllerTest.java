package Customer.controller;

import Customer.dto.ResponseData;
import Customer.dto.account.SaveAccountDTO;
import Customer.dto.client.ClientDTO;
import Customer.dto.movement.MovementRequestDTO;
import Customer.enums.SuccessMessagesEnum;
import Customer.exceptions.CustomValidationException;
import Customer.exceptions.GlobalExceptionHandler;
import Customer.model.T_Account;
import Customer.model.T_Movement;
import Customer.model.T_Person;
import Customer.repository.AccountRepository;
import Customer.repository.ClientRepository;
import Customer.repository.MovementRepository;
import Customer.repository.PersonRepository;
import Customer.service.AccountService;
import Customer.service.ClientService;
import Customer.service.MovementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovementControllerTest {

    @Autowired
    private MovementService movementService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MovementRepository movementRepository;


    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        movementRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void testHandleMovement() throws CustomValidationException {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setPersonDni("0123456789");
        clientDTO.setPersonName("Juanito  Perez");
        clientDTO.setClientState(true);
        clientDTO.setClientPassword("1234");
        clientDTO.setPersonGenre("MASCULINO");
        clientDTO.setPersonAddress("EEUU");
        clientDTO.setPersonAge(30);
        clientDTO.setPersonPhoneNumber("0962565129");
        clientService.saveClient(clientDTO);

        T_Person savedClient = personRepository.findByPersonDni(clientDTO.getPersonDni())
                .orElseThrow(GlobalExceptionHandler::clientNotFoundException);
        Integer clientId = savedClient.getPersonId();

        SaveAccountDTO accountDTO = new SaveAccountDTO();
        accountDTO.setClienteId(clientId);
        accountDTO.setNumeroCuenta("1");
        accountDTO.setSaldoInicial(BigDecimal.valueOf(1000));
        accountDTO.setTipoCuenta("AHORROS");
        accountDTO.setEstado(true);

        MovementRequestDTO movementRequestDTO = new MovementRequestDTO();
        movementRequestDTO.setAccountNumber("1");
        movementRequestDTO.setValue(BigDecimal.valueOf(1000));
        movementRequestDTO.setMovementType("DEPOSITO");
        accountService.saveAccount(accountDTO);

        ResponseData responseData = movementService.handleMovement(movementRequestDTO);

        assertThat(responseData).isNotNull();
        assertThat(responseData.getMessage()).isEqualTo(SuccessMessagesEnum.TRANSACTION_SUCCESSFUL.getMessage());

        List<T_Movement> movements = movementRepository.findAll();
        T_Movement savedMovement = movements.get(0);
        assertThat(savedMovement.getMovementType()).isEqualTo("DEPOSITO");
        assertThat(savedMovement.getMovementAmount()).isEqualByComparingTo(BigDecimal.valueOf(1000));
        assertThat(savedMovement.getMovementBalance()).isEqualByComparingTo(BigDecimal.valueOf(2000));

        T_Account savedAccount = accountRepository.findByAccountNumber("1").orElseThrow();
        assertThat(savedAccount.getAccountBalance()).isEqualByComparingTo(BigDecimal.valueOf(2000));
    }
}