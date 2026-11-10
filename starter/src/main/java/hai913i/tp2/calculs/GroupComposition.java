package hai913i.tp2.calculs;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hai913i.tp2.models.CouplingGraph;
import hai913i.tp2.models.Fusion;
import hai913i.tp2.models.Groupe;
import hai913i.tp2.models.Node;
import hai913i.tp2.utils.DotExporter;
import hai913i.tp2.utils.TreeExporter;

public class GroupComposition {
   
   
    public static List<Groupe> initialization(List<CouplingGraph> graph) {
        List<Groupe> groups = new ArrayList<>();

        //Récupérer les classes uniques
        Set<String> uniqueClasses = new LinkedHashSet<>();
        for (CouplingGraph e : graph) {
            if (e.source != null) uniqueClasses.add(e.source);
            if (e.target != null) uniqueClasses.add(e.target);
        }
        List<String> alphabeticalOrderedClasses = new ArrayList<>(uniqueClasses);
        alphabeticalOrderedClasses.sort(String::compareTo);

        //Un groupe par classe
        int groupeName = 65; // 'A' comme code ASCII pour le nom du groupe
        int order = 0, id = 0;
        for (String classe : alphabeticalOrderedClasses) {
            Groupe g = new Groupe();
            g.id = id++;
            g.name = String.valueOf((char) groupeName);
            g.size = 1;
            g.addOrder = order++;
            g.similarity = 0.0;         
            g.classes = List.of(classe);  
            g.node = new Node(g.id, classe);
            groups.add(g);
            groupeName++;
        }
        return groups;
    }


    public static List<Fusion> createGroup(List<Groupe> groups, List<CouplingGraph> graph) {
        List<Fusion> fusionList = new ArrayList<>();
        int step = 1;
        int nextId = groups.size(); // M = 15. Le prochain ID libre est 15.
        double epsilon = 1e-9; // Tolérance pour les doubles

        while (groups.size() > 1) {
            double maxSimilarity = -1.0;
            int group1Index = -1, group2Index = -1;
            // Pour stocker le meilleur couple normalisé (min, max)
            int bestMin = Integer.MAX_VALUE;
            int bestMax = Integer.MAX_VALUE;
                for (int i = 0; i < groups.size(); i++) {
                    for (int j = i + 1; j < groups.size(); j++) {
                        double sim = calculateSimilarity(groups.get(i), groups.get(j), graph);
                        // Normalisation du couple
                        int idA = groups.get(i).id;
                        int idB = groups.get(j).id;
                        int coupleMin = Math.min(idA, idB);
                        int coupleMax = Math.max(idA, idB);
                        boolean isBetter = false;
                
                        if (sim > maxSimilarity + epsilon) {
                           isBetter = true;
                        }else if(Math.abs(sim - maxSimilarity) <= epsilon ){
                              // Règle de départage (5.4) : le plus petit couple lexicographique
                            if (coupleMin < bestMin || (coupleMin == bestMin && coupleMax < bestMax)) {
                                isBetter = true;
                            }
                        }
                        if (isBetter) {
                            maxSimilarity = sim;
                            group1Index = i;
                            group2Index = j;
                            bestMin = coupleMin;
                            bestMax = coupleMax;
                        }
                    }
                
                }
                
         
            Groupe g1 = groups.get(group1Index);
            Groupe g2 = groups.get(group2Index);
            groups.remove(Math.max(group1Index, group2Index));
            groups.remove(Math.min(group1Index, group2Index));
            Groupe newGroup = fusionGroups(g1, g2, maxSimilarity, nextId);
            groups.add(newGroup);
            fusionList.add(createFusion(g1, g2, maxSimilarity)); //ajout de la nouvelle fusion a la liste des fusions
            nextId++;
            printGroups(g1, g2, newGroup, step);
            step++;
        }
        Node root = groups.get(0).node;
        TreeBuilder.printTree(root, "", true);
        TreeExporter.exportToJson(root, "dendrogramme.json");
        DotExporter.exportDendrogramToDot(root, "dendrogramme.dot");
        return fusionList;
    }

    public static Fusion createFusion(Groupe g1, Groupe g2, double similarity) {
        Fusion fusion = new Fusion();
        fusion.g1 = g1;
        fusion.g2 = g2;
        fusion.similarity = similarity;
        return fusion;
    }
    public static Groupe fusionGroups(Groupe g1, Groupe g2, double similarity,int newId) {
        Groupe newGroup = new Groupe();
        newGroup.id = newId;
        newGroup.name = g1.name + "," + g2.name;
        newGroup.size = g1.size + g2.size;
        newGroup.addOrder = Math.min(g1.addOrder, g2.addOrder);
        newGroup.similarity = similarity;                    
        newGroup.classes = new ArrayList<>();
        newGroup.classes.addAll(g1.classes);
        newGroup.classes.addAll(g2.classes);
        newGroup.node = new Node(newId, g1.node, g2.node, similarity);

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

        int nbCouples = g1.classes.size() * g2.classes.size(); 
        return nbCouples > 0 ? totalWeight / nbCouples : 0.0;
    }
    public static void printGroups(Groupe g1, Groupe g2, Groupe newGroup, int step) {
        System.out.println("\n==== Fusion " + step + " ==== ");
        
        // Affichage des deux groupes fusionnés
        System.out.println("Groupe A [ID " + g1.id + "] : {" + g1.classes + "}");
        System.out.println("Groupe B [ID " + g2.id + "] : {" + g2.classes + "}");
        
        // Affichage du résultat
        System.out.println("-> Nouveau groupe [ID " + newGroup.id + "] : {" + newGroup.classes + "}");
        System.out.println("-> Similarité (lien moyen) : " + String.format("%.4f", newGroup.similarity));
    }
}
