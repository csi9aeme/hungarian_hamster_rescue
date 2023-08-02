package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.AdopterDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdopterDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.CreateAdopterCommand;
import hungarian_hamster_resque.dtos.UpdateAdopterCommand;
import hungarian_hamster_resque.exceptions.AdopterCantDeleteBecauseHamstersListNotEmptyException;
import hungarian_hamster_resque.exceptions.AdopterWithCityNotExistException;
import hungarian_hamster_resque.exceptions.AdopterWithIdNotExistException;
import hungarian_hamster_resque.exceptions.AdopterWithNameNotExistException;
import hungarian_hamster_resque.mappers.AdoptiveMapper;
import hungarian_hamster_resque.models.Adoptive;
import hungarian_hamster_resque.repositories.AdoptiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdoptiveService {

    private final AdoptiveMapper adoptiveMapper;

    private final AdoptiveRepository adoptiveRepository;

    public AdopterDtoWithoutHamsters createAdoptive(CreateAdopterCommand command) {
        Adoptive adoptive = Adoptive.builder()
                .name(command.getName())
                .address(command.getAddress())
                .build();
        adoptiveRepository.save(adoptive);

        return adoptiveMapper.toDtoWithoutHamster(adoptive);

    }

    public AdopterDtoWithoutHamsters updateAdoptive(long id, UpdateAdopterCommand command) {
        Adoptive adoptive = findAdoptiveEntityById(id);

        adoptive.setName(command.getName());
        adoptive.setAddress(command.getAddress());
        adoptiveRepository.save(adoptive);
        return adoptiveMapper.toDtoWithoutHamster(adoptive);
    }


    public List<AdopterDtoWithoutHamsters> getAdoptives(Optional<String> namePart) {
        if (namePart.isPresent()) {
            List<Adoptive> result = findAdoptivesByNamePartWithoutHamstersList(namePart.get());
            return adoptiveMapper.toDtoWithoutHamster(result);
        }

        return adoptiveMapper.toDtoWithoutHamster(adoptiveRepository.findAll());
    }

    public List<AdopterDtoWithoutHamsters> getAdoptivesByCity(String city) {
        List<Adoptive> result = adoptiveRepository.findAdoptiveByAddressContains(city);
        if (result.isEmpty()) {
            throw new AdopterWithCityNotExistException(city);
        }

        return adoptiveMapper.toDtoWithoutHamster(result);

    }

    public AdopterDtoWithHamsters findAdoptiveByIdWithHamsters(long id) {
        Adoptive adoptive = adoptiveRepository.findAdoptiveByIdWithHamsters(id);
        return adoptiveMapper.toDtoWithHamster(adoptive);
    }

    public AdopterDtoWithoutHamsters findAdoptiveById(long id) {
        return adoptiveMapper.toDtoWithoutHamster(findAdoptiveEntityById(id));
    }


    public void deleteAdoptive(long id) {
        Adoptive adoptive = adoptiveRepository.findById(id)
                .orElseThrow(() -> new AdopterWithIdNotExistException(id));

        if (adoptive.getHamsters().size() > 0) {
            throw new AdopterCantDeleteBecauseHamstersListNotEmptyException(id);
        }

        adoptiveRepository.deleteById(id);


    }
    private Adoptive findAdoptiveEntityById(long id) {
        return adoptiveRepository.findById(id).orElseThrow(
                () -> new AdopterWithIdNotExistException(id));
    }

    private List<Adoptive> findAdoptivesByNamePartWithoutHamstersList(String s) {
        List<Adoptive> result = adoptiveRepository.findAdoptiveByNameContains(s);
        if (result.isEmpty()) {
            throw new AdopterWithNameNotExistException(s);
        }
        return result;

    }


}
