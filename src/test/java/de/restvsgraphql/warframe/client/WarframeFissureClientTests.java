package de.restvsgraphql.warframe.client;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import de.restvsgraphql.warframe.domain.Fissure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class WarframeFissureClientTests {

    private static final String BASE_URL = "https://api.warframestat.us/pc";

    @Test
    void fetchFissuresMapsApiResponseToDomainModel() {
        RestClient.Builder builder = RestClient.builder().baseUrl(BASE_URL);
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        WarframeFissureClient client = new WarframeFissureClient(builder.build());

        server.expect(requestTo(BASE_URL + "/fissures/"))
                .andRespond(withSuccess("""
                        [
                          {
                            "id": "6a2683de35243a2e2b8ce5b1",
                            "activation": "2026-06-08T08:57:02.368Z",
                            "expiry": "2026-06-08T10:26:41.268Z",
                            "node": "Eurasia (Earth)",
                            "missionType": "Mobile Defense",
                            "enemy": "Grineer",
                            "tier": "Lith",
                            "tierNum": 1,
                            "isStorm": false,
                            "isHard": true
                          }
                        ]
                        """, MediaType.APPLICATION_JSON));

        List<Fissure> fissures = client.fetchFissures();

        assertThat(fissures).hasSize(1);
        Fissure fissure = fissures.getFirst();
        assertThat(fissure.id()).isEqualTo("6a2683de35243a2e2b8ce5b1");
        assertThat(fissure.node()).isEqualTo("Eurasia (Earth)");
        assertThat(fissure.missionType()).isEqualTo("Mobile Defense");
        assertThat(fissure.enemy()).isEqualTo("Grineer");
        assertThat(fissure.tier()).isEqualTo("Lith");
        assertThat(fissure.tierNum()).isEqualTo(1);
        assertThat(fissure.storm()).isFalse();
        assertThat(fissure.hard()).isTrue();
        assertThat(fissure.activation().toString()).isEqualTo("2026-06-08T08:57:02.368Z");
        assertThat(fissure.expiry().toString()).isEqualTo("2026-06-08T10:26:41.268Z");
        server.verify();
    }
}
