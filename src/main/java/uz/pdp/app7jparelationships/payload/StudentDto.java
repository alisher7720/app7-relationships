package uz.pdp.app7jparelationships.payload;

import lombok.Data;
import uz.pdp.app7jparelationships.entity.Subject;

import java.util.List;

@Data
public class StudentDto {
    private String firstName;
    private String lastName;

    private Integer groupId;

    private String city;
    private String district;
    private String street;

    private List<Subject> subjects;
}
