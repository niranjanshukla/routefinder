package com.ns.countryapi.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CountryStore {

    private final Map<String, Country> byCode;

    public CountryStore(ObjectMapper mapper) throws IOException {
        try (InputStream is = new ClassPathResource("countries.json").getInputStream()) {
            List<Country> list = mapper.readValue(is, new TypeReference<>() {});
            byCode = list.stream()
                    .collect(Collectors.toUnmodifiableMap(
                            c -> c.cca3().toUpperCase(),
                            Function.identity()));
        }
    }

    public Optional<Country> findByCca3(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(byCode.get(code.toUpperCase()));
    }

    public Map<String, Country> all() {
        return byCode;
    }

    public int size() {
        return byCode.size();
    }
}
