package me.sergioramirez.days.day25;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Graph {

    private List<Node> nodes;
    private Map<Node, Node> parent;

    public Graph(List<Node> nodes) {
        this.nodes = nodes;
        this.parent = new HashMap<>();
    }

    public int solve() {
        List<Node> achievable = parent.entrySet().stream().filter(kv -> kv.getValue() != null).map(Map.Entry::getKey).toList();
        return (nodes.size()-achievable.size())*achievable.size();
    }


    public int minCut(Node source, Node target) {
        int maxFlow = 0;
        nodes.stream().flatMap(n -> n.getEdges().stream()).forEach(e -> e.setWeight(1)); //Reseteo peso de las aristas (sin peso, arista pesa 1)

        while(bfs(source, target)) {
            int flow = Integer.MAX_VALUE;
            Node n = target;
            while(n != source) {
                flow = Math.min(flow, getEdge(parent.get(n), n).getWeight());
                n = parent.get(n);
            }

            maxFlow+=flow;

            Node v = target;

            while(v != source) {
                Node u = parent.get(v);
                Edge e1 = getEdge(u, v);
                e1.setWeight(e1.getWeight()-flow);
                Edge e2 = getEdge(v, u);
                e2.setWeight(getEdge(v, u).getWeight()+flow);
                v = u;
            }

        }

        return maxFlow;

    }

    private boolean bfs(Node source, Node target) {
        nodes.forEach(n -> parent.put(n, null)); //Reseteo caminos
        parent.put(source, source);

        ArrayDeque<Node> deque = new ArrayDeque<>(){{add(source);}};
        while(!deque.isEmpty()) {
            Node sr = deque.poll();
            for(Edge edge: sr.getEdges()) {
                Node neighbor = edge.getNeighbor(sr);
                if(edge.getWeight() > 0 && parent.get(neighbor) == null) { //Si ya se calculó su max flow o ya lo visité
                    parent.put(neighbor, sr);
                    deque.add(neighbor);
                }
            }

        }
        return parent.get(target) != null;
    }


    public Edge getEdge(Node source, Node target) {
        return source.getEdges().stream().filter(e -> e.getNeighbor(source) == target).findFirst().get();
    }
}
