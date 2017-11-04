import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    // constructor takes a WordNet object
    private WordNet wn;
    public Outcast(WordNet wordnet) {
        wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast_noun = "";
        Integer max_dist = 0;
        for(String n1 : nouns){
            Integer dist = 0;
            for(String n2 : nouns) {
                dist += wn.distance(n1, n2);
            }

            if (dist > max_dist){
                max_dist = dist;
                outcast_noun = n1;
            }
        }

        return outcast_noun;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}