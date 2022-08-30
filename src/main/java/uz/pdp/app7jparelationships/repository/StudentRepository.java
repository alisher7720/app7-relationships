package uz.pdp.app7jparelationships.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.app7jparelationships.entity.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    List<Student> findAllByGroup_Faculty_UniversityId(Integer group_faculty_university_id);

    List<Student> findAllByGroup_FacultyId(Integer group_faculty_id);

    List<Student> findAllByGroupId(Integer group_id);

    Page<Student> findAllByGroup_Faculty_UniversityId(Pageable pageable, Integer group_faculty_university_id);

    Page<Student> findAllByGroup_FacultyId(Integer group_faculty_id, Pageable pageable);

    Page<Student> findAllByGroupId(Integer group_id, Pageable pageable);

}
