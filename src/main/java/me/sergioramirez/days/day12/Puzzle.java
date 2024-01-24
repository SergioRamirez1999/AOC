package me.sergioramirez.days.day12;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day12\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);

    private static final Map<String, Long> memory = new HashMap<>();

    private static long count(String left, List<Integer> nums) {
        if(left.isEmpty())
            return nums.isEmpty() ? 1L : 0L;

        if(nums.isEmpty())
            return left.contains("#") ? 0L : 1L;

        String key = left + nums.stream().map(String::valueOf).collect(Collectors.joining());

        if(memory.containsKey(key))
            return memory.get(key);

        long result = 0L;

        if(".?".contains(left.substring(0, 1)))
            result += count(left.substring(1), nums);

        if("#?".contains(left.substring(0, 1))) {
            if(nums.get(0) <= left.length()
                    && !left.substring(0, nums.get(0)).contains(".")
                    && (nums.get(0) == left.length() || left.charAt(nums.get(0)) != '#')) {
                String remaining = nums.get(0)+1 < left.length() ? left.substring(nums.get(0)+1) : "";
                result += count(remaining, nums.subList(1, nums.size()));
            }
        }

        memory.put(key, result);

        return result;
    }

    private static long different_arrangements(int times) {
        long count = 0;

        for(String line: input) {
            String[] arr = line.split(" ");
            String left = Stream.generate(() -> arr[0]).limit(times).collect(Collectors.joining("?"));
            List<Integer> nums = Stream.generate(() -> Arrays.stream(arr[1].split(",")).map(Integer::parseInt).toList()).limit(times).flatMap(Collection::stream).toList();
            count += count(left, nums);
        }

        return count;
    }

    public static void main(String[] args) {
        long result_A = different_arrangements(1);
        System.out.println(result_A);
//        long result_B = different_arrangements(5);
//        System.out.println(result_B);
    }
}
