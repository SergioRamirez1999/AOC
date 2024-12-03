package me.sergioramirez.days.day15;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.LongStream;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day15\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);

    private static final List<String> steps = Arrays.asList(input.get(0).split(","));

    private static final LinkedHashMap<Integer, LinkedHashMap<String, Integer>> boxes = new LinkedHashMap<>();

    private static Long sum_focusing_power() {
        long result = 0L;
        for(String step: steps) {
            String[] label_focal = step.split("=").length == 1 ? step.split("-") : step.split("=");
            int hash = hash_algorithm(label_focal[0].toCharArray());
            if(!boxes.containsKey(hash))
                boxes.put(hash, new LinkedHashMap<>());
            LinkedHashMap<String, Integer> box = boxes.get(hash);
            if(label_focal.length == 1) {
                box.remove(label_focal[0]);
                if(box.isEmpty())
                    boxes.remove(hash);
            } else box.put(label_focal[0], Integer.valueOf(label_focal[1]));
        }

        for(Map.Entry<Integer, LinkedHashMap<String, Integer>> box: boxes.entrySet()) {
            int boxIndex = box.getKey();
            LinkedHashMap<String, Integer> lens = box.getValue();
            Map.Entry<String, Integer>[] entries = new Map.Entry[100];
            lens.entrySet().toArray(entries);
            result += LongStream.range(0, lens.size()).map(i -> (boxIndex+1) * (i+1) * (entries[(int) i].getValue())).reduce(Long::sum).getAsLong();
        }
        return result;
    }

    private static Long sum_hash_algorithm() {
        return steps.stream().mapToLong(str -> hash_algorithm(str.toCharArray())).sum();
    }

    private static int hash_algorithm(char[] chars) {
        int result = 0;

        for(char ch: chars) {
            result = ((result + (int)ch)*17) % 256;
        }

        return result;
    }

    public static void main(String[] args) {
        Long result_A = sum_hash_algorithm();
        System.out.println(result_A);

        Long result_B = sum_focusing_power();
        System.out.println(result_B);
    }
}
