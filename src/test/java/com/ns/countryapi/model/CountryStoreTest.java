package com.ns.countryapi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountryStoreTest {

    private static CountryStore store;

    @BeforeAll
    static void load() throws Exception {
        store = new CountryStore(new ObjectMapper());
    }

    @Test
    void loadsAllRecords() {
        assertThat(store.size()).isEqualTo(250);
    }

    @Test
    void lookupCaseInsensitive() {
        assertThat(store.findByCca3("nld")).isPresent();
        assertThat(store.findByCca3("NLD")).isPresent();
    }

    @Test
    void unknownCodeReturnsEmpty() {
        assertThat(store.findByCca3("ZZZ")).isEmpty();
    }

    @Test
    void nldBordersContainBelAndDeu() {
        Country nld = store.findByCca3("NLD").orElseThrow();
        assertThat(nld.borders()).contains("BEL", "DEU");
    }
}
