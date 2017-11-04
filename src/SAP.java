import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Queue;

public class SAP {
    private Digraph g ;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        g = new Digraph(G);
    }


    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v > g.V() - 1 || w < 0 || w > g.V() - 1){
            throw new java.lang.IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(g, w);

        // O(N)
        Integer min_dist = Integer.MAX_VALUE;


        for (int v1 = 0; v1 < g.V(); v1++)
            if (bfsV.hasPathTo(v1))
                for(Integer nv : bfsV.pathTo(v1)){
                    if (bfsW.hasPathTo(nv))
                        if (bfsV.distTo(nv) + bfsW.distTo(nv) < min_dist){
                            min_dist = bfsV.distTo(nv) + bfsW.distTo(nv);
                        }
                }

        return min_dist == Integer.MAX_VALUE? -1 : min_dist;
    }


    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v > g.V() - 1 || w < 0 || w > g.V() - 1){
            throw new java.lang.IllegalArgumentException();
        }

        // all nodes reachable and its distances from v
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(g, v);

        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(g, w);

        // O(N)
        Integer min_dist = Integer.MAX_VALUE;
        Integer res_node_id = -1;
        for (int v1 = 0; v1 < g.V(); v1++)
            if (bfsV.hasPathTo(v1))
                for(Integer nv : bfsV.pathTo(v1)){
                    if (bfsW.hasPathTo(nv))
                        if (bfsV.distTo(nv) + bfsW.distTo(nv) < min_dist) {
                            min_dist = bfsV.distTo(nv) + bfsW.distTo(nv);
                            res_node_id = nv;
                        }
                }

        return res_node_id;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new java.lang.IllegalArgumentException();

        for (int v1 :v){
            if (v1 < 0 || v1 > g.V() - 1){
                throw new java.lang.IllegalArgumentException();
            }
        }

        for (int v1 :w){
            if (v1 < 0 || v1 > g.V() - 1){
                throw new java.lang.IllegalArgumentException();
            }
        }

        // all nodes reachable and its distances from v
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(g, w);

        // O(N)
        Integer min_dist = Integer.MAX_VALUE;

        for (int v1 = 0; v1 < g.V(); v1++)
            if (bfsV.hasPathTo(v1))
                for(Integer nv : bfsV.pathTo(v1)){
                    if (bfsW.hasPathTo(nv))
                        if (bfsV.distTo(nv) + bfsW.distTo(nv) < min_dist) {
                            min_dist = bfsV.distTo(nv) + bfsW.distTo(nv);
                        }
                }
        return min_dist == Integer.MAX_VALUE? -1 : min_dist;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new java.lang.IllegalArgumentException();

        for (int v1 :v){
            if (v1 < 0 || v1 > g.V() - 1){
                throw new java.lang.IllegalArgumentException();
            }
        }

        for (int v1 :w){
            if (v1 < 0 || v1 > g.V() - 1){
                throw new java.lang.IllegalArgumentException();
            }
        }


        // all nodes reachable and its distances from v
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(g, w);

        // O(N)
        Integer min_dist = Integer.MAX_VALUE;
        Integer res_node_id = -1;
        for (int v1 = 0; v1 < g.V(); v1++)
            if (bfsV.hasPathTo(v1))
                for(Integer nv : bfsV.pathTo(v1)){
                    if (bfsW.hasPathTo(nv))
                        if (bfsV.distTo(nv) + bfsW.distTo(nv) < min_dist){
                            min_dist = bfsV.distTo(nv) + bfsW.distTo(nv);
                            res_node_id = nv;
                        }
                }

        return res_node_id;
    }

    // do unit testing of this class
    public static void main(String[] args){

    }
}