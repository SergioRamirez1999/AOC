package me.sergioramirez.days.day05;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day05\\input.txt";
    private static final String input = InputTextUtil.read_all_string(filepath);

    private static List<Seed> seeds = new ArrayList<>();

    private static final List<CategoryMapping> categoryMappings = new ArrayList<>();

    private static void init_structures() {
        String[] sections = input.split("\\n\\s*\\n");
        seeds = Arrays.stream(sections[0].split(":")[1].split(" ")).filter(l -> !l.isEmpty()).map(n -> new Seed(Long.parseLong(n.trim()))).collect(Collectors.toList());
        for(int i = 1; i < sections.length; i++) {
            String section = sections[i];
            String[] category_mapping = section.split(":");
            String[] source_destination_arr = category_mapping[0].split("-");
            String source = source_destination_arr[0];
            String destination = source_destination_arr[2].split(" ")[0];
            String[] transformations_sections = Arrays.stream(category_mapping[1].split("\\r")).filter(s -> !s.isEmpty()).toArray(String[]::new);
            List<Transformation> transformations = new ArrayList<>();
            for(int h = 0; h < transformations_sections.length; h++) {
                Long[] numbers = Arrays.stream(transformations_sections[h].split(" ")).map(l -> l.replaceAll("\\n", "").replaceAll("\\r", "")).map(Long::parseLong).toArray(Long[]::new);
                transformations.add(new Transformation(numbers[0], numbers[1], numbers[2]));
            }
            categoryMappings.add(new CategoryMapping(source, destination, transformations));
        }
    }


    private static Long calcule_lowest_location_A() {
        Long location = 0L;
        for(Seed seed: seeds) {
            boolean firstProcessed = false;
            List<Long> destinations = new ArrayList<>();
            for(CategoryMapping categoryMapping: categoryMappings) {
                if(!firstProcessed) {
                    location = categoryMapping.calcule_destination(seed.getNumber());
                    firstProcessed = true;
                } else location = categoryMapping.calcule_destination(location);
                destinations.add(location);
            }
            seed.setDestinations(new ArrayList<>(destinations));
        }
        return seeds.stream().map(x -> x.getDestinations().get(x.getDestinations().size()-1)).mapToLong(Long::valueOf).min().getAsLong();
    }

    private static Long calcule_lowest_location_B() {
        Long location = 0L;
        Long lowestLocation = 99999999L;
        long init = System.currentTimeMillis();
        for(int i = 0; i < seeds.size()-1; i+=2) {
            Seed seedOrigin = seeds.get(i);
            Seed seedRange = seeds.get(i+1);
            for(Long actualSeed = seedOrigin.getNumber(); actualSeed < seedOrigin.getNumber() + seedRange.getNumber() -1; actualSeed++) {
                boolean firstProcessed = false;
                for(CategoryMapping categoryMapping: categoryMappings) {
                    if(!firstProcessed) {
                        location = categoryMapping.calcule_destination(actualSeed);
                        firstProcessed = true;
                    } else location = categoryMapping.calcule_destination(location);
                }
                lowestLocation = lowestLocation < location ? lowestLocation : location;
            }
        }
        System.out.println("Tiempo de procesamiento: " + (System.currentTimeMillis() - init));
        return lowestLocation;
    }

    public static void main(String[] args) {
        init_structures();
        Long result_A = calcule_lowest_location_A();
        System.out.println(result_A);
        Long result_B = calcule_lowest_location_B();
        System.out.println(result_B);
    }
}
