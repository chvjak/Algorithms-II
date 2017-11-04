import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private HashMap<String, ArrayList<Integer>> nouns_to_ids ;
    private HashMap<Integer, ArrayList<String>> ids_to_nouns;
    private Digraph G;
    private SAP s;

        // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        if (synsets == null || hypernyms == null){
            throw new java.lang.IllegalArgumentException();
        }

        In in = new In(synsets);
        String line;
        String cvsSplitBy = ",";


        nouns_to_ids = new HashMap<>();
        ids_to_nouns = new HashMap<>();
        Integer noun_count = 0;

        while ((line = in.readLine()) != null) {
            String[] noun_info = line.split(cvsSplitBy);
            Integer id = Integer.parseInt(noun_info[0]);
            if (noun_info[1].indexOf(' ') != -1)
                for (String noun : noun_info[1].split("\\s+")){
                    if (!nouns_to_ids.containsKey(noun))
                        nouns_to_ids.put(noun, new ArrayList<Integer>());
                    if (!ids_to_nouns.containsKey(id))
                        ids_to_nouns.put(id, new  ArrayList<String>());

                    nouns_to_ids.get(noun).add(id);
                    ids_to_nouns.get(id).add(noun);
                }
            else{
                String noun = noun_info[1];
                if (!nouns_to_ids.containsKey(noun))
                    nouns_to_ids.put(noun, new ArrayList<Integer>());
                if (!ids_to_nouns.containsKey(id))
                    ids_to_nouns.put(id, new  ArrayList<String>());

                nouns_to_ids.getOrDefault(noun, new ArrayList<Integer>()).add(id);
                ids_to_nouns.getOrDefault(id, new  ArrayList<String>()).add(noun);
            }



            noun_count++;
        }

        in.close();

        G = new Digraph(noun_count);

        In in2 = new In(hypernyms);
        while ((line = in2.readLine()) != null) {
            String[] hypernym_relations = line.split(cvsSplitBy);
            Integer from_node = Integer.parseInt(hypernym_relations[0]);
            for (int i = 1; i < hypernym_relations.length; i++) {
                Integer to_node = Integer.parseInt(hypernym_relations[0]);
                G.addEdge(from_node, to_node);
            }
        }

        in2.close();

        s = new SAP(G);

    }


    // returns all WordNet nouns
    public Iterable<String> nouns(){
        return nouns_to_ids.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        if (word == null){
            throw new java.lang.IllegalArgumentException();
        }

        return nouns_to_ids.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if (!isNoun(nounA) || !isNoun(nounB)){
            throw new java.lang.IllegalArgumentException();
        }

        int dist = s.length(nouns_to_ids.get(nounA), nouns_to_ids.get(nounB));

        return dist;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if (!isNoun(nounA) || !isNoun(nounB)){
            throw new java.lang.IllegalArgumentException();
        }

        int sap_id = s.ancestor(nouns_to_ids.get(nounA), nouns_to_ids.get(nounB));
        if (sap_id != -1)
            return ids_to_nouns.get(sap_id).get(0);
        else
            return "";
    }

    // do unit testing of this class
    public static void main(String[] args){
        WordNet wn = new WordNet("synsets.txt", "hypernyms.txt");
        boolean b = wn.isNoun("anamorphosis");


    }

}