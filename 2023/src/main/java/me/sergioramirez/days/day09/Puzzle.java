package me.sergioramirez.days.day09;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day09\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final List<List<Long>> historyValues = new ArrayList<>();

    private static void init_history(){
        for(String line:input){
            historyValues.add(Arrays.stream(line.split(" ")).map(Long::parseLong).collect(Collectors.toList()));
        }
    }

    private static Long calcule_extrapolation(String part){
        Long total=0L;
        if(part.equals("one"))
            for(List<Long> values:historyValues){
                total += predict(values);
            }
        else
            for(List<Long> values:historyValues){
                total += predict2(values);
            }
        return total;
    }

    private static Long predict(List<Long> values){
        if(values.stream().allMatch(v->v==0L)){
            return values.get(values.size()-1);
        }
        List<Long> auxList = new ArrayList<>();
        for(int i=0;i<values.size()-1;i++){
            auxList.add(values.get(i+1)-values.get(i));
        }
        return values.get(values.size()-1)+predict(auxList);
    }

    private static Long predict2(List<Long> values){
        if(values.stream().allMatch(v->v==0L)){
            return values.get(0);
        }
        List<Long> auxList = new ArrayList<>();
        for(int i=0;i<values.size()-1;i++){
            auxList.add(values.get(i+1)-values.get(i));
        }
        return values.get(0)-predict2(auxList);
    };

    public static void main(String[] args) {
        init_history();
        Long result_A = calcule_extrapolation("one");
        System.out.println(result_A);
        Long result_B = calcule_extrapolation("two");
        System.out.println(result_B);
    }
}
