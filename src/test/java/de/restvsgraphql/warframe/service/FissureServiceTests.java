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

    private Fissure fissure(String id) {
        return new Fissure(
                id,
                Instant.parse("2026-06-08T09:00:00Z"),
                Instant.parse("2026-06-08T11:00:00Z"),
                "Eurasia (Earth)",
                "Mobile Defense",
                "Grineer",
                "Lith",
                1,
                false,
                false
        );
    }
}
