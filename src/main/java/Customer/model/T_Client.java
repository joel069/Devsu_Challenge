package Customer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "T_Client")
public class T_Client extends T_Person {


    @Column(name = "cli_state")
    private Boolean clientState;

    @Column(name = "cli_password", nullable = false)
    @NotNull(message = "La contrasenia es requerida")
    private String clientPassword;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, orphanRemoval = true)
    @ToString.Exclude
    private List<T_Account> accounts;

}
