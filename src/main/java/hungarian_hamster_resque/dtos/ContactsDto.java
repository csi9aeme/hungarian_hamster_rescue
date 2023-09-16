package hungarian_hamster_resque.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactsDto {

    private String phoneNumber;

    private String email;

    private String otherContact;
}
