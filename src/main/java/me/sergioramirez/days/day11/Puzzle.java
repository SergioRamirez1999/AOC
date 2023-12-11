package me.sergioramirez.days.day11;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day11\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final List<Coordinate> galaxies = new ArrayList<>();
    private static List<Integer> expandedRows = new ArrayList<>();
    private static List<Integer> expandedCols = new ArrayList<>();

    private static void init_cosmos() {
        for(int i=0; i<input.size(); i++) {
            for(int h=0; h<input.get(i).length(); h++) {
                if(input.get(i).charAt(h) != '#')
                    continue;
                galaxies.add(new Coordinate(h, i));
            }
        }
        expandedCols = IntStream.range(0, input.size()).filter(v -> Arrays.stream(input.get(v).split("")).allMatch(x -> x.equals("."))).boxed().collect(Collectors.toList());
        expandedRows = IntStream.range(0, input.get(0).length()).filter(y -> IntStream.range(0, input.get(y).length()).allMatch(x -> input.get(x).charAt(y) == '.')).boxed().collect(Collectors.toList());
    }

    private static Long sum_distances_A() {
        List<Coordinate> auxGalaxies = new ArrayList<>(galaxies);
        Long sum = 0L;
        for(Coordinate actualGalaxy: galaxies) {
            auxGalaxies.remove(actualGalaxy);
            for(int h=0; h < auxGalaxies.size(); h++) {
                Coordinate auxGalaxy = auxGalaxies.get(h);
                sum += Math.abs(actualGalaxy.getX()-auxGalaxy.getX()) + Math.abs(actualGalaxy.getY()-auxGalaxy.getY());
                sum += expandedRows.stream().filter(v -> is_between(actualGalaxy.getX(), auxGalaxy.getX(), v)).count();
                sum += expandedCols.stream().filter(v -> is_between(actualGalaxy.getY(), auxGalaxy.getY(), v)).count();
            }

        }
        return sum;
    }

    private static Long sum_distances_B() {
        List<Coordinate> auxGalaxies = new ArrayList<>(galaxies);
        Long sum = 0L;
        for(Coordinate actualGalaxy: galaxies) {
            auxGalaxies.remove(actualGalaxy);
            for(int h=0; h < auxGalaxies.size(); h++) {
                Coordinate auxGalaxy = auxGalaxies.get(h);
                sum += Math.abs(actualGalaxy.getX()-auxGalaxy.getX()) + Math.abs(actualGalaxy.getY()-auxGalaxy.getY());
                sum += expandedRows.stream().filter(v -> is_between(actualGalaxy.getX(), auxGalaxy.getX(), v)).count() * 999999;
                sum += expandedCols.stream().filter(v -> is_between(actualGalaxy.getY(), auxGalaxy.getY(), v)).count() * 999999;
            }

        }
        return sum;
    }

    private static boolean is_between(int v1, int v2, int v3) {
        return v3 > Math.min(v1, v2) && v3 < Math.max(v1, v2);
    }

    public static void main(String[] args) {
        init_cosmos();
        Long result_A = sum_distances_A();
        System.out.println(result_A);
        Long result_B = sum_distances_B();
        System.out.println(result_B);
    }
}
