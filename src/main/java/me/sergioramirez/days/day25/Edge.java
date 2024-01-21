package me.sergioramirez.days.day25;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Edge {
    private Node n1;
    private Node n2;
    private int weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(n1, edge.n1) && Objects.equals(n2, edge.n2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n1, n2);
    }

    public Node getNeighbor(Node source) {
        return n1 == source ? n2 : n1;
    }
}
