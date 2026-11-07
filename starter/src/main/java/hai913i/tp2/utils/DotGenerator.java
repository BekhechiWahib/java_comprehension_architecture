package hai913i.tp2.utils;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import hai913i.tp2.models.CouplingGraph;


public class DotGenerator {
    //TODO : stocker le fichier dans exports
    public static String toDot(List<CouplingGraph> graph) {
        StringBuilder sb = new StringBuilder();
        sb.append("graph couplingGraph {\n");
        sb.append("  layout=neato;\n");
        sb.append("  node [shape=box, fontsize=10];\n");

        // 1. Déclarer tous les nœuds (arêtes + isolés), sans doublon
        Set<String> declared = new LinkedHashSet<>();
        for (CouplingGraph e : graph) {
            if (e.source != null) declared.add(e.source);
            if (e.target != null) declared.add(e.target);
        }
        for (String n : declared) {
            sb.append("  \"").append(n).append("\";\n");
        }

        // 2. Déclarer les arêtes (ignorer les isolés où target == null)
        for (CouplingGraph e : graph) {
            if (e.target == null) continue;
            sb.append("  \"").append(e.source).append("\" -- \"")
              .append(e.target).append("\" [label=\"")
              .append(String.format("%.3f", e.weight)).append("\"];\n");
        }

        sb.append("}\n");
        return sb.toString();
    }
}
