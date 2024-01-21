package me.sergioramirez.days.day10;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tile {

    private String value;
    @ToString.Exclude
    private Tile p1, p2;
    private boolean visited;
    private boolean up, down, left, right;
    private int x,y;

    public Tile(String value) {
        this.value = value;
    }

    public boolean isPipe() {
        return !value.equals(".");
    }

    public Tile getNotVisited() {
        return Stream.of(p1, p2).filter(t -> !t.isVisited()).findFirst().orElseGet(() -> null);
    }
}
