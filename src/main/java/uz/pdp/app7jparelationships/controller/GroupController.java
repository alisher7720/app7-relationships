package uz.pdp.app7jparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.app7jparelationships.entity.Faculty;
import uz.pdp.app7jparelationships.entity.Group;
import uz.pdp.app7jparelationships.payload.FacultyDto;
import uz.pdp.app7jparelationships.payload.GroupDto;
import uz.pdp.app7jparelationships.repository.FacultyRepository;
import uz.pdp.app7jparelationships.repository.GroupRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    FacultyRepository facultyRepository;

    // GET ALL ( FOR MINISTRY )
    @GetMapping
    public List<Group> getAllGroups(){
        return groupRepository.findAll();
    }

    // GET GROUPS FOR UNIVERSITY
    @GetMapping("/byUniversityId/{universityId}")
    public List<Group> getGroupsByUniversityId(@PathVariable Integer universityId){
        return groupRepository.getGroupsByUniversityIdNative(universityId);
    }

    // GET GROUPS FOR FACULTY
    @GetMapping("/byFacultyId/{facultyId}")
    public List<Group> getGroupsByFacultyId(@PathVariable Integer facultyId){
        return groupRepository.findAllByFacultyId(facultyId);
    }

    // GET ONE BY ID
    @GetMapping("/{id}")
    public Group getGroupById(@PathVariable Integer id){
        return groupRepository.findById(id).orElse(null);
    }

    // CREATE
    @PostMapping
    public String addGroup(@RequestBody GroupDto groupDto){
        boolean exists = groupRepository.existsByNameAndFacultyId(
                groupDto.getName(),
                groupDto.getFacultyId());
        if (exists)
            return "group exists in this faculty!";

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (optionalFaculty.isEmpty())
            return "Faculty not found!";

        Group group = new Group();
        group.setName(groupDto.getName());
        group.setFaculty(optionalFaculty.get());
        groupRepository.save(group);
        return "Group saved!";
    }

    // UPDATE
    @PutMapping("/{id}")
    public String editFaculty(@PathVariable Integer id,
                              @RequestBody GroupDto groupDto){

        Optional<Group> optionalGroup = groupRepository.findById(id);
        if (optionalGroup.isEmpty())
            return "Group not found!";

        boolean exists = groupRepository.existsByNameAndFacultyIdAndIdNot(
                groupDto.getName(),
                groupDto.getFacultyId(),
                id
        );
        if (exists)
            return "Group already exists!";

        Optional<Faculty> optionalFaculty = facultyRepository.findById(groupDto.getFacultyId());
        if (optionalFaculty.isEmpty())
            return "Faculty not found!";

        Group group = optionalGroup.get();
        group.setName(groupDto.getName());
        group.setFaculty(optionalFaculty.get());
        groupRepository.save(group);

        return "Group edited!";
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteGroup(@PathVariable Integer id){
        if (!groupRepository.existsById(id))
            return "Group not found!";

        groupRepository.deleteById(id);
        return "Group deleted!";
    }

}





