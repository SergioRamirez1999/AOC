package me.sergioramirez.days.day25;

import it.unimi.dsi.fastutil.Hash;
import me.sergioramirez.util.InputTextUtil;

import java.nio.file.Paths;
import java.util.*;

public class Puzzle {
    private static final String filepath = Paths.get("").toAbsolutePath()+"\\src\\main\\java\\me\\sergioramirez\\days\\day25\\input.txt";
    private static final List<String> input = InputTextUtil.read_all_lines(filepath);
    private static final Graph graph = init_graph();

    private static Graph init_graph() {
        Map<String, Node> nodes = new HashMap<>();
        for(String line: input) {
            String nodeLeftStr = line.split(":")[0];
            List<String> right = Arrays.stream(line.split(":")[1].trim().split(" ")).map(String::trim).toList();

            if(!nodes.containsKey(nodeLeftStr))
                nodes.put(nodeLeftStr, new Node(nodeLeftStr));

            Node nodeLeft = nodes.get(nodeLeftStr);

            for(String nodeRightStr: right) {
                if(!nodes.containsKey(nodeRightStr))
                    nodes.put(nodeRightStr, new Node(nodeRightStr));
                Node nodeRight = nodes.get(nodeRightStr);
                Edge edge1 = new Edge(nodeLeft, nodeRight, 1);
                Edge edge2 = new Edge(nodeRight, nodeLeft, 1);
                nodeRight.addEdge(edge1);
                nodeLeft.addEdge(edge2);
            }
        }
        return new Graph(nodes.values().stream().toList());
    }

    private static int min_cut() {
        Node root = graph.getNodes().get(0);
        List<Node> remainingNodes = graph.getNodes().subList(1, graph.getNodes().size());

        for(Node node: remainingNodes)
            if(graph.minCut(root, node) == 3)
                break;

        return graph.solve();
    }


    public static void main(String[] args) {

        int result_A = min_cut();
        System.out.println(result_A);

    }


}
