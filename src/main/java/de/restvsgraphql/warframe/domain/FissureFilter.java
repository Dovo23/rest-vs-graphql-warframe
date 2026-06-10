package de.restvsgraphql.warframe.domain;

public record FissureFilter(
        String tier,
        String missionType,
        String enemy,
        boolean activeOnly
) {
    public static FissureFilter empty() {
        return new FissureFilter(null, null, null, false);
    }
}
