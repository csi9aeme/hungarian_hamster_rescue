package hungarian_hamster_rescue.services;

import hungarian_hamster_rescue.dtos.adopter.AdopterDtoWithHamsters;
import hungarian_hamster_rescue.dtos.adopter.AdopterDtoWithoutHamsters;
import hungarian_hamster_rescue.dtos.adopter.CreateAdopterCommand;
import hungarian_hamster_rescue.dtos.adopter.UpdateAdopterCommand;
import hungarian_hamster_rescue.exceptions.AdopterCantDeleteBecauseHamstersListNotEmptyException;
import hungarian_hamster_rescue.exceptions.AdopterWithCityNotExistException;
import hungarian_hamster_rescue.exceptions.AdopterWithIdNotExistException;
import hungarian_hamster_rescue.exceptions.AdopterWithNameNotExistException;
import hungarian_hamster_rescue.mappers.AdopterMapper;
import hungarian_hamster_rescue.models.Address;
import hungarian_hamster_rescue.models.Adopter;
import hungarian_hamster_rescue.models.Contacts;
import hungarian_hamster_rescue.repositories.AdopterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdopterService {

    private final AdopterMapper adopterMapper;

    private final AdopterRepository adopterRepository;


    public AdopterDtoWithoutHamsters createAdopter(CreateAdopterCommand command) {
        Address address = new Address(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther());
        Adopter adopter = Adopter.builder()
                .name(command.getName())
                .address(address)
                .build();

        adopterRepository.save(adopter);

        return adopterMapper.toDtoWithoutHamster(adopter);

    }

    public AdopterDtoWithoutHamsters updateAdopter(long id, UpdateAdopterCommand command) {
        Adopter adopter = findAdopterEntityById(id);
        Address address = new Address(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther());
        Contacts contacts = new Contacts(command.getPhoneNumber(), command.getEmail(), command.getOtherContactInfo());

        adopter.setName(command.getName());
        adopter.setAddress(address);
        adopter.setContacts(contacts);

        adopterRepository.save(adopter);

        return adopterMapper.toDtoWithoutHamster(adopter);
    }

    public List<AdopterDtoWithoutHamsters> getAdopter(Optional<String> namePart) {
        if (namePart.isPresent()) {
            List<Adopter> result = findAdoptersByNamePartWithoutHamstersList(namePart.get());
            return adopterMapper.toDtoWithoutHamster(result);
        }

        return adopterMapper.toDtoWithoutHamster(adopterRepository.findAll());
    }

    public List<AdopterDtoWithoutHamsters> getAdoptersByCity(String city) {
        List<Adopter> result = adopterRepository.findAdopterByCity(city);
        if (result.isEmpty()) {
            throw new AdopterWithCityNotExistException(city);
        }

        return adopterMapper.toDtoWithoutHamster(result);

    }

    public AdopterDtoWithHamsters findAdopterByIdWithHamsters(long id) {
        Adopter adopter = adopterRepository.findAdopterByIdWithHamsters(id);
        return adopterMapper.toDtoWithHamster(adopter);
    }

    public AdopterDtoWithoutHamsters findAdopteryId(long id) {
        return adopterMapper.toDtoWithoutHamster(findAdopterEntityById(id));
    }


    public void deleteAdopter(long id) {
        Adopter adopter = adopterRepository.findById(id)
                .orElseThrow(() -> new AdopterWithIdNotExistException(id));

        if (adopter.getHamsters().size() > 0) {
            throw new AdopterCantDeleteBecauseHamstersListNotEmptyException(id);
        }

        adopterRepository.deleteById(id);

    }

    private Adopter findAdopterEntityById(long id) {
        return adopterRepository.findById(id).orElseThrow(
                () -> new AdopterWithIdNotExistException(id));
    }

    private List<Adopter> findAdoptersByNamePartWithoutHamstersList(String s) {
        List<Adopter> result = adopterRepository.findAdopterByNameContains(s);
        if (result.isEmpty()) {
            throw new AdopterWithNameNotExistException(s);
        }
        return result;

    }


}
