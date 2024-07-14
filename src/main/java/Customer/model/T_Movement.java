package Customer.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_Movement")
public class T_Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_movement_seq")
    @SequenceGenerator(name = "t_movement_seq", sequenceName = "t_movement_seq", allocationSize = 1)
    @Column(name = "movement_id")
    private Integer movementId;

    @Column(name = "movement_date", nullable = false)
    private LocalDateTime movementDate;

    @Column(name = "movement_type", nullable = false)
    private String movementType;

    @Column(name = "movement_amount", nullable = false)
    private BigDecimal movementAmount;

    @Column(name = "movement_balance", nullable = false)
    private BigDecimal movementBalance;

    @Column(name = "movement_state", nullable = false)
    private Boolean movementState;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = T_Account.class)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private T_Account account;
}
