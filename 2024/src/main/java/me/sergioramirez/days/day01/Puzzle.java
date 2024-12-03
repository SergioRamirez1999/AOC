package me.sergioramirez.days.day01;

import me.sergioramirez.util.InputTextUtil;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Puzzle
{

    private static void init_lists(List<String> input, List<Integer> leftList, List<Integer> rightList) {
        input.forEach(
                l -> {
                    String[] arr = l.split("\\s{3}");
                    leftList.add(Integer.valueOf(arr[0]));
                    rightList.add(Integer.valueOf(arr[1]));
                }
        );
    }

    public static int total_distance(List<String> input) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();

        init_lists(input, leftList, rightList);

        leftList.sort(Integer::compareTo);
        rightList.sort(Integer::compareTo);

        return IntStream.range(0, leftList.size()).map(i -> Math.abs(leftList.get(i) - rightList.get(i))).sum();
    }

    public static int total_similiraty(List<String> input) {
        List<Integer> leftList = new ArrayList<>();
        List<Integer> rightList = new ArrayList<>();
        Map<Integer, Integer> number_occurences = new HashMap<>();

        init_lists(input, leftList, rightList);

        return leftList.stream().mapToInt(nLeft -> {
            Integer ocurrences = number_occurences.getOrDefault(nLeft, -1);
            if (ocurrences == -1)
                ocurrences = (int) rightList.stream().filter(rN -> rN.equals(nLeft)).count();
            else
                number_occurences.put(nLeft, ocurrences);
            return nLeft * ocurrences;
        }).sum();
    }



    public static void main( String[] args ) {
        String filepath = Paths.get("").toAbsolutePath()+"\\2024\\src\\main\\java\\me\\sergioramirez\\days\\day01\\input.txt";
        List<String> input_lines = InputTextUtil.read_all_lines(filepath);
        int result_A = total_distance(input_lines);
        int result_B = total_similiraty(input_lines);
        System.out.println(result_A);
        System.out.println(result_B);
    }
}
