package Customer.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MovementDetailDTO {
    private String movementDate;
    private String movementType;
    private BigDecimal movementAmount;
    private BigDecimal movementBalance;
}
