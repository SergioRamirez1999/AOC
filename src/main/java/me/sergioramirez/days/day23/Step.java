package me.sergioramirez.days.day23;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step {

    private int x;
    private int y;
    private int count;
    private HashSet<Step> visited;

    public Step(int x, int y, int count) {
        this.x = x;
        this.y = y;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return x == step.x && y == step.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String getPointStr() {
        return x + "|" + y;
    }
}
