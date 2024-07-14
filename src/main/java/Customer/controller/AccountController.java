package Customer.controller;

import Customer.dto.account.AccountDTO;
import Customer.dto.account.AccountNumberDTO;
import Customer.dto.ResponseData;
import Customer.dto.account.SaveAccountDTO;
import Customer.exceptions.CustomValidationException;
import Customer.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devsu/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/findAllAccounts")
    public ResponseEntity<List<AccountDTO>> getAllClients() {
        List<AccountDTO> clients = accountService.findAllAccounts();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/getAccountByNumber")
    public ResponseEntity<AccountDTO> getAccountByNumber(AccountNumberDTO accountNumber) {
        AccountDTO account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/saveAccount")
    public ResponseEntity<ResponseData> saveAccount(@RequestBody @Valid SaveAccountDTO accountDTO) throws CustomValidationException {
        ResponseData account = accountService.saveAccount(accountDTO);
        return ResponseEntity.ok(account);
    }

    @PatchMapping("/updateAccount/{accountId}")
    public ResponseEntity<ResponseData> updateClient(@PathVariable Integer accountId, @RequestBody Map<String, Object> fields) throws CustomValidationException {
        ResponseData account = accountService.updateClient(accountId, fields);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/updateAccountAllData")
    public ResponseEntity<ResponseData> updateAccountAllData(@RequestBody @Valid AccountDTO accountDTO) throws CustomValidationException {
        ResponseData account = accountService.updateAccountAllData(accountDTO);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<ResponseData> deleteAccount(@RequestBody @Valid AccountNumberDTO accountNumberDTO) throws CustomValidationException {
        ResponseData account = accountService.deleteAccount(accountNumberDTO);
        return ResponseEntity.ok(account);
    }
    
}
