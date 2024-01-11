package me.sergioramirez.days.day21;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Plot {
    private int c;
    private int r;
    private int remaining;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plot plot = (Plot) o;
        return c == plot.c && r == plot.r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c, r);
    }
}
