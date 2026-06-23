package com.ns.countryapi.model;

import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Service
public class RouteService {

    private final CountryStore store;

    public RouteService(CountryStore store) {
        this.store = store;
    }

    /**
     * BFS Algorithm -- Returns the shortest land-border path as an ordered list of cca3 codes, or an empty list when no land route exists
     */
    public List<String> findRoute(String src, String dest) {

        if (src.equals(dest)) {
            return List.of(src);
        }

        Queue<String> dq = new ArrayDeque<>();
        Map<String, String> parent = new HashMap<>();

        dq.add(src);
        parent.put(src, null);

        // Still nodes to traverse?
        while (!dq.isEmpty()) {
            String current = dq.poll();

            List<String> borders = store.findByCca3(current)
                    .map(Country::borders)
                    .orElse(List.of());

            for (String next : borders) {
                if (parent.containsKey(next)) continue;

                // Traverse and add parent
                parent.put(next, current);

                if (next.equals(dest)) {
                    return rebuild(parent, dest);
                }
                dq.add(next);
            }
        }
        return List.of();
    }

    private List<String> rebuild(Map<String, String> parent, String end) {
        LinkedList<String> path = new LinkedList<>();
        for (String at = end; at != null; at = parent.get(at)) {
            path.addFirst(at);
        }
        return path;
    }
}
