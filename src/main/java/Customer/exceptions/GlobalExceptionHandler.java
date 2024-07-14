package Customer.exceptions;

import Customer.dto.ErrorMessageDTO;
import Customer.enums.ErrorCodesEnum;
import Customer.dto.ErrorValidationMessageDTO;
import Customer.enums.ErrorMessagesEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {




    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        ErrorValidationMessageDTO message = new ErrorValidationMessageDTO(HttpStatus.BAD_REQUEST, ErrorCodesEnum.BAD_REQUEST_CODE.getMessage(), errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(CustomValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessageDTO> handleCustomValidationException(CustomValidationException e) {
        ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.BAD_REQUEST, ErrorCodesEnum.BAD_REQUEST_CODE.getMessage(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessageDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorMessageDTO message = new ErrorMessageDTO(HttpStatus.NOT_FOUND, ErrorCodesEnum.NOT_FOUND_CODE.getMessage(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    public static CustomValidationException clientNotFoundException() {
        return new CustomValidationException(ErrorMessagesEnum.CLIENT_NOT_FOUND.getMessage());
    }

    public static CustomValidationException clientAlreadyExistException() {
        return new CustomValidationException(ErrorMessagesEnum.CLIENT_ALREADY_EXISTS.getMessage());
    }

    public static ResourceNotFoundException fieldNotFoundException(String fieldName) {
        return new ResourceNotFoundException(ErrorMessagesEnum.FIELD_NOT_FOUND.getMessage() + " Field: " + fieldName);
    }


    public static ResourceNotFoundException AccountsNotFoundException() {
        return new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNTS_NOT_FOUND.getMessage() );
    }

    public static ResourceNotFoundException AccountNotFoundException() {
        return new ResourceNotFoundException(ErrorMessagesEnum.ACCOUNT_NOT_FOUND.getMessage() );
    }


    public static CustomValidationException accountAlreadyExistException() {
        return new CustomValidationException(ErrorMessagesEnum.ACCOUNT_ALREADY_EXISTS.getMessage());
    }

    public static ResourceNotFoundException MovementsNotFoundException() {
        return new ResourceNotFoundException(ErrorMessagesEnum.MOVEMENTS_NOT_FOUND.getMessage() );
    }

    public static CustomValidationException movementsNotExistException() {
        return new CustomValidationException(ErrorMessagesEnum.MOVEMENTS_NOT_EXISTS.getMessage());
    }

    public static CustomValidationException insufficientBalanceException() {
        return new CustomValidationException(ErrorMessagesEnum.INSUFFICIENT_BALANCE.getMessage());
    }

    public static CustomValidationException invalidMovementTypeException() {
        return new CustomValidationException(ErrorMessagesEnum.INVALID_MOVEMENT_TYPE.getMessage());
    }

    public static CustomValidationException fieldModifiedNotAllowed(String field) {
        return new CustomValidationException(ErrorMessagesEnum.FIELD_MODIFIED_NOT_ALLOWED.getMessage() + " Field: " + field);
    }
}
