package org.lisasp.competition.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.CompetitionCreated;
import org.lisasp.competition.api.CompetitionDto;
import org.lisasp.competition.api.CreateCompetition;
import org.lisasp.competition.base.api.exception.*;

import java.util.UUID;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;

    private final EntityToDtoMapper mapper = new EntityToDtoMapper();

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
}
