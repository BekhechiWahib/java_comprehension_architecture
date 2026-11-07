package hai913i.tp2.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hai913i.tp2.models.Couple;
import hai913i.tp2.models.CouplingGraph;
import hai913i.tp2.utils.CSVGenerator;

public class CouplingGraphGenerator {
    
    public static List<CouplingGraph> generateGraph(Map<Couple, Double> coupling, List<String> isolatedClasses) {
      List<CouplingGraph> couplingMap = new ArrayList<>();

       for (Map.Entry<Couple, Double> entry : coupling.entrySet()) {
            Couple couple = entry.getKey();
            double value = Math.round(entry.getValue() * 10000.0) / 10000.0; // arrondi à 4 décimales
            couplingMap.add(new CouplingGraph(couple.getSource(), couple.getTarget(),value));
        }
        for (String isolatedClass : isolatedClasses) {
            //une classe isolée est représentée par un sommet sans arcs, donc on peut ajouter un CouplingGraph avec un poids de 0.0 et une cible nulle
            couplingMap.add(new CouplingGraph(isolatedClass, null, 0.0));
        }
         CSVGenerator.generateCsvFile("Coupling_Graph",couplingMap);
        return couplingMap;
    }
}