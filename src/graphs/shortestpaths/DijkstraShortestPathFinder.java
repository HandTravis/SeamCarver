package graphs.shortestpaths;

import priorityqueues.ExtrinsicMinPQ;
import priorityqueues.NaiveMinPQ;
import graphs.BaseEdge;
import graphs.Graph;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {
    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        return new NaiveMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, Double> distTo = new HashMap<>();
        Map<V, E> shortestPathTree = new HashMap<>();
        Set<V> known = new HashSet<>();
        ExtrinsicMinPQ<V> pq = createMinPQ();

        distTo.put(start, 0.0);
        pq.add(start, 0.0);

        while (!pq.isEmpty()) {
            V current = pq.removeMin();
            known.add(current);
            if (current.equals(end)) {
                break;
            }
            for (E edge : graph.outgoingEdgesFrom(current)) {
                V neighbor = edge.to();
                double weight = edge.weight();
                double distNew = distTo.get(current) + weight;
                double oldDist;
                if (!distTo.containsKey(neighbor)) {
                    oldDist = Double.POSITIVE_INFINITY;
                } else {
                    oldDist = distTo.get(neighbor);
                }
                if (distNew < oldDist) {
                    distTo.put(neighbor, distNew);
                    shortestPathTree.put(neighbor, edge);

                    if (pq.contains(neighbor)) {
                        pq.changePriority(neighbor, distNew);
                    } else if (!known.contains(neighbor)) {
                        pq.add(neighbor, distNew);
                    }
                }
            }
        }
        return shortestPathTree;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        } else if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }
        V current = end;
        List<E> edges = new ArrayList<>();
        while (!Objects.equals(current, start)) {
            E edge = spt.get(current);
            if (edge == null) {
                return new ShortestPath.Failure<>();
            }
            edges.add(edge);
            current = edge.from();
        }
        Collections.reverse(edges);
        return new ShortestPath.Success<>(edges);
    }

}
