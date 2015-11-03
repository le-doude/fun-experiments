package stuff;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import stuff.Djikstra.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by edouard on 03/11/2015.
 */
public class DjikstraRun {

    public static void main(String[] args) {

        List<Node> nodes = Stream.of(ArrayUtils.toObject("abcdefghijk".toUpperCase().toCharArray())).map((c) -> Node.make(Character.toString(c))).collect(Collectors.toList());
        List<Vertex> connections = new ArrayList<>();
        Random r = new Random();
        for (Node node : nodes) {
            for (Node next : pickSome(nodes, r.nextInt(4))) {
                if (!node.equals(next)) {
                    connections.add(node.to(next, r.nextInt(25) + 1));
                }
            }
        }

        Node source = pickOne(nodes);
        Node destination = pickOne(nodes);
        while (destination == source){
            destination = pickOne(nodes);
        }

        Result result = Djikstra.djikstra(source, destination, connections);

        if(result != null){
            System.out.println(result);
        }else{
            System.out.println("no path found");
        }
    }

    static <T> List<T> pickSome(List<T> elements, int N) {
        ArrayList<T> copy = new ArrayList<>(elements);
        Collections.shuffle(copy);
        return copy.subList(0, N);
    }

    static <T> T pickOne(List<T> elements) {
        return pickSome(elements, 1).get(0);
    }

}
