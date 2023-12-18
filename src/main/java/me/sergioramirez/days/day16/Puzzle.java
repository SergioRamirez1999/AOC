package me.sergioramirez.days.day16;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.IntStream;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day16\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final Contraption contraption = init_contraption();

    private static Contraption init_contraption() {
        String[][] grid = new String[input.size()][input.get(0).length()];
        IntStream.range(0, input.size()).forEach(i -> grid[i] = input.get(i).split(""));
        return new Contraption(grid);
    }

    public static void main(String[] args) {
        int result_A = contraption.bounce_light(-1, 0, Direction.RIGHT);
        System.out.println(result_A);
        int result_B = contraption.max_bounce_light();
        System.out.println(result_B);
    }
}
