package Customer.service;


import Customer.dto.account.AccountDTO;
import Customer.dto.account.AccountNumberDTO;
import Customer.dto.ResponseData;
import Customer.dto.account.SaveAccountDTO;
import Customer.exceptions.CustomValidationException;

import java.util.List;
import java.util.Map;

public interface AccountService {

    List<AccountDTO> findAllAccounts();
    AccountDTO getAccountByNumber(AccountNumberDTO accountNumber);
    ResponseData saveAccount(SaveAccountDTO accountDTO) throws CustomValidationException;
    ResponseData updateClient(Integer accountId, Map<String, Object> fields) throws CustomValidationException;
    ResponseData deleteAccount(AccountNumberDTO accountNumberDTO) throws CustomValidationException;
    ResponseData updateAccountAllData(AccountDTO accountDTO) throws CustomValidationException;
}
