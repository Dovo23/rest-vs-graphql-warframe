package de.restvsgraphql.warframe.graphql;

import java.time.Instant;
import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import de.restvsgraphql.warframe.domain.Fissure;
import de.restvsgraphql.warframe.domain.FissureFilter;
import de.restvsgraphql.warframe.service.FissureService;

@Controller
public class FissureGraphQlController {

    private final FissureService fissureService;

    public FissureGraphQlController(FissureService fissureService) {
        this.fissureService = fissureService;
    }

    @QueryMapping
    public List<Fissure> fissures(@Argument FissureFilterInput filter) {
        FissureFilter domainFilter = filter == null ? FissureFilter.empty() : filter.toDomainFilter();
        return fissureService.findFissures(domainFilter);
    }

    @QueryMapping
    public Fissure fissure(@Argument String id) {
        return fissureService.findById(id).orElse(null);
    }

    @SchemaMapping(typeName = "Fissure", field = "activation")
    public String activation(Fissure fissure) {
        return formatInstant(fissure.activation());
    }

    @SchemaMapping(typeName = "Fissure", field = "expiry")
    public String expiry(Fissure fissure) {
        return formatInstant(fissure.expiry());
    }

    @SchemaMapping(typeName = "Fissure", field = "isStorm")
    public boolean isStorm(Fissure fissure) {
        return fissure.storm();
    }

    @SchemaMapping(typeName = "Fissure", field = "isHard")
    public boolean isHard(Fissure fissure) {
        return fissure.hard();
    }

    private String formatInstant(Instant instant) {
        return instant.toString();
    }
}
