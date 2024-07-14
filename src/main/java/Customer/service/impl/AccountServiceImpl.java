package Customer.service.impl;

import Customer.dto.*;
import Customer.dto.account.AccountDTO;
import Customer.dto.account.AccountNumberDTO;
import Customer.dto.account.SaveAccountDTO;
import Customer.enums.SuccessCodesEnum;
import Customer.enums.SuccessMessagesEnum;
import Customer.exceptions.CustomValidationException;
import Customer.exceptions.GlobalExceptionHandler;
import Customer.model.T_Account;
import Customer.model.T_Client;
import Customer.model.T_Person;
import Customer.repository.AccountRepository;
import Customer.repository.ClientRepository;
import Customer.repository.PersonRepository;
import Customer.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PersonRepository personRepository;
    private final ClientRepository clientRepository;

    public AccountServiceImpl(AccountRepository accountRepository, PersonRepository personRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.personRepository = personRepository;
        this.clientRepository = clientRepository;
    }

    public List<AccountDTO> findAllAccounts() {
        List<T_Account> accounts = accountRepository.findAll();
        if (accounts != null && !accounts.isEmpty()) {
            return accounts.stream()
                    .map(account -> new AccountDTO(
                            account.getAccountNumber(),
                            account.getAccountType(),
                            account.getAccountBalance(),
                            account.getClient().getPersonDni(),
                            account.getAccountState()))
                    .collect(Collectors.toList());
        } else {
            throw GlobalExceptionHandler.AccountsNotFoundException();
        }
    }


    public AccountDTO getAccountByNumber(AccountNumberDTO accountNumber) {
        T_Account account = accountRepository.findByAccountNumber(accountNumber.getAccountNumber()).orElseThrow(
                GlobalExceptionHandler::AccountNotFoundException
        );
        if (account != null) {
            return new AccountDTO(
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getAccountBalance(),
                    account.getClient().getPersonDni(),
                    account.getAccountState());
        } else {
            throw GlobalExceptionHandler.AccountNotFoundException();
        }
    }

    public ResponseData saveAccount(SaveAccountDTO accountDTO) throws CustomValidationException {
        Optional<T_Client> client = clientRepository.findById(accountDTO.getClienteId());
        if (client.isEmpty()) {
            throw GlobalExceptionHandler.clientNotFoundException();
        }
        Optional<T_Account> accountExist = accountRepository.findByAccountNumber(accountDTO.getNumeroCuenta());
        if (accountExist.isPresent()) {
            throw GlobalExceptionHandler.accountAlreadyExistException();
        }
        T_Account account = new T_Account();
        account.setAccountNumber(accountDTO.getNumeroCuenta());
        account.setAccountType(accountDTO.getTipoCuenta());
        account.setAccountBalance(accountDTO.getSaldoInicial());
        account.setAccountState(accountDTO.getEstado());
        account.setClient(client.get());
        accountRepository.save(account);
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_CREATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData updateClient(Integer accountId, Map<String, Object> fields) throws CustomValidationException {
        Optional<T_Account> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw GlobalExceptionHandler.clientNotFoundException();
        }
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(T_Account.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, account.get(), value);
            } else {
                throw GlobalExceptionHandler.fieldNotFoundException(key);
            }
        });
        accountRepository.save(account.get());
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData updateAccountAllData(AccountDTO accountDTO) throws CustomValidationException {
        Optional<T_Person> existingPersonOpt = personRepository.findByPersonDni(accountDTO.getClienteCedula());

        if (existingPersonOpt.isEmpty()) {
            throw GlobalExceptionHandler.clientNotFoundException();
        }

        Optional<T_Account> accountExist = accountRepository.findByAccountNumber(accountDTO.getNumeroCuenta());
        if (accountExist.isPresent()) {
            throw GlobalExceptionHandler.accountAlreadyExistException();
        }

        T_Account existingAccount = new T_Account();
        existingAccount.setAccountNumber(accountDTO.getNumeroCuenta());
        existingAccount.setAccountType(accountDTO.getTipoCuenta());
        existingAccount.setAccountBalance(accountDTO.getSaldoInicial());
        existingAccount.setAccountState(accountDTO.getEstado());
        existingAccount.setClient((T_Client) existingPersonOpt.get());
        accountRepository.save(existingAccount);

        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    public ResponseData deleteAccount(AccountNumberDTO accountNumberDTO) throws CustomValidationException {
        Optional<T_Account> accountExist = accountRepository.findByAccountNumber(accountNumberDTO.getAccountNumber());
        if (accountExist.isEmpty()) {
            throw GlobalExceptionHandler.AccountNotFoundException();
        }

        accountRepository.delete(accountExist.get());
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_DELETED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }
}
