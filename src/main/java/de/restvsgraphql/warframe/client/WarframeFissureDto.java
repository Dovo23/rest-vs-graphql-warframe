package de.restvsgraphql.warframe.client;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.restvsgraphql.warframe.domain.Fissure;

record WarframeFissureDto(
        String id,
        Instant activation,
        Instant expiry,
        String node,
        String missionType,
        String enemy,
        String tier,
        Integer tierNum,
        @JsonProperty("isStorm") boolean storm,
        @JsonProperty("isHard") boolean hard
) {
    Fissure toDomain() {
        return new Fissure(
                id,
                activation,
                expiry,
                node,
                missionType,
                enemy,
                tier,
                tierNum,
                storm,
                hard
        );
    }
}
