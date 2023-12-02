package me.sergioramirez.days.day02;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Puzzle {

    private static final Map<String, Integer> color_value_limit = new HashMap<>() {{
        put("red", 12);
        put("green", 13);
        put("blue", 14);
    }};

    private static final List<Game> games = new ArrayList<>();

    private static int determine_possibles_A(List<String> input) {
        int i = 1;
        for(String line: input) {
            String[] game_sets = line.split(":");
            String[] setsArr = game_sets[1].split(";");
            List<String> sets = Arrays.stream(setsArr).map(String::trim).collect(Collectors.toList());
            games.add(new Game(i, sets));
            i++;
        }

        return games.stream().filter(g -> g.is_possible_A(color_value_limit)).mapToInt(Game::getId).sum();
    }

    private static int sum_power_set() {
        return games.stream().mapToInt(Game::calcule_power).sum();
    }

    public static void main(String[] args) {
        String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day02\\input.txt";
        List<String> input = InputTextUtil.read_all_lines(filepath);
        int result_A = determine_possibles_A(input);
        System.out.println(result_A);

        int result_B = sum_power_set();
        System.out.println(result_B);

    }

}
