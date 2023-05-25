package hungarian_hamster_resque.services;

import hungarian_hamster_resque.dtos.*;
import hungarian_hamster_resque.enums.HamsterSpecies;
import hungarian_hamster_resque.exceptions.*;
import hungarian_hamster_resque.mappers.HamsterMapper;
import hungarian_hamster_resque.models.*;
import hungarian_hamster_resque.repositories.AdoptiveRepository;
import hungarian_hamster_resque.repositories.HamsterRepository;
import hungarian_hamster_resque.repositories.HostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HamsterService {

    private final HamsterMapper hamsterMapper;
    private final HamsterRepository hamsterRepository;
    private final HostRepository hostRepository;
    private final AdoptiveRepository adoptiveRepository;


    public List<HamsterDto> getListOfHamsters(Optional<String> hamsterName) {
        if (hamsterName.isPresent()) {
            List<Hamster> result = findHamsterWithNamePart(hamsterName.get());
            return hamsterMapper.toDto(result);
        }

        return hamsterMapper.toDto(hamsterRepository.findAll());
    }

    public List<HamsterDtoWithoutAdoptive> getListOfActualHamsters() {
        List<Hamster> hamstersWithoutOwner = hamsterRepository.findFosteringHamsters();

        return hamsterMapper.toDtoWithoutAdoptive(hamstersWithoutOwner);

    }

    public HamsterDtoWithoutAdoptive findAdoptableHamsterById(long id) {
        return hamsterMapper.toDtoWithoutAdoptive(findHamsterEntityById(id));
    }

    @Transactional
    public HamsterDtoWithoutAdoptive createHamster(CreateHamsterCommand command) {
        Host host = checkHostIsAvailable(command.getHostId());
        HamsterSpecies spec = findHamsterSpecies(command.getHamsterSpecies());

        Hamster hamster = Hamster.builder()
                .name(command.getName())
                .hamsterSpecies(spec)
                .gender(command.getGender())
                .dateOfBirth(command.getDateOfBirth())
                .hamsterStatus(command.getHamsterStatus())
                .host(host)
                .startOfFostering(command.getStartOfFoster())
                .build();
        host.addHamster(hamster);

        hamsterRepository.save(hamster);

        return hamsterMapper.toDtoWithoutAdoptive(hamster);
    }

    private HamsterSpecies findHamsterSpecies(String hamsterSpecies) {
        for (HamsterSpecies sp : HamsterSpecies.values()) {
            if (sp.getNameOfSpecies().equals(hamsterSpecies)) {
                return sp;
            }
        }
        throw new HamsterSpeciesNotExistException(hamsterSpecies);
    }

        @Transactional
        public HamsterDtoWithoutAdoptive updateHamsterAllAttributes ( long id, UpdateHamsterCommand command){
            Hamster hamsterForUpdate = findHamsterEntityById(id);

            hamsterForUpdate.setName(command.getName());
            hamsterForUpdate.setHamsterSpecies(command.getHamsterSpecies());
            hamsterForUpdate.setDateOfBirth(command.getDateOfBirth());
            hamsterForUpdate.setHamsterStatus(command.getHamsterStatus());
            Host host = hostRepository.findById(command.getHostId())
                    .orElseThrow(() -> new HostWithIdNotExistException(command.getHostId()));
            hamsterForUpdate.setHost(host);

            hamsterRepository.save(hamsterForUpdate);
            return hamsterMapper.toDtoWithoutAdoptive(hamsterForUpdate);
        }
        public HamsterDto adoptHamster ( long id, AdoptHamsterCommand command){
            Hamster adopted = findHamsterEntityById(id);
            Adoptive adoptive = adoptiveRepository.findAdoptiveByIdWithHamsters(command.getAdoptiveId());

            adopted.setHamsterStatus(command.getHamsterStatus());

            adopted.setAdoptive(adoptive);
            adopted.setDateOfAdoption(command.getDateOfAdoption());

            adoptive.addHamster(adopted);
            hamsterRepository.save(adopted);

            return hamsterMapper.toDto(adopted);
        }


        private List<Hamster> findHamsterWithNamePart (String hamsterName){
            List<Hamster> result = hamsterRepository.findHamsterByNameContains(hamsterName);
            if (result.isEmpty()) {
                throw new HamsterWithNameNotExist(hamsterName);
            }
            return result;
        }

        private Hamster findHamsterEntityById ( long id){
            return hamsterRepository.findById(id)
                    .orElseThrow(() -> new HamsterWithIdNotExistException(id));
        }

        private Host checkHostIsAvailable ( long id){
            Host host = hostRepository.findById(id).orElseThrow(
                    () -> new HostWithIdNotExistException(id));

            List<Hamster> fosteringHamstersByHost = hamsterRepository.findFosteringHamstersByHostId(host.getId());
            if (fosteringHamstersByHost.size() >= host.getCapacity()) {
                throw new HostCantTakeMoreHamstersException(host.getId());
            }
            return host;
        }

    }
