package ru.practicum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHitStat;
import ru.practicum.model.EndpointStat;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointStatRepository extends JpaRepository<EndpointHitStat, Long> {
    @Query(value = "select app, uri, count(uri) as hits from endpoint_hits s " +
            "where s.request_t between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by hits desc", nativeQuery = true)
    List<EndpointStat> getStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "select app, uri, count(uri) as hits from endpoint_hits s " +
            "where s.uri in (?3) and s.request_t between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by hits desc", nativeQuery = true)
    List<EndpointStat> getStatsWithUris(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query(value = "select app, uri, count(uri) as hits from " +
            "(select app, uri, ip from endpoint_hits s " +
            "where s.request_t between ?1 and ?2 " +
            "group by s.app, s.uri, s.ip) s2 " +
            "group by s2.app, s2.uri " +
            "order by hits desc", nativeQuery = true)
    List<EndpointStat> getStatsByUniqueIps(LocalDateTime start, LocalDateTime end);

    @Query(value = "select app, uri, count(uri) as hits from " +
            "(select app, uri, ip from endpoint_hits s " +
            "where s.uri in (?3) and s.request_t between ?1 and ?2 " +
            "group by s.app, s.uri, s.ip) s2 " +
            "group by s2.app, s2.uri " +
            "order by hits desc", nativeQuery = true)
    List<EndpointStat> getStatsByUniqueIpsWithUris(LocalDateTime start, LocalDateTime end, String[] uris);
}
