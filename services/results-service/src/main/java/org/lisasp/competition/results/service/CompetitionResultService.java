package org.lisasp.competition.results.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.results.api.CompetitionCreated;
import org.lisasp.competition.results.api.CompetitionDto;
import org.lisasp.competition.results.api.CreateCompetition;
import org.lisasp.competition.base.api.exception.CompetitionNotFoundException;
import org.lisasp.competition.base.api.exception.NotFoundException;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class CompetitionResultService {

    private final CompetitionResultRepository competitionResultRepository;
    private final EventRepository eventRepository;
    private final EntryRepository entryRepository;

    private final EntityToDtoMapper mapper = new EntityToDtoMapper();

    public CompetitionCreated execute(CreateCompetition createCompetition) {
        CompetitionResultEntity competition = new CompetitionResultEntity();
        competition.setUploadId(UUID.randomUUID().toString());
        competition.setName(createCompetition.name());
        competition.setAcronym(createCompetition.acronym());
        competition.setFrom(createCompetition.from());
        competition.setTill(createCompetition.till());

        competitionResultRepository.save(competition);

        return new CompetitionCreated(competition.getId());
    }

    public CompetitionDto findCompetition(String id) throws NotFoundException {
        CompetitionResultEntity entity = competitionResultRepository.findById(id).orElseThrow(() -> new NotFoundException("Competition", id));
        return mapper.entityToDto(entity);
    }

    public CompetitionDto[] findCompetitions() {
        return StreamSupport.stream(competitionResultRepository.findAll().spliterator(), false).map(e -> mapper.entityToDto(e).withoutUploadId()).toArray(CompetitionDto[]::new);
    }

    public String getCompetitionIdByUploadId(String uploadId) throws CompetitionNotFoundException {
        CompetitionResultEntity entity = competitionResultRepository.findByUploadId(uploadId).orElseThrow(() -> new CompetitionNotFoundException(uploadId));
        return entity.getId();
    }

    public void update(String id, Consumer<ResultUpdater> updater) throws NotFoundException {
        ResultUpdater resultUpdater = new ResultUpdater(competitionResultRepository, eventRepository, entryRepository).initialize(id);
        updater.accept(resultUpdater);
        resultUpdater.save();
    }
}
