package de.restvsgraphql.warframe.service;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import de.restvsgraphql.warframe.client.WarframeFissureClient;
import de.restvsgraphql.warframe.domain.Fissure;
import de.restvsgraphql.warframe.domain.FissureFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FissureServiceTests {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-06-08T10:00:00Z"),
            ZoneOffset.UTC
    );

    @Test
    void findFissuresUsesCachedClientDataWithinTtl() {
        WarframeFissureClient client = mock(WarframeFissureClient.class);
        Fissure fissure = fissure("fissure-1");
        when(client.fetchFissures()).thenReturn(List.of(fissure));

        FissureService service = new FissureService(client, 60, FIXED_CLOCK);

        List<Fissure> firstCall = service.findFissures(FissureFilter.empty());
        List<Fissure> secondCall = service.findFissures(FissureFilter.empty());

        assertThat(firstCall).containsExactly(fissure);
        assertThat(secondCall).containsExactly(fissure);
        verify(client, times(1)).fetchFissures();
    }

    @Test
    void findByIdReturnsMatchingFissureFromCache() {
        WarframeFissureClient client = mock(WarframeFissureClient.class);
        Fissure matchingFissure = fissure("fissure-1");
        Fissure otherFissure = fissure("fissure-2");
        when(client.fetchFissures()).thenReturn(List.of(matchingFissure, otherFissure));

        FissureService service = new FissureService(client, 60, FIXED_CLOCK);

        Optional<Fissure> result = service.findById("fissure-1");

        assertThat(result).contains(matchingFissure);
        verify(client, times(1)).fetchFissures();
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        WarframeFissureClient client = mock(WarframeFissureClient.class);
        when(client.fetchFissures()).thenReturn(List.of(fissure("fissure-1")));

        FissureService service = new FissureService(client, 60, FIXED_CLOCK);

        Optional<Fissure> result = service.findById("missing");

        assertThat(result).isEmpty();
        verify(client, times(1)).fetchFissures();
    }

    @Test
    void findFissuresFiltersByTierIgnoringCase() {
        WarframeFissureClient client = mock(WarframeFissureClient.class);
        Fissure lithFissure = fissure("fissure-1", "Lith", "Mobile Defense", "Grineer", "2026-06-08T11:00:00Z");
        Fissure axiFissure = fissure("fissure-2", "Axi", "Extermination", "Corpus", "2026-06-08T11:00:00Z");
        when(client.fetchFissures()).thenReturn(List.of(lithFissure, axiFissure));

        FissureService service = new FissureService(client, 60, FIXED_CLOCK);

        List<Fissure> result = service.findFissures(new FissureFilter("lith", null, null, false));

        assertThat(result).containsExactly(lithFissure);
    }

    @Test
    void findFissuresFiltersByMissionTypeIgnoringCase() {
        WarframeFissureClient client = mock(WarframeFissureClient.class);
        Fissure mobileDefenseFissure = fissure("fissure-1", "Lith", "Mobile Defense", "Grineer", "2026-06-08T11:00:00Z");
        Fissure exterminationFissure = fissure("fissure-2", "Axi", "Extermination", "Corpus", "2026-06-08T11:00:00Z");
        when(client.fetchFissures()).thenReturn(List.of(mobileDefenseFissure, exterminationFissure));

        FissureService service = new FissureService(client, 60, FIXED_CLOCK);

        List<Fissure> result = service.findFissures(new FissureFilter(null, "extermination", null, false));

        assertThat(result).containsExactly(exterminationFissure);
    }

    @Test
    void findFissuresFiltersByEnemyIgnoringCase() {
        WarframeFissureClient client = mock(WarframeFissureClient.class);
        Fissure grineerFissure = fissure("fissure-1", "Lith", "Mobile Defense", "Grineer", "2026-06-08T11:00:00Z");
        Fissure corpusFissure = fissure("fissure-2", "Axi", "Extermination", "Corpus", "2026-06-08T11:00:00Z");
        when(client.fetchFissures()).thenReturn(List.of(grineerFissure, corpusFissure));

        FissureService service = new FissureService(client, 60, FIXED_CLOCK);

        List<Fissure> result = service.findFissures(new FissureFilter(null, null, "corpus", false));

        assertThat(result).containsExactly(corpusFissure);
    }

    @Test
    void findFissuresFiltersExpiredFissuresWhenActiveOnlyIsEnabled() {
        WarframeFissureClient client = mock(WarframeFissureClient.class);
        Fissure activeFissure = fissure("fissure-1", "Lith", "Mobile Defense", "Grineer", "2026-06-08T11:00:00Z");
        Fissure expiredFissure = fissure("fissure-2", "Axi", "Extermination", "Corpus", "2026-06-08T09:30:00Z");
        when(client.fetchFissures()).thenReturn(List.of(activeFissure, expiredFissure));

        FissureService service = new FissureService(client, 60, FIXED_CLOCK);

        List<Fissure> result = service.findFissures(new FissureFilter(null, null, null, true));

        assertThat(result).containsExactly(activeFissure);
    }

    private Fissure fissure(String id) {
        return fissure(id, "Lith", "Mobile Defense", "Grineer", "2026-06-08T11:00:00Z");
    }

    private Fissure fissure(String id, String tier, String missionType, String enemy, String expiry) {
        return new Fissure(
                id,
                Instant.parse("2026-06-08T09:00:00Z"),
                Instant.parse(expiry),
                "Eurasia (Earth)",
                missionType,
                enemy,
                tier,
                1,
                false,
                false
        );
    }
}
