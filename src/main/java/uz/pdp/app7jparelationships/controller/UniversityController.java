package uz.pdp.app7jparelationships.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.pdp.app7jparelationships.entity.Address;
import uz.pdp.app7jparelationships.entity.University;
import uz.pdp.app7jparelationships.payload.UniversityDto;
import uz.pdp.app7jparelationships.repository.AddressRepository;
import uz.pdp.app7jparelationships.repository.UniversityRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/university")
public class UniversityController {

    @Autowired
    UniversityRepository universityRepository;

    @Autowired
    AddressRepository addressRepository;

    // Read All
    @GetMapping
    public List<University> getAllUniversity(){
        return universityRepository.findAll();
    }

    // Read One
    @GetMapping("/{id}")
    public University getUniversityById(@PathVariable Integer id){
        Optional<University> optionalUniversity = universityRepository.findById(id);
        return optionalUniversity.orElse(new University());
    }

    // Create
    @PostMapping
    public String addUniversity(@RequestBody UniversityDto universityDto){
        boolean existsByName = universityRepository.existsByName(universityDto.getName());
        if (existsByName)
            return "University exists by this name: " + universityDto.getName();

        Address address = new Address();
        address.setCity(universityDto.getCity());
        address.setDistrict(universityDto.getDistrict());
        address.setStreet(universityDto.getStreet());
        Address savedAddress = addressRepository.save(address);

        University university = new University();
        university.setName(universityDto.getName());
        university.setAddress(savedAddress);
        universityRepository.save(university);
        return "University saved successfully";
    }

    // Update

    @PutMapping("/{id}")
    public String editUniversity(@PathVariable Integer id, @RequestBody UniversityDto universityDto){
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isEmpty())
            return "University not found!";

        University university = optionalUniversity.get();
        university.setName(universityDto.getName());

        Address address = university.getAddress();
        address.setStreet(universityDto.getStreet());
        address.setDistrict(universityDto.getDistrict());
        address.setCity(universityDto.getCity());
//        Address editedAddress = addressRepository.save(address);

//        university.setAddress(editedAddress);
        universityRepository.save(university);
        return "University edited!";
    }

    // Delete
    @DeleteMapping("/{id}")
    public String deleteUniversity(@PathVariable Integer id){
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isEmpty())
            return "University not found!";

        // University is deleted before, address is deleted after
        universityRepository.deleteById(id);
        addressRepository.deleteById(optionalUniversity.get().getAddress().getId());
        return "University deleted!";
    }

}
