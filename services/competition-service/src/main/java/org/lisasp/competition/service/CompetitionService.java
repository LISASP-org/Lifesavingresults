package org.lisasp.competition.service;

import lombok.RequiredArgsConstructor;
import org.lisasp.competition.api.CompetitionChangeListener;
import org.lisasp.competition.api.CompetitionDto;
import org.lisasp.competition.api.CreateCompetition;
import org.lisasp.competition.base.api.exception.NotFoundException;

import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;

    private final EntityToDtoMapper mapper = new EntityToDtoMapper();
    private final CompetitionChangeNotifier notifier = new CompetitionChangeNotifier();

    public CompetitionDto create(CreateCompetition createCompetition) {
        CompetitionEntity competition = new CompetitionEntity();
        competition.setName(createCompetition.getName());
        competition.setAcronym(createCompetition.getAcronym());
        competition.setFrom(createCompetition.getFrom());
        competition.setTill(createCompetition.getTill());

        competitionRepository.save(competition);

        CompetitionDto dto = mapper.entityToDto(competition);
        notifier.added(dto);
        return dto;
    }

    public void deleteCompetition(String id) {
        if (id == null) {
            throw new NullPointerException("Id must not be null");
        }
        competitionRepository.findById(id).ifPresent(e -> {
            CompetitionDto dto = mapper.entityToDto(e);
            competitionRepository.deleteById(e.getId());
            notifier.deleted(dto);
        });
    }

    public CompetitionDto findCompetition(String id) throws NotFoundException {
        CompetitionEntity entity = competitionRepository.findById(id).orElseThrow(() -> new NotFoundException("Competition", id));
        return mapper.entityToDto(entity);
    }

    public CompetitionDto[] findCompetitions() {
        return StreamSupport.stream(competitionRepository.findAll().spliterator(), false).map(mapper::entityToDto).toArray(CompetitionDto[]::new);
    }

    public CompetitionDto update(CompetitionDto competition) throws NotFoundException {
        CompetitionEntity entity =
                competitionRepository.findById(competition.id()).orElseThrow(() -> new NotFoundException("Competition", competition.id()));
        entity.setName(competition.name());
        entity.setAcronym(competition.acronym());
        entity.setFrom(competition.from());
        entity.setTill(competition.till());

        competitionRepository.save(entity);

        CompetitionDto dto = mapper.entityToDto(entity);
        notifier.updated(dto);
        return dto;
    }

    public CompetitionService register(CompetitionChangeListener listener) {
        notifier.add(listener);
        return this;
    }
}
