package me.sergioramirez.days.day19;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day19\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static String RULE_REGEX_PATTERN = "^([a-z]{1,3})[{](?:(.)(>|<)(\\d{1,4}):([a-z A-Z]{1,3}),?)(?:(.)(>|<)(\\d{1,4}):([a-z A-Z]{1,3}),?)?(?:(.)(>|<)(\\d{1,4}):([a-z A-Z]{1,3}),?)?,([a-z A-Z]{1,3})}$";
    private static final Map<String, BiPredicate<Integer, Integer>> predicates = new HashMap<>(){{
        put("<", (a, b) -> a < b);
        put(">", (a, b) -> a > b);
    }};
    private static final List<PartRange> validRanges = new ArrayList<>();
    private static final Map<String, List<String>> workflows = new HashMap<>();
    private static final Map<String, Rule> label_rule = init_rules();
    private static final List<LinkedHashMap<String, Integer>> parts = init_parts();


    private static Map<String, Rule> init_rules() {
        Map<String, Rule> label_rule = new HashMap<>();
        Pattern pattern = Pattern.compile(RULE_REGEX_PATTERN);
        for(String line: input.subList(0, input.indexOf(""))) {
            Matcher matcher = pattern.matcher(line);
            if(matcher.find()) {
                String ruleName = matcher.group(1);
                String rules = line.substring(0, line.length()-1).split("\\{")[1];
                workflows.put(ruleName, Arrays.asList(rules.split(",")));
                List<Condition> conditions = new ArrayList<>();
                String defaultDestination = "";
                for(int i=2; i < matcher.groupCount()+1; i+=4) {
                    String category = matcher.group(i);
                    if(category != null) {
                        if(i<14) {
                            String operator = matcher.group(i + 1);
                            Integer op2 = Integer.parseInt(matcher.group(i + 2));
                            String destination = matcher.group(i + 3);
                            conditions.add(new Condition(category, 0, op2, predicates.get(operator), destination));
                        } else defaultDestination = category;

                    }
                }
                label_rule.put(ruleName, new Rule(ruleName, conditions, defaultDestination));
            }
        }
        return label_rule;
    }

    private static List<LinkedHashMap<String, Integer>> init_parts() {
        List<LinkedHashMap<String, Integer>> parts = new ArrayList<>();
        for(String partStr: input.subList(input.indexOf("")+1, input.size())) {
            String[] categories = partStr.substring(1, partStr.length()-1).split(",");
            LinkedHashMap<String, Integer> part = new LinkedHashMap<>(){{
                put(categories[0].split("=")[0], Integer.parseInt(categories[0].split("=")[1]));
                put(categories[1].split("=")[0], Integer.parseInt(categories[1].split("=")[1]));
                put(categories[2].split("=")[0], Integer.parseInt(categories[2].split("=")[1]));
                put(categories[3].split("=")[0], Integer.parseInt(categories[3].split("=")[1]));
            }};
            parts.add(part);
        }
        return parts;
    }

    private static long sum_rating_numbers() {
        long result = 0L;

        for(LinkedHashMap<String, Integer> part: parts) {
            boolean accepted_rejected = false;
            String ruleName = "in";
            while(!accepted_rejected) {
                Rule rule = label_rule.get(ruleName);
                rule.prepare_conditions(part);
                ruleName = rule.resolveDestination();
                if(ruleName.equals("A") || ruleName.equals("R"))
                    accepted_rejected = true;
            }
            if(ruleName.equals("A"))
                result += part.get("x") + part.get("m") + part.get("a") + part.get("s");
        }

        return result;
    }

    private static void testRange(PartRange partRange, String category) {
        if(partRange == null) return;
        if(category.equals("A") || category.equals("R")) {
            if(category.equals("A"))
                validRanges.add(partRange);
        } else {
            List<String> rules = workflows.get(category);
            for(String rule: rules) {
                if(!rule.contains(":"))
                    testRange(partRange, rule);
                else {
                    String[] split = rule.split(":");
                    String lhs = split[0];
                    String nextCategory = split[1];
                    List<PartRange> partRanges = partRange.splitOn(lhs);
                    testRange(partRanges.get(0), nextCategory);
                    partRange = partRanges.get(1);
                    if(partRange == null) return;
                }
            }
        }
    }

    private static Long sum_all_combinations() {
        testRange(new PartRange(), "in");
        return validRanges.stream().mapToLong(PartRange::score).sum();
    }

    public static void main(String[] args) {
        long result_A = sum_rating_numbers();
        System.out.println(result_A);
        long result_B = sum_all_combinations();
        System.out.println(result_B);

    }

}
