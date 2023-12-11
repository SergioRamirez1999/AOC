package me.sergioramirez.days.day10;

import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Puzzle {

    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day10\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);

    private static final List<List<Tile>> grid = new ArrayList<>();

    private static void init_grid() {

        for(int i = 0; i < input.size(); i++) {
            List<Tile> line = new ArrayList<>();
            for(int h = 0; h < input.get(i).length(); h++){
                String x = String.valueOf(input.get(i).charAt(h));
                Tile tile = new Tile();
                if(x.equals("-")) {
                    tile.setLeft(true);
                    tile.setRight(true);
                }else if(x.equals("|")) {
                    tile.setUp(true);
                    tile.setDown(true);
                }else if(x.equals("L")) {
                    tile.setUp(true);
                    tile.setRight(true);
                }else if(x.equals("J")) {
                    tile.setUp(true);
                    tile.setLeft(true);
                }else if(x.equals("7")) {
                    tile.setDown(true);
                    tile.setLeft(true);
                }else if(x.equals("F")) {
                    tile.setDown(true);
                    tile.setRight(true);
                }
                tile.setValue(x);
                tile.setX(h);
                tile.setY(i);
                line.add(tile);
            }
            grid.add(line);
        }

        for(int i=0; i<grid.size(); i++){
            for(int h=0; h < grid.get(i).size(); h++) {
                Tile t = grid.get(i).get(h);
                Tile up = (i-1) >= 0 ? grid.get(i-1).get(h) : null;
                Tile down = (i+1) < grid.size() ? grid.get(i+1).get(h) : null;
                Tile left = (h-1) >= 0 ? grid.get(i).get(h-1) : null;
                Tile right = (h+1) < grid.get(i).size() ? grid.get(i).get(h+1) : null;

                t.setP1(up);
                t.setP2(up);
                t.setP3(up);
                t.setP4(up);

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
                    t.setRealValue(value);
                }

                if(t.isPipe()) {
                    if(value.equals("-")) {
                            t.setP1(left);
                            t.setP2(right);
                    }else if(value.equals("|")) {
                            t.setP1(up);
                            t.setP2(down);
                    }else if(value.equals("L")) {
                            t.setP1(up);
                            t.setP2(right);
                    }else if(value.equals("J")) {
                            t.setP1(up);
                            t.setP2(left);
                    }else if(value.equals("7")) {
                            t.setP1(left);
                            t.setP2(down);
                    }else if(value.equals("F")) {
                            t.setP1(right);
                            t.setP2(down);
                    }
                }

            }
        }
    }

    private static int recorrer() {
        Tile tileS = grid.stream().flatMap(Collection::stream).filter(t -> t.getValue().equals("S")).findFirst().get();
        boolean flag = false;
        int count = 0;
        Tile tilep1 = tileS.getP1();
        Tile tilep2 = tileS.getP2();
        tileS.setVisited(true);
        while(!flag) {
            tilep1.setVisited(true);
            tilep2.setVisited(true);
            Tile p1 = tilep1.getNotVisited();
            Tile p2 = tilep2.getNotVisited();
            if(p1 != null){
                p1.setVisited(true);
                tilep1 = p1;
            } else flag = true;
            if(p2 != null){
                p2.setVisited(true);
                tilep2 = p2;
            } else flag = true;
            count++;
        }
        return count;
    }


    public static void main(String[] args) {
        init_grid();
        int result_A = recorrer();
        System.out.println(result_A);

    }
}
