package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.CreateAdopterCommand;
import hungarian_hamster_resque.dtos.UpdateAdopterCommand;
import hungarian_hamster_resque.exceptions.AdopterCantDeleteBecauseHamstersListNotEmptyException;
import hungarian_hamster_resque.exceptions.AdopterWithCityNotExistException;
import hungarian_hamster_resque.exceptions.AdopterWithIdNotExistException;
import hungarian_hamster_resque.exceptions.AdopterWithNameNotExistException;
import hungarian_hamster_resque.mappers.AdopterMapper;
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

    public AdopterDtoWithoutHamsters createAdopter(CreateAdopterCommand command) {
        Adopter adopter = Adopter.builder()
                .name(command.getName())
                .address(command.getAddress())
                .build();
        adopterRepository.save(adopter);

        return adopterMapper.toDtoWithoutHamster(adopter);

    }

    public AdopterDtoWithoutHamsters updateAdopter(long id, UpdateAdopterCommand command) {
        Adopter adopter = findAdopterEntityById(id);

        adopter.setName(command.getName());
        adopter.setAddress(command.getAddress());
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
        List<Adopter> result = adopterRepository.findAdopterByAddressContains(city);
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
