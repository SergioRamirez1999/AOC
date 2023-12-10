package me.sergioramirez.days.day08;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Puzzle {
    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day08\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final List<String> instructions = new ArrayList<>();
    private static final Map<String, Node> nodes = new HashMap<>();
    private static final String NODE_PATTERN = "(\\w{3}) = [(](\\w{3}), (\\w{3})[)]";

    private static void init_nodes() {
        instructions.addAll(Arrays.asList(input.get(0).split("")));
        String nodes_text = IntStream.range(2, input.size()).mapToObj(input::get).collect(Collectors.joining("\n"));
        Pattern.compile(NODE_PATTERN).matcher(nodes_text).results().forEach( m -> nodes.put(m.group(1), new Node(m.group(2), m.group(3))));
    }

    private static int count_steps() {
        String actual = "AAA";
        int count = 0;
        while(!actual.equals("ZZZ")) {
            for(String instruction: instructions) {
                actual = instruction.equals("L") ? nodes.get(actual).getLeft() : nodes.get(actual).getRight();
                count++;
                if(actual.equals("ZZZ")) break;
            }
        }
        return count;
    }

    private static int count_steps(String actual) {
        int count = 0;
        while(!(actual.charAt(actual.length()-1) == 'Z')) {
            for(String instruction: instructions) {
                actual = instruction.equals("L") ? nodes.get(actual).getLeft() : nodes.get(actual).getRight();
                count++;
                if(actual.charAt(actual.length()-1) == 'Z') break;
            }
        }
        return count;
    }

    private static long count_simultaneous_steps_B() {
        List<Map.Entry<String, Node>> nodes_A = nodes.entrySet().stream().filter(kv -> kv.getKey().split("")[kv.getKey().split("").length-1].equals("A")).toList();
        List<Integer> counts = nodes_A.stream().map(n -> count_steps(n.getKey())).sorted().toList();
        return counts.stream().mapToLong(Long::valueOf).reduce(Puzzle::mcm).getAsLong();
    }

    public static long mcd(long a, long b) {
        long temp;
        while (b != 0) {
            temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static long mcm(long a, long b) {
        return (a * b) / mcd(a, b);
    }

    public static void main(String[] args) {
        init_nodes();
        int result_A = count_steps();
        System.out.println(result_A);
        long result_B = count_simultaneous_steps_B();
        System.out.println(result_B);
    }
}
