package org.lisasp.results.model;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.dto.CompetitionCreated;
import org.lisasp.competition.api.dto.CompetitionDto;
import org.lisasp.competition.api.dto.CreateCompetition;
import org.lisasp.competition.api.exception.CompetitionNotFoundException;
import org.lisasp.competition.api.exception.NotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component
public class CompetitionService {

    private final EntityToDtoMapper mapper = Mappers.getMapper(EntityToDtoMapper.class);

    private final CompetitionRepository competitionRepository;
    private final EventRepository eventRepository;
    private final EntryRepository entryRepository;

    @Transactional
    public CompetitionCreated execute(CreateCompetition createCompetition) {
        CompetitionEntity competition = new CompetitionEntity();
        competition.setUploadId(UUID.randomUUID().toString());
        competition.setName(createCompetition.name());
        competition.setAcronym(createCompetition.acronym());
        competition.setFrom(createCompetition.from());
        competition.setTill(createCompetition.till());

        competitionRepository.save(competition);

        return new CompetitionCreated(competition.getId());
    }

    public CompetitionDto findCompetition(String id) throws NotFoundException {
        CompetitionEntity entity = competitionRepository.findById(id).orElseThrow(() -> new NotFoundException("Competition", id));
        return mapper.entityToDto(entity);
    }

    public CompetitionDto[] findCompetitions() {
        return StreamSupport.stream(competitionRepository.findAll().spliterator(), false).map(e -> mapper.entityToDto(e).withoutUploadId()).toArray(CompetitionDto[]::new);
    }

    public String getCompetitionIdByUploadId(String uploadId) throws CompetitionNotFoundException {
        CompetitionEntity entity = competitionRepository.findByUploadId(uploadId).orElseThrow(() -> new CompetitionNotFoundException(uploadId));
        return entity.getId();
    }

    private CompetitionEntity getOrCreateCompetitionForId(String id) {
        return competitionRepository.findById(id).orElseGet(() -> new CompetitionEntity());
    }

    public void update(String id, Consumer<CompetitionUpdater> updater) throws NotFoundException {

        CompetitionUpdater competitionUpdater = new CompetitionUpdater(competitionRepository, eventRepository, entryRepository).initialize(id);
        updater.accept(competitionUpdater);
        competitionUpdater.save();
    }
}
