package de.restvsgraphql.warframe.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import de.restvsgraphql.warframe.client.WarframeFissureClient;
import de.restvsgraphql.warframe.domain.Fissure;
import de.restvsgraphql.warframe.domain.FissureFilter;

@Service
public class FissureService {

    private final WarframeFissureClient warframeFissureClient;
    private final Duration cacheTtl;
    private final Clock clock;

    private List<Fissure> cachedFissures = List.of();
    private Instant cachedAt;

    public FissureService(
            WarframeFissureClient warframeFissureClient,
            @Value("${warframe.cache.ttl-seconds}") long cacheTtlSeconds,
            Clock clock
    ) {
        this.warframeFissureClient = warframeFissureClient;
        this.cacheTtl = Duration.ofSeconds(cacheTtlSeconds);
        this.clock = clock;
    }

    public List<Fissure> findFissures(FissureFilter filter) {
        FissureFilter effectiveFilter = filter == null ? FissureFilter.empty() : filter;

        return getCachedFissures().stream()
                .filter(fissure -> matches(fissure, effectiveFilter))
                .toList();
    }

    public Optional<Fissure> findById(String id) {
        if (!StringUtils.hasText(id)) {
            return Optional.empty();
        }

        return getCachedFissures().stream()
                .filter(fissure -> id.equals(fissure.id()))
                .findFirst();
    }

    private synchronized List<Fissure> getCachedFissures() {
        Instant now = clock.instant();
        if (cachedAt == null || !cachedAt.plus(cacheTtl).isAfter(now)) {
            cachedFissures = List.copyOf(warframeFissureClient.fetchFissures());
            cachedAt = now;
        }
        return cachedFissures;
    }

    private boolean matches(Fissure fissure, FissureFilter filter) {
        return matchesIgnoreCase(filter.tier(), fissure.tier())
                && matchesIgnoreCase(filter.missionType(), fissure.missionType())
                && matchesIgnoreCase(filter.enemy(), fissure.enemy())
                && (!filter.activeOnly() || fissure.expiry().isAfter(clock.instant()));
    }

    private boolean matchesIgnoreCase(String expected, String actual) {
        return !StringUtils.hasText(expected) || expected.equalsIgnoreCase(actual);
    }
}
