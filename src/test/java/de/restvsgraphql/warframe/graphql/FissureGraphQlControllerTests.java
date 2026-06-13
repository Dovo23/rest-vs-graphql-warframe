package de.restvsgraphql.warframe.graphql;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import de.restvsgraphql.warframe.domain.Fissure;
import de.restvsgraphql.warframe.domain.FissureFilter;
import de.restvsgraphql.warframe.service.FissureService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@GraphQlTest(FissureGraphQlController.class)
class FissureGraphQlControllerTests {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private FissureService fissureService;

    @Test
    void fissuresReturnsFissureList() {
        when(fissureService.findFissures(any(FissureFilter.class))).thenReturn(List.of(fissure()));

        graphQlTester.document("""
                        query {
                          fissures {
                            id
                            activation
                            expiry
                            node
                            missionType
                            enemy
                            tier
                            tierNum
                            isStorm
                            isHard
                          }
                        }
                        """)
                .execute()
                .path("fissures[0].id").entity(String.class).isEqualTo("fissure-1")
                .path("fissures[0].activation").entity(String.class).isEqualTo("2026-06-08T09:00:00Z")
                .path("fissures[0].expiry").entity(String.class).isEqualTo("2026-06-08T11:00:00Z")
                .path("fissures[0].node").entity(String.class).isEqualTo("Eurasia (Earth)")
                .path("fissures[0].missionType").entity(String.class).isEqualTo("Mobile Defense")
                .path("fissures[0].enemy").entity(String.class).isEqualTo("Grineer")
                .path("fissures[0].tier").entity(String.class).isEqualTo("Lith")
                .path("fissures[0].tierNum").entity(Integer.class).isEqualTo(1)
                .path("fissures[0].isStorm").entity(Boolean.class).isEqualTo(false)
                .path("fissures[0].isHard").entity(Boolean.class).isEqualTo(true);
    }

    @Test
    void fissuresPassesInputToServiceFilter() {
        when(fissureService.findFissures(any(FissureFilter.class))).thenReturn(List.of());

        graphQlTester.document("""
                        query {
                          fissures(filter: {
                            tier: "Lith"
                            missionType: "Mobile Defense"
                            enemy: "Grineer"
                            activeOnly: true
                          }) {
                            id
                          }
                        }
                        """)
                .execute()
                .path("fissures").entityList(Fissure.class).hasSize(0);

        verify(fissureService).findFissures(new FissureFilter("Lith", "Mobile Defense", "Grineer", true));
    }

    @Test
    void fissureReturnsMatchingFissure() {
        when(fissureService.findById("fissure-1")).thenReturn(Optional.of(fissure()));

        graphQlTester.document("""
                        query {
                          fissure(id: "fissure-1") {
                            id
                            activation
                            expiry
                            node
                            missionType
                            enemy
                            tier
                            tierNum
                            isStorm
                            isHard
                          }
                        }
                        """)
                .execute()
                .path("fissure.id").entity(String.class).isEqualTo("fissure-1")
                .path("fissure.activation").entity(String.class).isEqualTo("2026-06-08T09:00:00Z")
                .path("fissure.expiry").entity(String.class).isEqualTo("2026-06-08T11:00:00Z")
                .path("fissure.node").entity(String.class).isEqualTo("Eurasia (Earth)")
                .path("fissure.missionType").entity(String.class).isEqualTo("Mobile Defense")
                .path("fissure.enemy").entity(String.class).isEqualTo("Grineer")
                .path("fissure.tier").entity(String.class).isEqualTo("Lith")
                .path("fissure.tierNum").entity(Integer.class).isEqualTo(1)
                .path("fissure.isStorm").entity(Boolean.class).isEqualTo(false)
                .path("fissure.isHard").entity(Boolean.class).isEqualTo(true);

        verify(fissureService).findById("fissure-1");
    }

    @Test
    void fissureReturnsNullForUnknownId() {
        when(fissureService.findById("missing")).thenReturn(Optional.empty());

        graphQlTester.document("""
                        query {
                          fissure(id: "missing") {
                            id
                          }
                        }
                        """)
                .execute()
                .path("fissure").valueIsNull();

        verify(fissureService).findById("missing");
    }

    private Fissure fissure() {
        return new Fissure(
                "fissure-1",
                Instant.parse("2026-06-08T09:00:00Z"),
                Instant.parse("2026-06-08T11:00:00Z"),
                "Eurasia (Earth)",
                "Mobile Defense",
                "Grineer",
                "Lith",
                1,
                false,
                true
        );
    }
}
