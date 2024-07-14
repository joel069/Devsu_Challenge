package Customer.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "T_Account")
public class T_Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_account_seq")
    @SequenceGenerator(name = "t_account_seq", sequenceName = "t_account_seq", allocationSize = 1)
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_type", nullable = false)
    private String accountType;

    @Column(name = "account_balance", nullable = false)
    private BigDecimal accountBalance;

    @Column(name = "account_state")
    private Boolean accountState;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = T_Client.class)
    @JoinColumn(name = "account_client_id", referencedColumnName = "per_id")
    private T_Client client;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, orphanRemoval = true)
    @ToString.Exclude
    private List<T_Movement> movements;


}
