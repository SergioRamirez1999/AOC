package me.sergioramirez.days.day22;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day22\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final List<List<Integer>> bricks = init_bricks();
    private static final Map<Integer, HashSet<Integer>> k_supports_v = new HashMap<>();
    private static final Map<Integer, HashSet<Integer>> v_supports_k = new HashMap<>();

    private static List<List<Integer>> init_bricks() {
        List<List<Integer>> bricks = new ArrayList<>();
        for(String line: input)
            bricks.add(Arrays.stream(line.replace("~", ",").split(",")).map(Integer::parseInt).collect(Collectors.toList()));
        return bricks;
    }

    private static boolean overlaps(List<Integer> b1, List<Integer> b2) {
        return Math.max(b1.get(0), b2.get(0)) <= Math.min(b1.get(3), b2.get(3)) && Math.max(b1.get(1), b2.get(1)) <= Math.min(b1.get(4), b2.get(4));
    }

    private static long count_desintegration() {
        long result = 0L;
        bricks.sort(Comparator.comparing(c -> c.get(2))); //Orden en eje z

        for(int i=0; i < bricks.size(); i++) {
            List<Integer> brick = bricks.get(i);
            int max_z = 1;
            for(List<Integer> check: bricks.subList(0, i))
                if(overlaps(brick, check)) {
                    max_z = Math.max(max_z, check.get(5) + 1);
                }
            brick.set(5, (brick.get(5) - (brick.get(2) - max_z)));
            brick.set(2, max_z);
        }

        bricks.sort(Comparator.comparing(c -> c.get(2))); //Orden en eje z



        for(int i=0; i < bricks.size(); i++) {
            k_supports_v.put(i, new HashSet<>());
            v_supports_k.put(i, new HashSet<>());
        }

        for(int i=0; i < bricks.size(); i++) {
            List<Integer> upper = bricks.get(i);
            List<List<Integer>> remaining = bricks.subList(0, i);
            for(int h=0; h < remaining.size(); h++) {
                List<Integer> lower = remaining.get(h);
                if(overlaps(lower, upper) && upper.get(2) == (lower.get(5) + 1)) {
                    k_supports_v.get(h).add(i);
                    v_supports_k.get(i).add(h);
                }
            }
        }

        for(int i=0; i < bricks.size(); i++) {
            if(k_supports_v.get(i).stream().allMatch(j -> v_supports_k.get(j).size() >= 2))
                result++;
        }

        return result;
    }

    private static long sum_falls() {
        long result = 0L;

        for(int i=0; i < bricks.size(); i++) {
            ArrayDeque<Integer> deque = k_supports_v.get(i).stream().filter(j -> v_supports_k.get(j).size() == 1).collect(Collectors.toCollection(ArrayDeque::new));
            HashSet<Integer> falling = new HashSet<>(deque);
            falling.add(i);

            while(!deque.isEmpty()) {
                int j = deque.poll();
                for(Integer k: k_supports_v.get(j).stream().filter(v -> !falling.contains(v)).toList()) { //A - B
                    if(falling.containsAll(v_supports_k.get(k))) {// B subconjunto de A
                        deque.add(k);
                        falling.add(k);
                    }
                }
            }

            result += falling.size() - 1;
        }


        return result;
    }

    public static void main(String[] args) {
        long result_A = count_desintegration();
        System.out.println(result_A);
        long result_B = sum_falls();
        System.out.println(result_B);
    }
}
