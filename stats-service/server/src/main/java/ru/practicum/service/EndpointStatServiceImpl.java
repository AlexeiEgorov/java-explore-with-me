package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointStat;
import ru.practicum.EndpointStatRepository;
import ru.practicum.model.EndpointHitStat;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EndpointStatServiceImpl implements EndpointStatService {
    private final EndpointStatRepository repository;

    @Override
    public void addEndpointHitStat(EndpointHitStat endpointHitStat) {
        repository.save(endpointHitStat);
    }

    @Override
    public List<EndpointStat> getStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        if (unique) {
            return (uris == null) ? repository.getStatsByUniqueIps(start, end) :
                    repository.getStatsByUniqueIpsWithUris(start, end, uris);
        } else {
            return (uris == null) ? repository.getStats(start, end) :
                    repository.getStatsWithUris(start, end, uris);
        }
    }
}
