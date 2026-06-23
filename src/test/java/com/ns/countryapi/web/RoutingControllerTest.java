package com.ns.countryapi.web;

import com.ns.countryapi.model.Country;
import com.ns.countryapi.model.CountryStore;
import com.ns.countryapi.model.RouteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoutingController.class)
class RoutingControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    CountryStore store;

    @MockBean
    RouteService routeService;

    private static final Country NLD = new Country("NLD", List.of("BEL", "DEU"));
    private static final Country POL = new Country("POL", List.of("BLR", "CZE", "DEU", "LTU", "RUS", "SVK", "UKR"));
    private static final Country AUS = new Country("AUS", List.of());

    @Test
    void knownRouteReturns200WithRouteArray() throws Exception {
        when(store.findByCca3("NLD")).thenReturn(Optional.of(NLD));
        when(store.findByCca3("POL")).thenReturn(Optional.of(POL));
        when(routeService.findRoute("NLD", "POL")).thenReturn(List.of("NLD", "DEU", "POL"));

        mvc.perform(get("/routing/NLD/POL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route[0]").value("NLD"))
                .andExpect(jsonPath("$.route[1]").value("DEU"))
                .andExpect(jsonPath("$.route[2]").value("POL"));
    }

    @Test
    void unknownOriginReturns400() throws Exception {
        when(store.findByCca3("ZZZ")).thenReturn(Optional.empty());
        when(store.findByCca3("POL")).thenReturn(Optional.of(POL));

        mvc.perform(get("/routing/ZZZ/POL"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void noLandRouteReturns400() throws Exception {
        when(store.findByCca3("NLD")).thenReturn(Optional.of(NLD));
        when(store.findByCca3("AUS")).thenReturn(Optional.of(AUS));
        when(routeService.findRoute("NLD", "AUS")).thenReturn(List.of());

        mvc.perform(get("/routing/NLD/AUS"))
                .andExpect(status().isBadRequest());
    }
}
