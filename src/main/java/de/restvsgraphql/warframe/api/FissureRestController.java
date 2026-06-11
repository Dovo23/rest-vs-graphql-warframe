package de.restvsgraphql.warframe.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.restvsgraphql.warframe.domain.FissureFilter;
import de.restvsgraphql.warframe.service.FissureService;

@RestController
@RequestMapping("/api/rest/fissures")
public class FissureRestController {

    private final FissureService fissureService;

    public FissureRestController(FissureService fissureService) {
        this.fissureService = fissureService;
    }

    @GetMapping
    public List<RestFissureResponse> findFissures(
            @RequestParam(required = false) String tier,
            @RequestParam(required = false) String missionType,
            @RequestParam(required = false) String enemy,
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        FissureFilter filter = new FissureFilter(tier, missionType, enemy, activeOnly);

        return fissureService.findFissures(filter).stream()
                .map(RestFissureResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestFissureResponse> findFissureById(@PathVariable String id) {
        return fissureService.findById(id)
                .map(RestFissureResponse::from)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
