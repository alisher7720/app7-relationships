package uz.pdp.app7jparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.app7jparelationships.entity.Faculty;
import uz.pdp.app7jparelationships.entity.University;
import uz.pdp.app7jparelationships.payload.FacultyDto;
import uz.pdp.app7jparelationships.repository.FacultyRepository;
import uz.pdp.app7jparelationships.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    UniversityRepository universityRepository;

    // CREATE
    @PostMapping
    public String addFaculty(@RequestBody FacultyDto facultyDto){
        boolean exists = facultyRepository.existsByNameAndUniversityId(
                facultyDto.getName(),
                facultyDto.getUniversityId()
        );
        if (exists)
            return "Faculty already exists in this university";

        Optional<University> optionalUniversity = universityRepository.findById(facultyDto.getUniversityId());
        if (optionalUniversity.isEmpty())
            return "university not found!";

        Faculty faculty = new Faculty();
        faculty.setName(facultyDto.getName());
        faculty.setUniversity(optionalUniversity.get());
        facultyRepository.save(faculty);
        return "Faculty saved!";
    }

    // READ ALL
    @GetMapping
    public List<Faculty> getAllFaculties(){
        return facultyRepository.findAll();
    }

    // READ FACULTIES OF UNIVERSITY (by id)
    @GetMapping("/byUniversityId/{universityId}")
    public List<Faculty> getFacultiesByUniversityId(@PathVariable Integer universityId){
        return facultyRepository.findAllByUniversityId(universityId);
    }

    // READ ONE
    @GetMapping("/{id}")
    public Faculty getFacultyById(@PathVariable Integer id){
        return facultyRepository.findById(id).orElse(new Faculty());
    }

    // UPDATE
    @PutMapping("/{id}")
    public String editFaculty(@PathVariable Integer id, @RequestBody FacultyDto facultyDto){
        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        if (optionalFaculty.isEmpty())
            return "Faculty not found by this id: " + id;

        Optional<University> optionalUniversity = universityRepository.findById(facultyDto.getUniversityId());
        if (optionalUniversity.isEmpty())
            return "University not found!";

        boolean exists = facultyRepository.existsByNameAndUniversityIdAndIdNot(
                facultyDto.getName(),
                facultyDto.getUniversityId(),
                id);
        if (exists)
            return "This faculty already exists!";

        Faculty faculty = optionalFaculty.get();
        faculty.setName(facultyDto.getName());
        faculty.setUniversity(optionalUniversity.get());
        facultyRepository.save(faculty);
        return "Faculty edited!";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteFaculty(@PathVariable Integer id){
        /*
         * TODO Agar faculty o'chsa, uning ichidagi guruhlar ham o'chishi kerak
         * */

        Optional<Faculty> optionalFaculty = facultyRepository.findById(id);
        if (optionalFaculty.isEmpty())
            return "Faculty not found for deleting!";

        facultyRepository.deleteById(id);
        return "Faculty deleted!";
    }
}





