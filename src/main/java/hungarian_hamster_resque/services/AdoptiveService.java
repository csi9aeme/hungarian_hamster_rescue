package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.AdoptiveDtoWithHamsters;
import hungarian_hamster_resque.dtos.AdoptiveDtoWithoutHamsters;
import hungarian_hamster_resque.dtos.CreateAdoptiveCommand;
import hungarian_hamster_resque.dtos.UpdateAdoptiveCommand;
import hungarian_hamster_resque.exceptions.AdoptiveCantDeleteBecauseHamstersListNotEmptyException;
import hungarian_hamster_resque.exceptions.AdoptiveWithCityNotExistException;
import hungarian_hamster_resque.exceptions.AdoptiveWithIdNotExistException;
import hungarian_hamster_resque.exceptions.AdoptiveWithNameNotExistException;
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

    public AdoptiveDtoWithoutHamsters createAdoptive(CreateAdoptiveCommand command) {
        Adoptive adoptive = Adoptive.builder()
                .name(command.getName())
                .address(command.getAddress())
                .build();
        adoptiveRepository.save(adoptive);

        return adoptiveMapper.toDtoWithoutHamster(adoptive);

    }

    public AdoptiveDtoWithoutHamsters updateAdoptive(long id, UpdateAdoptiveCommand command) {
        Adoptive adoptive = findAdoptiveEntityById(id);

        adoptive.setName(command.getName());
        adoptive.setAddress(command.getAddress());
        adoptiveRepository.save(adoptive);
        return adoptiveMapper.toDtoWithoutHamster(adoptive);
    }


    public List<AdoptiveDtoWithoutHamsters> getAdoptives(Optional<String> namePart) {
        if (namePart.isPresent()) {
            List<Adoptive> result = findAdoptivesByNamePartWithoutHamstersList(namePart.get());
            return adoptiveMapper.toDtoWithoutHamster(result);
        }

        return adoptiveMapper.toDtoWithoutHamster(adoptiveRepository.findAll());
    }

    public List<AdoptiveDtoWithoutHamsters> getAdoptivesByCity(String city) {
        List<Adoptive> result = adoptiveRepository.findAdoptiveByAddressContains(city);
        if (result.isEmpty()) {
            throw new AdoptiveWithCityNotExistException(city);
        }

        return adoptiveMapper.toDtoWithoutHamster(result);

    }

    public AdoptiveDtoWithHamsters findAdoptiveByIdWithHamsters(long id) {
        Adoptive adoptive = adoptiveRepository.findAdoptiveByIdWithHamsters(id);
        return adoptiveMapper.toDtoWithHamster(adoptive);
    }

    public AdoptiveDtoWithoutHamsters findAdoptiveById(long id) {
        return adoptiveMapper.toDtoWithoutHamster(findAdoptiveEntityById(id));
    }


    public void deleteAdoptive(long id) {
        Adoptive adoptive = adoptiveRepository.findById(id)
                .orElseThrow(() -> new AdoptiveWithIdNotExistException(id));

        if (adoptive.getHamsters().size() > 0) {
            throw new AdoptiveCantDeleteBecauseHamstersListNotEmptyException(id);
        }

        adoptiveRepository.deleteById(id);


    }
    private Adoptive findAdoptiveEntityById(long id) {
        return adoptiveRepository.findById(id).orElseThrow(
                () -> new AdoptiveWithIdNotExistException(id));
    }

    private List<Adoptive> findAdoptivesByNamePartWithoutHamstersList(String s) {
        List<Adoptive> result = adoptiveRepository.findAdoptiveByNameContains(s);
        if (result.isEmpty()) {
            throw new AdoptiveWithNameNotExistException(s);
        }
        return result;

    }


}
