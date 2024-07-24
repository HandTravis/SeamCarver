package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.ShortestPathFinder;

public class BasicDijkstraSeamFinderTests extends BasicSeamFinderTests {

    protected SeamFinder createSeamFinder() {
       return new DijkstraSeamFinder() {
            @Override
            protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
                return super.createPathFinder();
            }
        };
    }
}
