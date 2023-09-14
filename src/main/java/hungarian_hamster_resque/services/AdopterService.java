package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.adopter.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.adopter.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.adopter.CreateAdopterCommand;
import hungarian_hamster_resque.dtos.adopter.UpdateAdopterCommand;
import hungarian_hamster_resque.exceptions.AdopterCantDeleteBecauseHamstersListNotEmptyException;
import hungarian_hamster_resque.exceptions.AdopterWithCityNotExistException;
import hungarian_hamster_resque.exceptions.AdopterWithIdNotExistException;
import hungarian_hamster_resque.exceptions.AdopterWithNameNotExistException;
import hungarian_hamster_resque.mappers.AddressMapper;
import hungarian_hamster_resque.mappers.AdopterMapper;
import hungarian_hamster_resque.models.Address;
import hungarian_hamster_resque.models.Adopter;
import hungarian_hamster_resque.repositories.AdopterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdopterService {

    private final AdopterMapper adopterMapper;

    private final AdopterRepository adopterRepository;

    private final AddressMapper addressMapper;

    public AdopterDtoWithoutHamsters createAdopter(CreateAdopterCommand command) {
        Address address = new Address(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther());
        Adopter adopter = Adopter.builder()
                .name(command.getName())
                .address(address)
                .build();

        adopterRepository.save(adopter);
        AdopterDtoWithoutHamsters adopterDto = adopterMapper.toDtoWithoutHamster(adopter);
        adopterDto.setAddress(addressMapper.toAddressDto(address));

        return adopterDto;

    }

    public AdopterDtoWithoutHamsters updateAdopter(long id, UpdateAdopterCommand command) {
        Adopter adopter = findAdopterEntityById(id);
        Address address = new Address(command.getZip(), command.getTown(), command.getStreet(), command.getHouseNumber(), command.getOther());

        adopter.setName(command.getName());
        adopter.setAddress(address);
        adopterRepository.save(adopter);

        AdopterDtoWithoutHamsters adopterDto = AdopterDtoWithoutHamsters.builder()
                .name(adopter.getName())
                .address(addressMapper.toAddressDto(address))
                .build();

        return adopterDto;
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
