package uz.pdp.app7jparelationships.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniversityDto {
    private String name;
    private String street;
    private String district;
    private String city;
}
