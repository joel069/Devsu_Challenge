package Customer.repository;

import Customer.model.T_Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<T_Account, Integer> {


    Optional<T_Account> findByAccountNumber(String accountNumber);

    @Query("SELECT a FROM T_Account a WHERE a.client.personDni = ?1")
    List<T_Account> findByPersonDni(String clientDni);

    List<T_Account> findByClientPersonDni(String clientDni);
}
