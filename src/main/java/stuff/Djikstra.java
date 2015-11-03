package stuff;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by edouard on 03/11/2015.
 */
public class Djikstra {

    public static Result djikstra(Node source, Node destination, Collection<Vertex> vertices) {

        PriorityQueue<PQElement> queue = new PriorityQueue<>(PQElement.comparator);
        HashMap<Node, Integer> distances = new HashMap<>();
        HashMap<Node, Node> previous = new HashMap<>();

        Map<Node, List<Vertex>> connections = nodeToConnections(vertices);

        distances.put(source, 0);
        previous.put(source, Node.EMPTY);
        queue.add(PQElement.make(source, 0));

        while (!queue.isEmpty()) {
            PQElement current = queue.poll();
            for (Vertex vertex : connections.getOrDefault(current.node, Collections.emptyList())) {
                Node n = vertex.to;
                Integer d = distances.get(current.node) + vertex.cost;
                if (distances.getOrDefault(n, Integer.MAX_VALUE) > d) {
                    distances.put(n, d);
                    previous.put(n, current.node);
                    queue.add(PQElement.make(n, d));
                }
            }
        }

        if (distances.containsKey(destination)) {
            ArrayList<Node> path = new ArrayList<>();
            Node current = destination;
            Node prev = previous.get(current);
            path.add(current);
            while (!Node.EMPTY.equals(prev) && prev != null) {
                path.add(prev);
                current = prev;
                prev = previous.get(current);
            }
            return new Result(source, destination, path, distances.get(destination));
        } else {
            return null;
        }

    }

    public static class Result {
        final Node from;
        final Node to;
        final List<Node> path;
        final int cost;

        public Result(Node from, Node to, List<Node> path, int cost) {
            this.from = from;
            this.to = to;
            this.path = path;
            this.cost = cost;
        }

        @Override
        public String toString() {
            List<String> ids = path.stream().map((n) -> n.id).collect(Collectors.toList());
            return String.format("from %s to %s is cost = %s | path = { %s }", this.from.id, this.to.id,this.cost, String.join(" -> ", ids));
        }
    }


    static Map<Node, List<Vertex>> nodeToConnections(Collection<Vertex> vs) {
        HashMap<Node, List<Vertex>> r = new HashMap<>();
        List<Vertex> l;
        for (Vertex v : vs) {
            l = r.get(v.from);
            if (l == null) {
                l = new ArrayList<>();
            }
            l.add(v);
            r.put(v.from, l);
        }
        return r;
    }

    public static class PQElement {
        final Node node;
        final int costFromSource;

        public static PQElement make(Node n, int c) {
            return new PQElement(n, c);
        }

        public PQElement(Node node, int costFromSource) {
            this.node = node;
            this.costFromSource = costFromSource;
        }

        public static final Comparator<PQElement> comparator = new Comparator<PQElement>() {
            @Override
            public int compare(PQElement o1, PQElement o2) {
                return Integer.compare(o1.costFromSource, o2.costFromSource);
            }
        };
    }

    public static class Node {
        public static final Node EMPTY = new Node(null);

        final String id;

        public static Node make() {
            return make(UUID.randomUUID().toString());
        }

        public static Node make(String s) {
            return new Node(s);
        }

        public Vertex to(Node n, int cost) {
            return new Vertex(this, n, cost);
        }

        public Node(String id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return new EqualsBuilder()
                    .append(id, node.id)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(id)
                    .toHashCode();
        }
    }

    public static class Vertex {
        final Node from;
        final Node to;
        final int cost;

        public Vertex(Node from, Node to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Vertex vertex = (Vertex) o;

            return new EqualsBuilder()
                    .append(cost, vertex.cost)
                    .append(from, vertex.from)
                    .append(to, vertex.to)
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37)
                    .append(from)
                    .append(to)
                    .append(cost)
                    .toHashCode();
        }
    }

}
