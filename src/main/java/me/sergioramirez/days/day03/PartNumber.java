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
}

@Data
@AllArgsConstructor
class Coordinate {
    private int x;
    private int y;
}
