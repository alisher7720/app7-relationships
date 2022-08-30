package uz.pdp.app7jparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.app7jparelationships.entity.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {

    boolean existsByName(String name);

}
