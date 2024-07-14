package Customer.repository;

import Customer.model.T_Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<T_Client, Integer> {




}
