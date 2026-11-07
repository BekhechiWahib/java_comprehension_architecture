package hai913i.tp2.models;

import java.util.List;

public class CouplingGraph {
  public String source; // nom de la classe source
  public String target; // nom de la classe cible (null si la classe est isolée)
  public double weight; // poids du couplage entre les deux classes (0.0 si la classe est isolée)
    public CouplingGraph(String source, String target, double weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

    public static void printGraph(List<CouplingGraph> couplingGraph) {
        System.out.println("\n=== Graphe de Couplage Pondere ===\n");
        for (int i = 0; i < couplingGraph.size(); i++) {
            System.out.printf("Couplage entre %s --- %s : %.4f%n",
                    couplingGraph.get(i).source, couplingGraph.get(i).target, couplingGraph.get(i).weight);
        }
    }
}
