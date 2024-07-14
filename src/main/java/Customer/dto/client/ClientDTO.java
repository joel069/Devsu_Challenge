package Customer.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class ClientDTO {


    private Integer personId;
    @NotBlank(message = "La cedula de la persona no puede ser nula o vacia")
    private String personDni;
    @NotBlank(message = "El nombre de la persona no puede ser nulo o vacio")
    private String personName;
    @NotBlank(message = "El genero de la persona no puede ser nulo o vacio")
    private String personGenre;
    @NotNull(message = "La edad de la persona no puede ser nula")
    private Integer personAge;
    @NotBlank(message = "La direccion de la persona no puede ser nula o vacia")
    private String personAddress;
    @NotBlank(message = "El numero de telefono de la persona no puede ser nulo o vacio")
    private String personPhoneNumber;
    @NotNull(message = "El estado del cliente no puede ser nulo")
    private Boolean clientState;
    @NotBlank(message = "La contraseña del cliente no puede ser nula o vacia")
    private String clientPassword;

}
