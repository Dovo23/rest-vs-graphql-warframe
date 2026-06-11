package de.restvsgraphql.warframe.api;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.restvsgraphql.warframe.domain.Fissure;

public record RestFissureResponse(
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
    public static RestFissureResponse from(Fissure fissure) {
        return new RestFissureResponse(
                fissure.id(),
                fissure.activation(),
                fissure.expiry(),
                fissure.node(),
                fissure.missionType(),
                fissure.enemy(),
                fissure.tier(),
                fissure.tierNum(),
                fissure.storm(),
                fissure.hard()
        );
    }
}
