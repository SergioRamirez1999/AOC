package me.sergioramirez.days.day14;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Puzzle {
    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day14\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final String[][] ground = new String[input.size()][input.get(0).length()];

    private static void init_ground() {
        for(int h=0; h < input.size(); h++) {
            String line = input.get(h);
            for(int i=0; i < line.length(); i++) {
                ground[h][i] = String.valueOf(line.charAt(i));
            }
        }
    }

    private static long calcule_load() {
        long result = 0;

        for(int y=0; y < ground.length; y++) {
            for(int yb=0; yb < ground.length; yb++) {
                int up = yb-1;
                if(0<=up) {
                    for(int x=0; x < ground[yb].length; x++) {
                        if(ground[yb][x].equals("O") && ground[up][x].equals(".")) {
                            String auxUp = ground[up][x];
                            ground[up][x] = ground[yb][x];
                            ground[yb][x] = auxUp;
                        }
                    }
                }
            }
        }

        for(int y=ground.length-1; y >= 0; y--) {
            result += Arrays.stream(ground[y]).filter(x -> x.equals("O")).count() * (ground.length - y);
        }

        return result;
    }

    public static void main(String[] args) {
        init_ground();
        long resultA = calcule_load();
        System.out.println(resultA);
    }

}
