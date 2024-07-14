package Customer.service;

import Customer.dto.ResponseData;
import Customer.dto.account.AccountNumberDTO;
import Customer.dto.client.UserDniDTO;
import Customer.dto.movement.MovementDTO;
import Customer.dto.movement.MovementId;
import Customer.dto.movement.MovementRequestDTO;
import Customer.dto.reports.ClientReportDTO;
import Customer.dto.reports.ReportRequestDTO;
import Customer.exceptions.CustomValidationException;

import java.util.List;
import java.util.Map;

public interface MovementService {

    List<MovementDTO> findMovementByAccountNumber(AccountNumberDTO accountNumberDTO);
    List<MovementDTO> findAll() throws CustomValidationException;
    List<MovementDTO> findMovementByDni(UserDniDTO userDniDTO) throws CustomValidationException;
    ResponseData handleMovement(MovementRequestDTO movementDTO) throws CustomValidationException;
    ClientReportDTO generateReport(ReportRequestDTO reportRequestDTO) throws CustomValidationException;
    ResponseData updateMovement(Integer userId, Map<String, Object> fields) throws CustomValidationException;
    ResponseData deleteMovement(MovementId movementId) throws CustomValidationException;


}
