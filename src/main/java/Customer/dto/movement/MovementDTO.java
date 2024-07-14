package Customer.dto.movement;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovementDTO {
    private String personDni;
    private String personName;
    private String accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private String movementType;
    private BigDecimal movementAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String movementDate;
    private BigDecimal availableBalance;
}
