package Customer.repository;

import Customer.model.T_Account;
import Customer.model.T_Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<T_Movement, Integer>{

    List<T_Movement> findByAccountAccountNumber(String accountNumber);

    @Query("SELECT m FROM T_Movement m WHERE m.account.client.personDni = ?1")
    List<T_Movement> findByAccountClientPersonDni(String userDni);

    @Query("SELECT m FROM T_Movement m WHERE m.account = ?1 AND m.movementDate BETWEEN ?2 AND ?3")
    List<T_Movement> findByAccountAndMovementDateBetween(T_Account account, LocalDateTime startDate, LocalDateTime endDate);
}
