package de.restvsgraphql.warframe.graphql;

import de.restvsgraphql.warframe.domain.FissureFilter;

public record FissureFilterInput(
        String tier,
        String missionType,
        String enemy,
        Boolean activeOnly
) {
    FissureFilter toDomainFilter() {
        return new FissureFilter(
                tier,
                missionType,
                enemy,
                Boolean.TRUE.equals(activeOnly)
        );
    }
}
