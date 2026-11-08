package hai913i.tp2.calculs;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hai913i.tp2.models.CouplingGraph;
import hai913i.tp2.models.Groupe;

public class GroupComposition {
   
   
    public static List<Groupe> initialization(List<CouplingGraph> graph) {
        List<Groupe> groups = new ArrayList<>();

        //Récupérer les classes uniques
        Set<String> uniqueClasses = new LinkedHashSet<>();
        for (CouplingGraph e : graph) {
            if (e.source != null) uniqueClasses.add(e.source);
            if (e.target != null) uniqueClasses.add(e.target);
        }

        //Un groupe par classe
        int groupeName = 65; // 'A' comme code ASCII pour le nom du groupe
        int order = 0;
        for (String classe : uniqueClasses) {
            Groupe g = new Groupe();
            g.name = String.valueOf((char) groupeName);
            g.size = 1;
            g.addOrder = order++;
            g.similarity = 0.0;         
            g.classes = List.of(classe);  
            groups.add(g);
            groupeName++;
        }
        return groups;
    }


    public static void createGroup(List<Groupe> groups, List<CouplingGraph> graph) {
        while (groups.size() > 1) {
            double maxSimilarity = -1.0;
            int group1Index = -1, group2Index = -1;
            int step = 1;

            for (int i = 0; i < groups.size(); i++) {
                for (int j = i + 1; j < groups.size(); j++) {
                    double sim = calculateSimilarity(groups.get(i), groups.get(j), graph);
                    if (sim > maxSimilarity) {
                        maxSimilarity = sim;
                        group1Index = i;
                        group2Index = j;
                    }
                }
            }

            Groupe newGroup = fusionGroups(groups.get(group1Index), groups.get(group2Index), groups, maxSimilarity);
            printGroups(newGroup,step);
            step++;
        }
    }

    public static Groupe fusionGroups(Groupe g1, Groupe g2, List<Groupe> groups, double similarity) {
        Groupe newGroup = new Groupe();
        newGroup.name = g1.name + g2.name;
        newGroup.size = g1.size + g2.size;
        newGroup.addOrder = Math.min(g1.addOrder, g2.addOrder);
        newGroup.similarity = similarity;                    
        newGroup.classes = new ArrayList<>();
        newGroup.classes.addAll(g1.classes);
        newGroup.classes.addAll(g2.classes);

        groups.remove(g2);
        groups.remove(g1);
        groups.add(newGroup);
        return newGroup;
    }

    public static double calculateSimilarity(Groupe g1, Groupe g2, List<CouplingGraph> graph) {
        double totalWeight = 0.0;

        for (CouplingGraph e : graph) {
            if (e.source == null || e.target == null) continue;   // ← isolés ignorés
            if (e.weight == 0.0) continue;                        // ← poids nuls ignorés

            if ((g1.classes.contains(e.source) && g2.classes.contains(e.target)) ||
                (g1.classes.contains(e.target) && g2.classes.contains(e.source))) {
                totalWeight += e.weight;
            }
        }

        int nbCouples = g1.classes.size() * g2.classes.size();    // ← |G1| × |G2|
        return nbCouples > 0 ? totalWeight / nbCouples : 0.0;
    }
    public static void printGroups(Groupe g,int step) {
         System.out.println("\n==== Fusion ==== \n");
        
            System.out.println("Groupe " + g.name + " : {" + g.classes + "}");
            System.out.println("  Similarité : " + g.similarity);
        
    }
}
