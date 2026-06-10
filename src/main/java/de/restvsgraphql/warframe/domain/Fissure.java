package de.restvsgraphql.warframe.domain;

import java.time.Instant;

public record Fissure(
        String id,
        Instant activation,
        Instant expiry,
        String node,
        String missionType,
        String enemy,
        String tier,
        Integer tierNum,
        boolean storm,
        boolean hard
) {
}
