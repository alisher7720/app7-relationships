package uz.pdp.app7jparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.app7jparelationships.entity.Subject;
import uz.pdp.app7jparelationships.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    SubjectRepository subjectRepository;

    @GetMapping
    public List<Subject> getAllSubjects(){
        return subjectRepository.findAll();
    }

    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        return optionalSubject.orElse(new Subject());
    }

    @PostMapping
    public String addSubject(@RequestBody Subject subject){
        boolean exists = subjectRepository.existsByName(subject.getName());
        if (exists)
            return "Subject already exists!";

        subjectRepository.save(subject);
        return "Subject saved successfully!";
    }

    @PutMapping("/{id}")
    public String editSubject(@PathVariable Integer id, @RequestBody Subject subject){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isEmpty())
            return "Subject not found!";

        Subject editingSubject = optionalSubject.get();
        editingSubject.setName(subject.getName());
        subjectRepository.save(editingSubject);
        return "Subject edited!";
    }

    @DeleteMapping("/{id}")
    public String deleteSubject(@PathVariable Integer id){
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        if (optionalSubject.isEmpty())
            return "Subject not found!";

        subjectRepository.deleteById(id);
        return "Subject deleted!";
    }

}
