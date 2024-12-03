package me.sergioramirez.days.day03;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartNumber {

    private int value;
    private List<Coordinate> coordinates;
    private boolean visited;

    public boolean containCoordinate(Coordinate coordinate) {
        return coordinates.stream().anyMatch(c -> c.getX()==coordinate.getX() && c.getY()==coordinate.getY());
    }

}

@Data
@AllArgsConstructor
class Coordinate {
    private int x;
    private int y;
    private char character;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
