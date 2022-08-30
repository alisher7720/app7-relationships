package uz.pdp.app7jparelationships.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.app7jparelationships.entity.Group;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    boolean existsByNameAndFacultyId(String name, Integer faculty_id);

    boolean existsByNameAndFacultyIdAndIdNot(String name, Integer faculty_id, Integer id);

    // Get Groups For Faculty
    List<Group> findAllByFacultyId(Integer faculty_id);


    /*
    * Those methods are same methods (Get groups for university)
    * */

    // Jpa Query
    List<Group> findAllByFaculty_UniversityId(Integer faculty_university_id);

    // Not Native Query.
    @Query("select g from groups g where g.faculty.university.id=:universityId") // =: -> =?1
    List<Group> getGroupsByUniversityId(Integer universityId);

    @Query(value = "select * from groups g join faculty f on g.faculty_id = f.id " +
            "join university u on f.university_id = u.id " +
            "where u.id=?1", nativeQuery = true)
    List<Group> getGroupsByUniversityIdNative(Integer universityId);

}
