package de.restvsgraphql.warframe.api;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import de.restvsgraphql.warframe.domain.Fissure;
import de.restvsgraphql.warframe.domain.FissureFilter;
import de.restvsgraphql.warframe.service.FissureService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FissureRestController.class)
class FissureRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FissureService fissureService;

    @Test
    void findFissuresReturnsFissureList() throws Exception {
        when(fissureService.findFissures(any(FissureFilter.class))).thenReturn(List.of(fissure()));

        mockMvc.perform(get("/api/rest/fissures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("fissure-1"))
                .andExpect(jsonPath("$[0].activation").value("2026-06-08T09:00:00Z"))
                .andExpect(jsonPath("$[0].expiry").value("2026-06-08T11:00:00Z"))
                .andExpect(jsonPath("$[0].node").value("Eurasia (Earth)"))
                .andExpect(jsonPath("$[0].missionType").value("Mobile Defense"))
                .andExpect(jsonPath("$[0].enemy").value("Grineer"))
                .andExpect(jsonPath("$[0].tier").value("Lith"))
                .andExpect(jsonPath("$[0].tierNum").value(1))
                .andExpect(jsonPath("$[0].isStorm").value(false))
                .andExpect(jsonPath("$[0].isHard").value(true));
    }

    @Test
    void findFissuresPassesQueryParametersToServiceFilter() throws Exception {
        when(fissureService.findFissures(any(FissureFilter.class))).thenReturn(List.of());

        mockMvc.perform(get("/api/rest/fissures")
                        .param("tier", "Lith")
                        .param("missionType", "Mobile Defense")
                        .param("enemy", "Grineer")
                        .param("activeOnly", "true"))
                .andExpect(status().isOk());

        verify(fissureService).findFissures(new FissureFilter("Lith", "Mobile Defense", "Grineer", true));
    }

    @Test
    void findFissureByIdReturnsMatchingFissure() throws Exception {
        when(fissureService.findById("fissure-1")).thenReturn(Optional.of(fissure()));

        mockMvc.perform(get("/api/rest/fissures/fissure-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("fissure-1"))
                .andExpect(jsonPath("$.activation").value("2026-06-08T09:00:00Z"))
                .andExpect(jsonPath("$.expiry").value("2026-06-08T11:00:00Z"))
                .andExpect(jsonPath("$.node").value("Eurasia (Earth)"))
                .andExpect(jsonPath("$.missionType").value("Mobile Defense"))
                .andExpect(jsonPath("$.enemy").value("Grineer"))
                .andExpect(jsonPath("$.tier").value("Lith"))
                .andExpect(jsonPath("$.tierNum").value(1))
                .andExpect(jsonPath("$.isStorm").value(false))
                .andExpect(jsonPath("$.isHard").value(true));

        verify(fissureService).findById("fissure-1");
    }

    @Test
    void findFissureByIdReturnsNotFoundForUnknownId() throws Exception {
        when(fissureService.findById("missing")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rest/fissures/missing"))
                .andExpect(status().isNotFound());

        verify(fissureService).findById("missing");
    }

    @Test
    void findFissuresReturnsServiceUnavailableWhenFissureDataCannotBeLoaded() throws Exception {
        when(fissureService.findFissures(any(FissureFilter.class)))
                .thenThrow(new RestClientException("Upstream unavailable"));

        mockMvc.perform(get("/api/rest/fissures"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.error").value("Service Unavailable"))
                .andExpect(jsonPath("$.message").value("Fissure data is currently unavailable."));
    }

    @Test
    void findFissureByIdReturnsServiceUnavailableWhenFissureDataCannotBeLoaded() throws Exception {
        when(fissureService.findById("fissure-1"))
                .thenThrow(new RestClientException("Upstream unavailable"));

        mockMvc.perform(get("/api/rest/fissures/fissure-1"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.error").value("Service Unavailable"))
                .andExpect(jsonPath("$.message").value("Fissure data is currently unavailable."));
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
