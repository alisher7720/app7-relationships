package uz.pdp.app7jparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import uz.pdp.app7jparelationships.entity.Address;
import uz.pdp.app7jparelationships.entity.Group;
import uz.pdp.app7jparelationships.entity.Student;
import uz.pdp.app7jparelationships.payload.StudentDto;
import uz.pdp.app7jparelationships.repository.AddressRepository;
import uz.pdp.app7jparelationships.repository.GroupRepository;
import uz.pdp.app7jparelationships.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    AddressRepository addressRepository;

    private static final int pageSize = 10;

    /*
    * __ READ __
    * */

    // read students in page

    // read all
    @GetMapping("/forMinistry")
    public Page<Student> getAllStudentsInPage(@RequestParam int page){
        /*
        * pageSize * page ta student ni tashlab, pageSize ta student ni olib kel
        * example:
        *   page = 2 (yani 3 - bet)
        *   10 * 2 = 20 ta studentni tashlab, 10 ta student olib kel
        * */
        Pageable pageable = PageRequest.of(page, pageSize);
        return studentRepository.findAll(pageable);
    }

    @GetMapping("/forUniversity/{universityId}")
    public Page<Student> getStudentsForUniversityInPage(@PathVariable Integer universityId,
                                                       @RequestParam int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return studentRepository.findAllByGroup_Faculty_UniversityId(pageable, universityId);
    }

    @GetMapping("/forFaculty/{facultyId}")
    public Page<Student> getStudentsForFacultyInPage(@PathVariable Integer facultyId,
                                                     @RequestParam int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return studentRepository.findAllByGroup_FacultyId(facultyId, pageable);
    }

    @GetMapping("/forGroup/{groupId}")
    public Page<Student> getStudentsForGroupInPage(@PathVariable Integer groupId,
                                                   @RequestParam int page){
        Pageable pageable = PageRequest.of(page, pageSize);
        return studentRepository.findAllByGroupId(groupId, pageable);
    }

    // read all
    @GetMapping
    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    // read students for university
    @GetMapping("/byUniversityId/{universityId}")
    public List<Student> getStudentsByUniversityId(@PathVariable Integer universityId){
        return studentRepository.findAllByGroup_Faculty_UniversityId(universityId);
    }

    // read students for faculty
    @GetMapping("/byFacultyId/{facultyId}")
    public List<Student> getStudentsByFacultyId(@PathVariable Integer facultyId){
        return studentRepository.findAllByGroup_FacultyId(facultyId);
    }

    // read students for group
    @GetMapping("/byGroupId/{groupId}")
    public List<Student> getStudentsByGroupId(@PathVariable Integer groupId){
        return studentRepository.findAllByGroupId(groupId);
    }


    /*
    * __ CREATE __
    * */

    @PostMapping
    public String addStudent(@RequestBody StudentDto studentDto){
        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (optionalGroup.isEmpty())
            return "Group not found!";

        Address savedAddress = addressRepository.save(new Address(
                studentDto.getStreet(),
                studentDto.getDistrict(),
                studentDto.getCity()
        ));

        Student student = new Student();
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setGroup(optionalGroup.get());
        student.setAddress(savedAddress);
        studentRepository.save(student);
        return "Student saved!";
    }

    /*
    * __ UPDATE __
    * */

    @PutMapping("/{id}")
    public String editStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty())
            return "Student saved!";

        Optional<Group> optionalGroup = groupRepository.findById(studentDto.getGroupId());
        if (optionalGroup.isEmpty())
            return "Group not found!";

        Student student = optionalStudent.get();
        student.setLastName(studentDto.getLastName());
        student.setFirstName(studentDto.getFirstName());

        Address address = student.getAddress();
        address.setCity(studentDto.getCity());
        address.setDistrict(studentDto.getDistrict());
        address.setStreet(studentDto.getStreet());

        student.setAddress(address);
        student.setGroup(optionalGroup.get());
        studentRepository.save(student);
        return "Student edited!";
    }

    /*
    * __ DELETE __
    * */

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty())
            return "Student not found!";

        studentRepository.deleteById(id);
        addressRepository.deleteById(optionalStudent.get().getAddress().getId());
        return "Student deleted!";
    }

}





