package me.sergioramirez.days.day16;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contraption {
    private String[][] grid;
    private List<Beam> beams;
    private int WIDTH;
    private int HEIGHT;

    public Contraption(String[][] grid) {
        this.grid = grid;
        this.beams = new ArrayList<>();
        this.WIDTH = grid[0].length;
        this.HEIGHT = grid.length;
    }

    public int max_bounce_light() {
        int max = 0;

        for(int y=0; y<HEIGHT; y++) {
            max = Math.max(max, bounce_light(-1, y, Direction.RIGHT));
            max = Math.max(max, bounce_light(WIDTH, y, Direction.LEFT));
        }

        for(int x=0; x<WIDTH; x++) {
            max = Math.max(max, bounce_light(x, -1, Direction.DOWN));
            max = Math.max(max, bounce_light(x, HEIGHT, Direction.UP));
        }

        return max;
    }

    public int bounce_light(int initialX, int initialY, Direction initialDirection) {

        beams = new ArrayList<>() {{
            add(new Beam(new Position(initialX, initialY, initialDirection)));
        }};

        List<Beam> newBeams = new ArrayList<>();
        Set<Position> positions = new HashSet<>();
        Set<Position> visited = new HashSet<>();
        while (!beams.isEmpty()) {
            for (Beam beam : beams) {
                if (!visited.add(beam.getActualPosition())) continue;
                positions.add(beam.getActualPosition());
                beam.nextPosition(grid, WIDTH, HEIGHT, newBeams);
            }
            beams = new ArrayList<>(newBeams);
            newBeams.clear();
        }

        List<Position> uniquePositions = positions.stream().collect(Collectors.groupingBy(Position::getXY)).values().stream().flatMap(p -> Stream.of(p.getFirst())).filter(x -> x.getX() != -1).toList();
        return uniquePositions.size();
    }


}
