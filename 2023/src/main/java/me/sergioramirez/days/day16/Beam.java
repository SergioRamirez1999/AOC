package me.sergioramirez.days.day16;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beam {

    private Position actualPosition;

    private boolean isLooping;

    private boolean isOutOfBounds;

    public Beam(Position actualPosition) {
        this.actualPosition = actualPosition;
    }

    public void nextPosition(String[][] grid, int width, int height, List<Beam> beams) {

        int actualX = actualPosition.getX();
        int actualY = actualPosition.getY();
        Direction actualDirection = actualPosition.getDirection();

        beams.add(this);

        switch (actualDirection) {
            case UP: {
                step(actualX, actualY-1, actualDirection, width, height, grid, beams);
                break;
            }
            case DOWN: {
                step(actualX, actualY+1, actualDirection, width, height, grid, beams);
                break;
            }
            case LEFT: {
                step(actualX-1, actualY, actualDirection, width, height, grid, beams);
                break;
            }
            case RIGHT: {
                step(actualX+1, actualY, actualDirection, width, height, grid, beams);
                break;
            }
        }
    }

    private void step(int nextX, int nextY, Direction actualDirection, int width, int height, String[][] grid, List<Beam> beams) {
        if((nextX<0 || nextX >= width) || (nextY<0 || nextY >= height)) { //Si estoy fuera de rango
            isOutOfBounds = true;
            return;
        }

        Position nextPosition, positionNewBeam = null;
        String nextTile =  grid[nextY][nextX];

        switch (actualDirection) {
            case RIGHT: {
                switch (nextTile) {
                    case "/":
                        nextPosition = new Position(nextX, nextY, Direction.UP);
                        break;
                    case "\\": {
                        nextPosition = new Position(nextX, nextY, Direction.DOWN);
                        break;
                    }
                    case "|":
                        positionNewBeam = new Position(nextX, nextY, Direction.UP);
                        nextPosition = new Position(nextX, nextY, Direction.DOWN);
                        break;
                    default:
                        nextPosition = new Position(nextX, nextY, actualDirection);
                        break;
                }
                break;
            }
            case LEFT: {
                switch (nextTile) {
                    case "/":
                        nextPosition = new Position(nextX, nextY, Direction.DOWN);
                        break;
                    case "\\": {
                        nextPosition = new Position(nextX, nextY, Direction.UP);
                        break;
                    }
                    case "|":
                        positionNewBeam = new Position(nextX, nextY, Direction.UP);
                        nextPosition = new Position(nextX, nextY, Direction.DOWN);
                        break;
                    default:
                        nextPosition = new Position(nextX, nextY, actualDirection);
                        break;
                }
                break;
            }
            case UP: {
                switch (nextTile) {
                    case "/":
                        nextPosition = new Position(nextX, nextY, Direction.RIGHT);
                        break;
                    case "\\": {
                        nextPosition = new Position(nextX, nextY, Direction.LEFT);
                        break;
                    }
                    case "-":
                        positionNewBeam = new Position(nextX, nextY, Direction.LEFT);
                        nextPosition = new Position(nextX, nextY, Direction.RIGHT);
                        break;
                    default:
                        nextPosition = new Position(nextX, nextY, actualDirection);
                        break;
                }
                break;
            }
            default: {
                switch (nextTile) {
                    case "/":
                        nextPosition = new Position(nextX, nextY, Direction.LEFT);
                        break;
                    case "\\": {
                        nextPosition = new Position(nextX, nextY, Direction.RIGHT);
                        break;
                    }
                    case "-":
                        positionNewBeam = new Position(nextX, nextY, Direction.LEFT);
                        nextPosition = new Position(nextX, nextY, Direction.RIGHT);
                        break;
                    default:
                        nextPosition = new Position(nextX, nextY, actualDirection);
                        break;
                }
                break;
            }
        }

        if (positionNewBeam != null) {
            beams.add(new Beam(positionNewBeam));
        }

        actualPosition = nextPosition;
    }

}
