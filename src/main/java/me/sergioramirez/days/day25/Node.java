package me.sergioramirez.days.day25;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    private String value;
    @ToString.Exclude
    private List<Edge> edges;

    private List<Edge> path;

    public Node(String value) {
        this.value = value;
        this.edges = new ArrayList<>();
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public void addEdge(List<Edge> edges) {
        this.edges.addAll(edges);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(value, node.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
