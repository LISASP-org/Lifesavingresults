package org.lisasp.results.model.competition;

import lombok.RequiredArgsConstructor;
import org.lisasp.results.api.dto.CompetitionCreated;
import org.lisasp.results.api.dto.CompetitionDto;
import org.lisasp.results.api.dto.CreateCompetition;
import org.lisasp.results.model.exception.NotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Component
public class CompetitionService {

    private CompetitionMapper mapper = Mappers.getMapper( CompetitionMapper.class );

    private final CompetitionRepository competitionRepository;

    public CompetitionCreated execute(CreateCompetition createCompetition) {
        CompetitionEntity competition = new CompetitionEntity();
        competition.setName(createCompetition.name());
        competition.setAcronym(createCompetition.acronym());
        competition.setFrom(createCompetition.from());
        competition.setTill(createCompetition.till());

        competitionRepository.save(competition);

        return new CompetitionCreated(competition.getId());
    }

    public CompetitionDto findCompetition(String id) throws NotFoundException{
        CompetitionEntity entity = competitionRepository.findById(id).orElseThrow(() -> new NotFoundException("Competition", id));
        return mapper.entityToDto(entity);
    }

    public CompetitionDto[] findCompetitions() {
        return StreamSupport.stream(competitionRepository.findAll().spliterator(), false).map(e -> mapper.entityToDto(e)).toArray(CompetitionDto[]::new);
    }
}
