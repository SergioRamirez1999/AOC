package me.sergioramirez.days.day01;

import me.sergioramirez.util.InputTextUtil;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Puzzle
{

    public static int count_calibration_A(List<String> input) {

        return input.stream().map(l -> l.replaceAll("[\\D]", ""))
                .map(
                    l -> {
                        if(!l.isEmpty())
                            return ""+l.charAt(0
                            )+l.charAt(l.length()-1);
                        else
                            return "0";
                    }
                ).mapToInt(Integer::parseInt).sum();
    }

    public static int count_calibration_B(List<String> input) {
        List<String> posibilities = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        List<List<String>> calibration_values = new ArrayList<>();
        for(String line: input) {
            List<String> matches = new ArrayList<>();
            String auxLine = line;
            while(!auxLine.isEmpty()) {
                StringBuffer subString = new StringBuffer();
                for(int i = 0; i < auxLine.length(); i++) {
                    subString.append(auxLine.charAt(i));
                    Optional<String> ocurrence = posibilities.stream().filter(p -> subString.toString().contains(p)).findFirst();
                    if(ocurrence.isPresent()) {
                        matches.add(ocurrence.get());
                        if(subString.toString().matches(".*[0-9].*"))
                            i++;
                        auxLine = auxLine.substring(i);
                        break;
                    } else if(i == auxLine.length()-1)
                        auxLine = "";
                }
            }
            calibration_values.add(matches);
        }


        return calibration_values.stream().map(
                    e -> Stream.of(e.get(0), e.get(e.size()-1)).map(x -> {
                        if(x.matches(".*[0-9].*"))
                            return x;
                        else
                            return posibilities.get(posibilities.indexOf(x)+9);
                    }
                ).collect(Collectors.joining())).mapToInt(Integer::valueOf).sum();
    }

    public static void main( String[] args ) {
        String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day01\\input.txt";
        List<String> input_lines = InputTextUtil.read_all_lines(filepath);
        int result_A = count_calibration_A(input_lines);
        System.out.println(result_A);

        int result_B = count_calibration_B(input_lines);
        System.out.println(result_B);

    }
}
