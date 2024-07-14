package Customer.controller;

import Customer.dto.ResponseData;
import Customer.dto.account.AccountNumberDTO;
import Customer.dto.client.UserDniDTO;
import Customer.dto.movement.MovementDTO;
import Customer.dto.movement.MovementId;
import Customer.dto.movement.MovementRequestDTO;
import Customer.dto.reports.ClientReportDTO;
import Customer.dto.reports.ReportRequestDTO;
import Customer.exceptions.CustomValidationException;
import Customer.service.MovementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/devsu/movement")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @PostMapping("/getMovementByAccount")
    public ResponseEntity<List<MovementDTO>> getAccountByNumber(@RequestBody @Valid AccountNumberDTO accountNumber) {
        List<MovementDTO> account = movementService.findMovementByAccountNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/getAllMovements")
    public ResponseEntity<List<MovementDTO>> getAllMovements() throws CustomValidationException {
        List<MovementDTO> movements = movementService.findAll();
        return ResponseEntity.ok(movements);
    }

    @PostMapping("/getMovementByDni")
    public ResponseEntity<List<MovementDTO>> getMovementByDni(@RequestBody @Valid UserDniDTO userDniDTO) throws CustomValidationException {
        List<MovementDTO> movements = movementService.findMovementByDni(userDniDTO);
        return ResponseEntity.ok(movements);
    }

    @PostMapping("/makeMovement")
    public ResponseEntity<ResponseData> makeMovement(@RequestBody @Valid MovementRequestDTO movementDTO) throws CustomValidationException {
        ResponseData response = movementService.handleMovement(movementDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/generateReport")
    public ResponseEntity<ClientReportDTO> generateReport(@RequestBody @Valid ReportRequestDTO reportRequestDTO) throws CustomValidationException {
        ClientReportDTO report = movementService.generateReport(reportRequestDTO);
        return ResponseEntity.ok(report);
    }

    @PatchMapping("/updateMovement/{movementId}")
    public ResponseEntity<ResponseData> updateMovement(@PathVariable Integer movementId, @RequestBody Map<String, Object> fields) throws CustomValidationException {
        ResponseData response = movementService.updateMovement(movementId, fields);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/deleteMovement")
    public ResponseEntity<ResponseData> deleteMovement(@RequestBody MovementId movementId) throws CustomValidationException {
        ResponseData response = movementService.deleteMovement(movementId);
        return ResponseEntity.ok(response);
    }


}
