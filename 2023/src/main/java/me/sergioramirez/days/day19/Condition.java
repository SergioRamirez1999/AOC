package me.sergioramirez.days.day19;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.BiPredicate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Condition {

    private String category;
    private Integer op1;
    private Integer op2;
    private BiPredicate<Integer, Integer> predicate;
    private String destination;

    public boolean test() {
        return predicate.test(op1, op2);
    }

}
