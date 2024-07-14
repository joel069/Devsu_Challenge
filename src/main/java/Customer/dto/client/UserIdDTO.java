package Customer.dto.client;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserIdDTO {
    @NotNull(message = "El id del usuario es requerido")
    private Integer userId;
}
