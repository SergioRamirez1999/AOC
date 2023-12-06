package me.sergioramirez.days.day06;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day06\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final List<Race> races = new ArrayList<>();

    private static void init_races() {
        List<String> times = Arrays.stream(input.get(0).split(":")[1].split("\\s+")).filter(l -> !l.isEmpty()).collect(Collectors.toList());
        List<String> distances = Arrays.stream(input.get(1).split(":")[1].split("\\s+")).filter(l -> !l.isEmpty()).collect(Collectors.toList());
        races.addAll(IntStream.range(0, times.size()).mapToObj(i -> new Race(Long.parseLong(times.get(i)), Long.parseLong(distances.get(i)))).toList());
    }

    private static Long ways_beat_record_A() {
        return races.stream().map(Puzzle::calcule_ways).reduce(1L, (acc, v) -> acc*v);
    }

    private static Long ways_beat_record_B() {
        Long time = races.stream().map(x -> x.getTime().toString()).reduce((acc, v) -> acc+v).map(Long::parseLong).get();
        Long distance = races.stream().map(x -> x.getDistance().toString()).reduce((acc, v) -> acc+v).map(Long::parseLong).get();
        Race uniqueRace = new Race(time, distance);
        return calcule_ways(uniqueRace);
    }


    private static Long calcule_ways(Race race) {
        Long[] solutions = bascara(race.getTime(), -(race.getDistance()));
        return 1L+solutions[1] - solutions[0];
    }
    private static Long[] bascara(Long b, Long c) {
        Long a = -1L;
        double discriminante = (Math.pow(b, 2) - (4 * a * c));
        if (discriminante >= 0) {
            Long soluciones[];
            if(discriminante == 0){
                soluciones = new Long[1];
                soluciones[0] = ((-b) - (4 * a * c)) / (2 * a);
            }else{
                soluciones = new Long[2];
                soluciones[0] = (long) Math.floor(((-b) + Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a))+1;
                soluciones[1] = (long) Math.ceil(((-b) - Math.sqrt(Math.pow(b, 2) - (4 * a * c))) / (2 * a))-1;
            }
            return soluciones;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        init_races();
        System.out.println(ways_beat_record_A());
        System.out.println(ways_beat_record_B());
    }
}
