package de.restvsgraphql.warframe.client;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import de.restvsgraphql.warframe.domain.Fissure;

@Component
public class WarframeFissureClient {

    private final RestClient restClient;

    public WarframeFissureClient(RestClient warframeRestClient) {
        this.restClient = warframeRestClient;
    }

    public List<Fissure> fetchFissures() {
        List<WarframeFissureDto> fissures = restClient.get()
                .uri("/fissures/")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (fissures == null) {
            return List.of();
        }

        return fissures.stream()
                .map(WarframeFissureDto::toDomain)
                .toList();
    }
}
