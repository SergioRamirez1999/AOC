package me.sergioramirez.days.day19;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class PartRange {

    Map<String, List<Integer>> ranges;

    public PartRange() {
        ranges = new HashMap<>() {{
            put("x", Stream.of(0, 4001).collect(Collectors.toList()));
            put("m", Stream.of(0, 4001).collect(Collectors.toList()));
            put("a", Stream.of(0, 4001).collect(Collectors.toList()));
            put("s", Stream.of(0, 4001).collect(Collectors.toList()));
        }};
    }

    public PartRange(Map<String, List<Integer>> range) {
        ranges = new HashMap<>();
        for(Map.Entry<String, List<Integer>> eS: range.entrySet()) {
            ranges.put(eS.getKey(), Stream.of(eS.getValue().get(0), eS.getValue().get(1)).collect(Collectors.toList()));
        }
    }

    public List<PartRange> splitOn(String lhs) {
        PartRange inRange = new PartRange(ranges);
        PartRange outRange = new PartRange(ranges);
        Integer num = Integer.parseInt(lhs.substring(2));
        String category = lhs.substring(0,1);
        String operator = lhs.substring(1,2);

        List<Integer> existingRange = inRange.getRanges().get(category);
        if(existingRange.get(0) <= num && num <= existingRange.get(1)) {
            if(operator.equals(">")) {
                inRange.getRanges().get(category).set(0, num);
                outRange.getRanges().get(category).set(1, num+1);
            } else {
                inRange.getRanges().get(category).set(1, num);
                outRange.getRanges().get(category).set(0, num-1);
            }
        } else if(num < existingRange.get(0) && operator.equals(">")) {
            outRange = null;
        } else if(num > existingRange.get(1) && operator.equals("<"))
            outRange = null;
        else {
            inRange = null;
            outRange = null;
        }

        return Stream.of(inRange, outRange).collect(Collectors.toList());

    }

    public Long score() {
        Long result = 1L;
        for(Map.Entry<String, List<Integer>> es: ranges.entrySet()) {
            Integer l = es.getValue().get(0);
            Integer h = es.getValue().get(1);
            result *= h-l-1;
        }
        return result;
    }

    public boolean inRange(Integer x, Integer m, Integer a, Integer s) {
        Map<String, Integer> c_v = new HashMap<>(){{
           put("x", x);
           put("m", m);
           put("a", a);
           put("s", s);
        }};
        for(Map.Entry<String, Integer> eS: c_v.entrySet()) {
            if(!(ranges.get(eS.getKey()).get(0) < eS.getValue() && eS.getValue() < ranges.get(eS.getKey()).get(1)))
                return false;
        }
        return true;
    }


}
