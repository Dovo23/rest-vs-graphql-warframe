package de.restvsgraphql.warframe.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WarframeClientConfig {

    @Bean
    RestClient warframeRestClient(
            RestClient.Builder builder,
            @Value("${warframe.api.base-url}") String baseUrl
    ) {
        return builder.baseUrl(baseUrl).build();
    }
}
