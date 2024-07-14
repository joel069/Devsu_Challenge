package Customer.service.impl;

import Customer.dto.ResponseData;
import Customer.dto.account.AccountNumberDTO;
import Customer.dto.client.UserDniDTO;
import Customer.dto.movement.MovementDTO;
import Customer.dto.movement.MovementId;
import Customer.dto.movement.MovementRequestDTO;
import Customer.dto.reports.AccountReportDTO;
import Customer.dto.reports.ClientReportDTO;
import Customer.dto.reports.MovementDetailDTO;
import Customer.dto.reports.ReportRequestDTO;
import Customer.enums.MovementsTypeEnum;
import Customer.enums.SuccessCodesEnum;
import Customer.enums.SuccessMessagesEnum;
import Customer.exceptions.CustomValidationException;
import Customer.exceptions.GlobalExceptionHandler;
import Customer.model.T_Account;
import Customer.model.T_Movement;
import Customer.model.T_Person;
import Customer.repository.AccountRepository;
import Customer.repository.MovementRepository;
import Customer.repository.PersonRepository;
import Customer.service.MovementService;
import Customer.utils.Utils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovementServiceImpl implements MovementService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;
    private final PersonRepository personRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public MovementServiceImpl(AccountRepository accountRepository, MovementRepository movementRepository, PersonRepository personRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
        this.personRepository = personRepository;
    }

    public List<MovementDTO> findMovementByAccountNumber(AccountNumberDTO accountNumberDTO) {

        Optional<T_Account> account = accountRepository.findByAccountNumber(accountNumberDTO.getAccountNumber());

        if (account.isEmpty()) {
            throw GlobalExceptionHandler.AccountNotFoundException();
        }
        List<T_Movement> movements = movementRepository.findByAccountAccountNumber(accountNumberDTO.getAccountNumber());
        if (movements.isEmpty()) {
            throw GlobalExceptionHandler.MovementsNotFoundException();
        }
        return getMovementDTOS(movements);
    }

    public List<MovementDTO> findAll() throws CustomValidationException {
        List<T_Movement> movements = movementRepository.findAll();
        if (movements.isEmpty()) {
            throw GlobalExceptionHandler.movementsNotExistException();
        }
        return getMovementDTOS(movements);
    }


    public List<MovementDTO> findMovementByDni(UserDniDTO userDniDTO) throws CustomValidationException {
        Optional<T_Person> personExist = personRepository.findByPersonDni(userDniDTO.getUserDni());

        if (personExist.isEmpty()) {
            throw GlobalExceptionHandler.clientNotFoundException();
        }
        List<T_Movement> movements = movementRepository.findByAccountClientPersonDni(userDniDTO.getUserDni());
        if (movements.isEmpty()) {
            throw GlobalExceptionHandler.MovementsNotFoundException();
        }
        return getMovementDTOS(movements);
    }

    @Transactional
    public ResponseData handleMovement(MovementRequestDTO movementDTO) throws CustomValidationException {

        T_Account account = accountRepository.findByAccountNumber(movementDTO.getAccountNumber())
                .orElseThrow(GlobalExceptionHandler::AccountNotFoundException);

        BigDecimal currentBalance = account.getAccountBalance();
        BigDecimal updatedBalance;

        if (movementDTO.getMovementType().equalsIgnoreCase(MovementsTypeEnum.DEPOSIT.getMessage())) {
            updatedBalance = currentBalance.add(movementDTO.getValue());
        } else if (movementDTO.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
            BigDecimal balanceAfterWithdrawal = currentBalance.subtract(movementDTO.getValue());
            if (balanceAfterWithdrawal.compareTo(BigDecimal.ZERO) < 0) {
                throw GlobalExceptionHandler.insufficientBalanceException();
            }
            updatedBalance = balanceAfterWithdrawal;
        } else {
            throw GlobalExceptionHandler.invalidMovementTypeException();
        }

        T_Movement movement = new T_Movement(
                null,
                LocalDateTime.now(),
                movementDTO.getMovementType(),
                movementDTO.getValue(),
                updatedBalance,
                true,
                account
        );
        movementRepository.save(movement);
        account.setAccountBalance(updatedBalance);
        accountRepository.save(account);
        return new ResponseData(
                SuccessMessagesEnum.TRANSACTION_SUCCESSFUL.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

    private List<MovementDTO> getMovementDTOS(List<T_Movement> movements) {
        Map<T_Account, List<T_Movement>> movementsByAccount = movements.stream()
                .collect(Collectors.groupingBy(T_Movement::getAccount));

        return movements.stream()
                .map(movement -> {
                    MovementDTO dto = modelMapper.map(movement, MovementDTO.class);
                    dto.setPersonDni(movement.getAccount().getClient().getPersonDni());
                    dto.setPersonName(movement.getAccount().getClient().getPersonName());
                    dto.setAccountType(movement.getAccount().getAccountType());
                    dto.setMovementDate(Utils.convertLocalDateTimeToString(movement.getMovementDate()));
                    if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
                        dto.setMovementAmount(movement.getMovementAmount().negate());
                    } else {
                        dto.setMovementAmount(movement.getMovementAmount());
                    }
                    BigDecimal initialBalance = calculateInitialBalance(movementsByAccount.get(movement.getAccount()), movement);
                    BigDecimal availableBalance = calculateAvailableBalance(movementsByAccount.get(movement.getAccount()), movement);
                    dto.setInitialBalance(initialBalance);
                    dto.setAvailableBalance(availableBalance);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateInitialBalance(List<T_Movement> accountMovements, T_Movement currentMovement) {
        BigDecimal initialBalance = BigDecimal.ZERO;

        for (T_Movement movement : accountMovements) {
            if (movement.equals(currentMovement)) {
                break;
            }
            if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.DEPOSIT.getMessage())) {
                initialBalance = initialBalance.add(movement.getMovementAmount());
            } else if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
                initialBalance = initialBalance.subtract(movement.getMovementAmount());
            }
        }
        return initialBalance;
    }

    private BigDecimal calculateAvailableBalance(List<T_Movement> accountMovements, T_Movement currentMovement) {
        BigDecimal availableBalance = BigDecimal.ZERO;

        for (T_Movement movement : accountMovements) {
            if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.DEPOSIT.getMessage())) {
                availableBalance = availableBalance.add(movement.getMovementAmount());
            } else if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
                availableBalance = availableBalance.subtract(movement.getMovementAmount());
            }
            if (movement.equals(currentMovement)) {
                break;
            }
        }
        return availableBalance;
    }

    public ResponseData updateMovement(Integer userId, Map<String, Object> fields) throws CustomValidationException {

        Optional<T_Movement> moves = movementRepository.findById(userId);
        if (moves.isEmpty()) {
            throw GlobalExceptionHandler.clientNotFoundException();
        }
        fields.forEach((key, value) -> {
            if (key.equals("movementDate")) {
                try {
                    throw GlobalExceptionHandler.fieldModifiedNotAllowed(key);
                } catch (CustomValidationException e) {
                    throw new RuntimeException(e);
                }
            }
            Field field = ReflectionUtils.findField(T_Movement.class, key);
            if (field != null) {
                if (field.getType().equals(BigDecimal.class)) {
                    value = new BigDecimal(value.toString());
                }
                field.setAccessible(true);
                ReflectionUtils.setField(field, moves.get(), value);
            } else {
                throw GlobalExceptionHandler.fieldNotFoundException(key);
            }
        });
        movementRepository.save(moves.get());
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_UPDATED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );

    }


    public ClientReportDTO generateReport(ReportRequestDTO reportRequestDTO) {
        List<T_Account> accounts = accountRepository.findByClientPersonDni(reportRequestDTO.getClientDni());

        List<AccountReportDTO> accountReports = accounts.stream().map(account -> {
            List<T_Movement> movements = movementRepository.findByAccountAndMovementDateBetween(
                    account,
                    reportRequestDTO.getInitialDate(),
                    reportRequestDTO.getFinalDate());

            List<MovementDetailDTO> movementDetails = movements.stream().map(movement -> {
                BigDecimal movementAmount = movement.getMovementAmount();
                if (movement.getMovementType().equalsIgnoreCase(MovementsTypeEnum.WITHDRAWAL.getMessage())) {
                    movementAmount = movementAmount.negate();
                }

                return new MovementDetailDTO(
                        Utils.convertLocalDateTimeToString(movement.getMovementDate()),
                        movement.getMovementType(),
                        movementAmount,
                        movement.getMovementBalance()
                );
            }).collect(Collectors.toList());

            return new AccountReportDTO(
                    account.getAccountNumber(),
                    account.getAccountType(),
                    account.getAccountBalance(),
                    movementDetails
            );
        }).collect(Collectors.toList());

        return new ClientReportDTO(
                accounts.get(0).getClient().getPersonDni(),
                accounts.get(0).getClient().getPersonName(),
                accountReports
        );
    }

    public ResponseData deleteMovement(MovementId movementId) {
        T_Movement movement = movementRepository.findById(movementId.getMovementId())
                .orElseThrow(GlobalExceptionHandler::MovementsNotFoundException);

        movementRepository.delete(movement);
        return new ResponseData(
                SuccessMessagesEnum.SUCCESSFULLY_DELETED.getMessage(),
                SuccessCodesEnum.SUCCESS_CODE.getMessage(),
                SuccessMessagesEnum.STATUS_OK.getMessage()
        );
    }

}
