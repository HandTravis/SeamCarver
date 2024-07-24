package seamcarving;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.DijkstraShortestPathFinder;
import graphs.shortestpaths.ShortestPathFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DijkstraSeamFinder implements SeamFinder {
    private final ShortestPathFinder<Graph<Vertex, Edge<Vertex>>, Vertex, Edge<Vertex>> pathFinder;

    public DijkstraSeamFinder() {
        this.pathFinder = createPathFinder();

    }

    protected <G extends Graph<V, Edge<V>>, V> ShortestPathFinder<G, V, Edge<V>> createPathFinder() {
        /*
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
        */
        return new DijkstraShortestPathFinder<>();
    }

    @Override
    public List<Integer> findHorizontalSeam(double[][] energies) {
        SeamGraph graph = new SeamGraph(energies, false);
        List<Vertex> vertices = pathFinder.findShortestPath(graph, graph.source, graph.sink).vertices();
        vertices = vertices.subList(1, vertices.size() - 1);
        List<Integer> path = new ArrayList<>(vertices.size());
        for (Vertex v : vertices) {
            path.add(v.row);
        }
        return path;
    }

    @Override
    public List<Integer> findVerticalSeam(double[][] energies) {
        SeamGraph graph = new SeamGraph(energies, true);
        List<Vertex> vertices = pathFinder.findShortestPath(graph, graph.source, graph.sink).vertices();
        vertices = vertices.subList(1, vertices.size() - 1);
        List<Integer> path = new ArrayList<>(vertices.size());
        for (Vertex v : vertices) {
            path.add(v.col);
        }
        return path;
    }
    private class SeamGraph implements Graph<Vertex, Edge<Vertex>> {
        private final double[][] energies;
        private final Vertex source;
        private final Vertex sink;
        private final Map<Integer, List<Vertex>> vertices;

        public SeamGraph(double[][] energies, boolean transpose) {
            this.energies = energies;
            this.source = new Vertex();
            this.sink = new Vertex(-2, -2);
            this.vertices = new HashMap<>();
            // int size1 = energies.length is 6
            // int size2 = energies[0].length is 5
            if (transpose) {
                for (int r = 0; r < height(); r++) {
                    vertices.put(r, new ArrayList<>(width()));
                    for (int c = 0; c < width(); c++) {
                        Vertex v = new Vertex(r, c);
                        vertices.get(r).add(v);
                    }
                }
                for (int i = 0; i < width(); i++) {
                    Vertex to = vertices.get(0).get(i);
                    source.neighbors.add(new Edge<Vertex>(source, to, energies[i][0]));
                }
                for (int k = 0; k < width(); k++) {
                    Vertex from = vertices.get(height() - 1).get(k);
                    from.neighbors.add(new Edge<Vertex>(from, sink, 0));
                }
                for (int r = 0; r < height() - 1; r++) {
                    for (int c = 0; c < width(); c++) {
                        Vertex from = vertices.get(r).get(c);
                        Vertex to = vertices.get(r + 1).get(c);
                        from.neighbors.add(new Edge<Vertex>(from, to, energies[c][r + 1]));
                        if (c < width() - 1) {
                            to = vertices.get(r + 1).get(c + 1);
                            from.neighbors.add(new Edge<Vertex>(from, to, energies[c + 1][r + 1]));
                        }
                        if (c > 0) {
                            to = vertices.get(r + 1).get(c - 1);
                            from.neighbors.add(new Edge<Vertex>(from, to, energies[c - 1][r + 1]));
                        }
                    }
                }
            } else {
                for (int c = 0; c < width(); c++) {
                    vertices.put(c, new ArrayList<>(height()));
                    for (int r = 0; r < height(); r++) {
                        Vertex v = new Vertex(r, c);
                        vertices.get(c).add(v);
                    }
                }
                for (int j = 0; j < height(); j++) {
                    Vertex to = vertices.get(0).get(j);
                    source.neighbors.add(new Edge<Vertex>(source, to, energies[0][j]));
                }
                for (int z = 0; z < height(); z++) {
                    Vertex from = vertices.get(width() - 1).get(z);
                    from.neighbors.add(new Edge<Vertex>(from, sink, 0));
                }
                for (int c = 0; c < width() - 1; c++) {
                    for (int r = 0; r < height(); r++) {
                        Vertex from = vertices.get(c).get(r);
                        Vertex to = vertices.get(c + 1).get(r);
                        from.neighbors.add(new Edge<Vertex>(from, to, energies[c + 1][r]));
                        if (r < height() - 1) {
                            to = vertices.get(c + 1).get(r + 1);
                            from.neighbors.add(new Edge<Vertex>(from, to, energies[c + 1][r + 1]));
                        }
                        if (r > 0) {
                            to = vertices.get(c + 1).get(r - 1);
                            from.neighbors.add(new Edge<Vertex>(from, to, energies[c + 1][r - 1]));
                        }
                    }
                }
            }
        }

        public int height() { return energies[0].length; }
        public int width() { return energies.length; }

        @Override
        public Collection<Edge<Vertex>> outgoingEdgesFrom(Vertex vertex) {
            return Collections.unmodifiableCollection(vertex.neighbors);
        }
    }

    private class Vertex implements Comparable<Vertex> {
        private final int row;
        private final int col;
        private final List<Edge<Vertex>> neighbors;

        public Vertex(int row, int col) {
            this.row = row;
            this.col = col;
            this.neighbors = new ArrayList<>(3);
        }

        public Vertex() { this(-1, -1); }

        public String toString() {
            return "Vertex: Row: " + row + "Col: " + col;
        }


        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Vertex)) {
                return false;
            }

            Vertex o = (Vertex) other;
            return this.col == o.col && this.row == o.row;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }


        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(this.hashCode(), o.hashCode());
        }
    }

}
