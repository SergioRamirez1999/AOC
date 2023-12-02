package me.sergioramirez.days.day02;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Game {

    private int id;
    private List<String> sets; //[2 blue, 2 red, 5 blue]
    private List<Map<String, Integer>> color_value_set = new ArrayList<>(); //[{blue -> 7}, {red -> 2}]

    public Game(int id, List<String> sets) {
        this.id = id;
        this.sets = sets;
        sets.stream().map(s -> Arrays.stream(s.split(",")).map(String::trim).collect(Collectors.toList())).flatMap(Collection::stream).forEach( v_c -> {
            String[] value_color = v_c.split(" ");
            color_value_set.add(Collections.singletonMap(value_color[1], Integer.valueOf(value_color[0])));
        });
    }

    public boolean is_possible_A(Map<String, Integer> color_value_limit) {
        return !color_value_set.stream().flatMap(cvs -> cvs.entrySet().stream()).anyMatch(kv -> kv.getValue() > color_value_limit.get(kv.getKey()));
    }

    public Integer calcule_power() {

        Map<String, Integer> min_cubes = new HashMap<>() {{
            put("red", 0);
            put("green", 0);
            put("blue", 0);
        }};

        for(Map<String, Integer> set: color_value_set) {
            for(Map.Entry<String, Integer> cube: set.entrySet()) {
                if(min_cubes.get(cube.getKey()) < cube.getValue())
                    min_cubes.put(cube.getKey(), cube.getValue());
            }
        }

        return min_cubes.values().stream().reduce((acc,v) -> acc * v ).get();
    }


}
