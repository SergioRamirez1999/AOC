package me.sergioramirez.days.day10;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day10\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final List<List<Tile>> grid = init_grid();
    private static final List<Tile> path_loop = new ArrayList<>();

    private static List<List<Tile>> init_grid() {
        List<List<Tile>> grid = getTiles();

        for(int i=0; i<grid.size(); i++){
            for(int h=0; h < grid.get(i).size(); h++) {
                Tile t = grid.get(i).get(h);
                Tile up = (i-1) >= 0 ? grid.get(i-1).get(h) : null;
                Tile down = (i+1) < grid.size() ? grid.get(i+1).get(h) : null;
                Tile left = (h-1) >= 0 ? grid.get(i).get(h-1) : null;
                Tile right = (h+1) < grid.get(i).size() ? grid.get(i).get(h+1) : null;

                String value = t.getValue();
                if(value.equals("S")) {
                    if(left != null && left.isRight() && right != null && right.isLeft())
                        value = "-";
                    else if(up != null && up.isDown() && down != null && down.isUp())
                        value = "|";
                    else if(up != null && up.isDown() && right != null && right.isLeft())
                        value = "L";
                    else if(up != null && up.isDown() && left != null && left.isRight())
                        value = "J";
                    else if(down != null && down.isUp() && left != null && left.isRight())
                        value = "7";
                    else if(down != null && down.isUp() && right != null && right.isLeft())
                        value = "F";
                }

                if(t.isPipe()) {
                    switch (value) {
                        case "-" -> {
                            t.setP1(left);
                            t.setP2(right);
                        }
                        case "|" -> {
                            t.setP1(up);
                            t.setP2(down);
                        }
                        case "L" -> {
                            t.setP1(up);
                            t.setP2(right);
                        }
                        case "J" -> {
                            t.setP1(up);
                            t.setP2(left);
                        }
                        case "7" -> {
                            t.setP1(left);
                            t.setP2(down);
                        }
                        case "F" -> {
                            t.setP1(right);
                            t.setP2(down);
                        }
                    }
                }

            }
        }

        return grid;
    }

    private static List<List<Tile>> getTiles() {
        List<List<Tile>> grid = new ArrayList<>();

        for(int i = 0; i < input.size(); i++) {
            List<Tile> line = new ArrayList<>();
            for(int h = 0; h < input.get(i).length(); h++){
                String x = String.valueOf(input.get(i).charAt(h));
                Tile tile = new Tile();
                switch (x) {
                    case "-" -> {
                        tile.setLeft(true);
                        tile.setRight(true);
                    }
                    case "|" -> {
                        tile.setUp(true);
                        tile.setDown(true);
                    }
                    case "L" -> {
                        tile.setUp(true);
                        tile.setRight(true);
                    }
                    case "J" -> {
                        tile.setUp(true);
                        tile.setLeft(true);
                    }
                    case "7" -> {
                        tile.setDown(true);
                        tile.setLeft(true);
                    }
                    case "F" -> {
                        tile.setDown(true);
                        tile.setRight(true);
                    }
                }
                tile.setValue(x);
                tile.setX(h);
                tile.setY(i);
                line.add(tile);
            }
            grid.add(line);
        }
        return grid;
    }


    private static int walk_loop() {
        Tile tileS = grid.stream().flatMap(Collection::stream).filter(t -> t.getValue().equals("S")).findFirst().get();
        int count = 0;
        Tile tile = tileS;
        tileS.setVisited(true);
        while (tile != null) {
            tile.setVisited(true);
            path_loop.add(tile);
            tile = tile.getNotVisited();
            count++;
        }
        return count/2;
    }

    private static int area_loop() {
        int area = area_gauss();
        int bdiv = (path_loop.size() / 2);
        return area - bdiv + 1;
    }

    private static int area_gauss() {
        //I = cantidad de puntos interiores
        //B = puntos limite
        //Teorema Pick's --> A = I + B/2 - 1
        //Despejamos I ---> I = A - b/2 + 1

        List<Tile> tileLeft = new ArrayList<>(path_loop.subList(0, path_loop.size()-1));
        List<Tile> tileRight = new ArrayList<>(path_loop.subList(1, path_loop.size()));
        tileLeft.add(tileRight.getLast()); //Agrego los puntos límites para realizar tmbn la diferencia de productos de coordenadas
        tileRight.add(tileLeft.getFirst());
        int area = 0;
        for(int i = 0; i < tileLeft.size(); i++) {
            Tile t1 = tileLeft.get(i);
            Tile t2 = tileRight.get(i);
            int x1 = t1.getX();
            int y1 = t1.getY();
            int x2 = t2.getX();
            int y2 = t2.getY();
            area += (x1*y2)-(x2*y1);
        }

        return Math.abs(area) / 2;
    }


    public static void main(String[] args) {
        int result_A = walk_loop();
        System.out.println(result_A);
        int result_B = area_loop();
        System.out.println(result_B);

    }
}
