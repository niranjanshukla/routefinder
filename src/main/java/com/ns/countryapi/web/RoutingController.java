package com.ns.countryapi.web;

import com.ns.countryapi.model.CountryStore;
import com.ns.countryapi.model.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/routing")
public class RoutingController {

    private final CountryStore store;
    private final RouteService routeService;

    public RoutingController(CountryStore store, RouteService routeService) {
        this.store = store;
        this.routeService = routeService;
    }

    @GetMapping("/{origin}/{destination}")
    public ResponseEntity<RouteResponse> route(
            @PathVariable String origin,
            @PathVariable String destination) {

        String o = origin.toUpperCase();
        String d = destination.toUpperCase();

        if (store.findByCca3(o).isEmpty() || store.findByCca3(d).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<String> path = routeService.findRoute(o, d);
        if (path.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new RouteResponse(path));
    }
}
