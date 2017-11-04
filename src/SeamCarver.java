import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.DijkstraSP;

import java.awt.Color;
import java.util.ArrayList;

public class SeamCarver {
    private Picture pic;

    private Picture copy_pic(Picture picture){
        Picture new_pic = new Picture(picture.width(), picture.height());
        int width = picture.width();
        int height = picture.height();

        for (int col = 0; col < width; col++)
            for (int row = 0; row < height; row++)
                new_pic.set(col, row, picture.get(col, row));

        return new_pic;
    }

    public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
    {
        if (picture == null){
            throw new java.lang.IllegalArgumentException();
        }

        pic = copy_pic(picture);
    }

    private int coords_to_id(int x, int y){
        return y * width() + x;
    }

    private int[] id_to_coords(int node_id){
        return new int[]{node_id % width(), node_id / width()};
    }

    private boolean is_valid_coord(int x, int y){
        return (x >= 0 && x < pic.width() && y >= 0 && y < pic.height());
    }

    private int[] arrlist_to_int(ArrayList<Integer> al){
        int[] res = new int[al.size()];
        for (int i = 0; i < al.size(); i++){
            res[i] = al.get(i);
        }

        return res;
    }


    private int[] get_neighbors_v(int node_id){
        int[] coords = id_to_coords(node_id);

        int x = coords[0];
        int y = coords[1];


        int[][] neighbor_coords = {{x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}};

        ArrayList<Integer> res = new ArrayList<Integer>();
        for (int[] c: neighbor_coords){
            int xn = c[0];
            int yn = c[1];
            if (is_valid_coord(xn, yn)){
                res.add(coords_to_id(xn, yn));
            }
        }

        return arrlist_to_int(res);

    }

    private int[] get_neighbors_h(int node_id){
        int[] coords = id_to_coords(node_id);

        int x = coords[0];
        int y = coords[1];


        int[][] neighbor_coords = {{x + 1, y + 1}, {x + 1, y}, {x + 1, y - 1}};

        ArrayList<Integer> res = new ArrayList<Integer>();
        for (int[] c: neighbor_coords){
            int xn = c[0];
            int yn = c[1];
            if (is_valid_coord(xn, yn)){
                res.add(coords_to_id(xn, yn));
            }
        }

        return arrlist_to_int(res);

    }


    private double get_energy(int node_id){
        int[] coords = id_to_coords(node_id);

        return energy(coords[0], coords[1]);
    }

    public Picture picture()                          // current picture
    {
        return copy_pic(pic);
    }

    public int width()                            // width of current picture
    {
        return pic.width();
    }
    public int height()                           // height of current picture
    {
        return pic.height();
    }

    public  double energy(int x, int y)               // energy of pixel at column x and row y
    {
        if (!is_valid_coord(x, y)){
            throw new java.lang.IllegalArgumentException();
        }

        if (x == 0 || x == pic.width() - 1 || y == 0 || y == pic.height() - 1){
            return 1000;
        }

        Color c1 = pic.get(x - 1, y);
        double r1 = c1.getRed();
        double g1 = c1.getGreen();
        double b1 = c1.getBlue();

        Color c2 = pic.get(x + 1, y);
        double r2 = c2.getRed();
        double g2 = c2.getGreen();
        double b2 = c2.getBlue();

        Color c3 = pic.get(x, y - 1);
        double r3 = c3.getRed();
        double g3 = c3.getGreen();
        double b3 = c3.getBlue();

        Color c4 = pic.get(x, y + 1);
        double r4 = c4.getRed();
        double g4 = c4.getGreen();
        double b4 = c4.getBlue();

        double delta_x = (r1 - r2) * (r1 - r2)   + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);
        double delta_y = (r3 - r4) * (r3 - r4)   + (g3 - g4) * (g3 - g4) + (b3 - b4) * (b3 - b4);

        return Math.sqrt(delta_x + delta_y);
    }

    private EdgeWeightedDigraph get_g_v()
    {
        int N = pic.height() * pic.width();
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(N + 4);

        int top_node = N;
        int bottom_node = N + 1;
        int left_node = N + 2;
        int right_node = N + 3;


        for(int y = 0; y < pic.height(); y++){
            for(int x = 0; x < pic.width(); x++){
                int from_node = coords_to_id(x, y);
                for (int to_node : get_neighbors_v(from_node)){
                    double w = get_energy(to_node);
                    g.addEdge(new DirectedEdge(from_node, to_node, w));
                }
            }
        }

        for(int x = 0; x < pic.width(); x++){
            g.addEdge(new DirectedEdge(top_node, coords_to_id(x, 0), 0));
            g.addEdge(new DirectedEdge(coords_to_id(x, pic.height() - 1), bottom_node, 0));
        }


        return g;
    }

    private EdgeWeightedDigraph get_g_h()
    {
        int N = pic.height() * pic.width();
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(N + 4);

        int top_node = N;
        int bottom_node = N + 1;
        int left_node = N + 2;
        int right_node = N + 3;


        for(int y = 0; y < pic.height(); y++){
            for(int x = 0; x < pic.width(); x++){
                int from_node = coords_to_id(x, y);
                for (int to_node : get_neighbors_h(from_node)){
                    double w = get_energy(to_node);
                    g.addEdge(new DirectedEdge(from_node, to_node, w));
                }
            }
        }

        for(int y = 0; y < pic.height(); y++){
            g.addEdge(new DirectedEdge(left_node, coords_to_id(0, y), 0));
            g.addEdge(new DirectedEdge(coords_to_id(pic.width() - 1, y), right_node, 0));
        }

        return g;
    }

    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        int N = pic.height() * pic.width();

        int top_node = N;
        int bottom_node = N + 1;
        int left_node = N + 2;
        int right_node = N + 3;


        DijkstraSP dijkstra = new DijkstraSP(get_g_h(), left_node);
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (DirectedEdge edge: dijkstra.pathTo(right_node))
        {
            int[] coords = id_to_coords(edge.to());

            indices.add(coords[1]);
        }

        // indices.size() - 1 <= omit last node which is virtual
        int[] res = new int[indices.size() - 1];

        for(int i = 0; i < indices.size() - 1; i++){
            res[i] = (int)indices.get(i);
        }

        return res;
    }

    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        int N = pic.height() * pic.width();

        int top_node = N;
        int bottom_node = N + 1;
        int left_node = N + 2;
        int right_node = N + 3;

        DijkstraSP dijkstra = new DijkstraSP(get_g_v(), top_node);
        ArrayList<Integer> indices = new ArrayList<Integer>();
        for (DirectedEdge edge: dijkstra.pathTo(bottom_node))
        {
            int[] coords = id_to_coords(edge.to());

            indices.add(coords[0]);
        }

        // indices.size() - 1 <= omit last node which is virtual
        int[] res = new int[indices.size() - 1];

        for(int i = 0; i < indices.size() - 1; i++){
            res[i] = indices.get(i);
        }

        return res;
    }

    /*
     Throw a java.lang.IllegalArgumentException if removeVerticalSeam() or removeHorizontalSeam() is called with
     an array of the wrong length or if the array is not a valid seam (i.e., either an entry is outside its
     prescribed range or two adjacent entries differ by more than 1).

     Throw a java.lang.IllegalArgumentException if removeVerticalSeam() is called when the width of the picture
     is less than or equal to 1 or if removeHorizontalSeam() is called when the height of the picture is less than or equal to 1.
    * */

    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        Picture no_seam_pic = new Picture(pic.width(), pic.height() - 1);
        int width = pic.width();
        int height = pic.height();

        if (seam == null || seam.length > width || height <= 1){
            throw new java.lang.IllegalArgumentException();
        }

        for(int i = 1; i < seam.length; i++)
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new java.lang.IllegalArgumentException();


        for (int col = 0; col < width; col++) {
            if (!is_valid_coord(col, seam[col])){
                throw new java.lang.IllegalArgumentException();
            }

            for (int row = 0; row < seam[col]; row++) {
                no_seam_pic.set(col, row, pic.get(col, row));
            }

            for (int row = seam[col] + 1; row < height; row++) {
                no_seam_pic.set(col, row - 1, pic.get(col, row));
            }

        }

        pic = no_seam_pic;
    }

    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        Picture no_seam_pic = new Picture(pic.width() - 1, pic.height());
        int width = pic.width();
        int height = pic.height();

        if (seam == null || seam.length > height|| width <= 1){
            throw new java.lang.IllegalArgumentException();
        }

        for(int i = 1; i < seam.length; i++)
            if (Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new java.lang.IllegalArgumentException();

        for (int row = 0; row < height; row++) {
            if (!is_valid_coord(seam[row], row)){
                throw new java.lang.IllegalArgumentException();
            }

            for (int col = 0; col < seam[row]; col++) {
                no_seam_pic.set(col, row, pic.get(col, row));
            }

            for (int col = seam[row] + 1; col < width; col++) {
                no_seam_pic.set(col - 1, row, pic.get(col, row));
            }

        }

        pic = no_seam_pic;

    }


}
