package Customer.repository;

import Customer.model.T_Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<T_Person, Integer> {

    Optional<T_Person> findByPersonDni(String personDni);
}
