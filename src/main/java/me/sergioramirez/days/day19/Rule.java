package me.sergioramirez.days.day19;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rule {

    private String name;
    private List<Condition> conditions;
    private String defaultDestination;

    public String resolveDestination() {
        Optional<Condition> trueCondition = conditions.stream().filter(Condition::test).findFirst();
        return trueCondition.isPresent() ? trueCondition.get().getDestination() : defaultDestination;
    }

    public void prepare_conditions(LinkedHashMap<String, Integer> part) {
        conditions.forEach(c -> c.setOp1(part.get(c.getCategory())));
    }
}
