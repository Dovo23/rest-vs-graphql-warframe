package de.restvsgraphql.warframe.api;

public record ApiErrorResponse(
        int status,
        String error,
        String message
) {
}
