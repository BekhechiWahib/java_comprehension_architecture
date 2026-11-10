package hai913i.tp2.utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import hai913i.tp2.models.Node;

public class DotExporter {

    public static void exportDendrogramToDot(Node root, String filename) {
        StringBuilder sb = new StringBuilder();
        
        // En-tête du graphe (non orienté : "graph" et non "digraph")
        sb.append("graph Dendrogram {\n");
        sb.append("  node [shape=box, fontsize=10];\n");
        
        // Appel de la méthode récursive pour construire les nœuds et arêtes
        buildDot(root, sb);
        
        sb.append("}\n");
        
        // Écriture dans le dossier export
        try {
            Path dir = Path.of("src/exports");
            Files.createDirectories(dir);
            Path file = dir.resolve(filename);
            Files.writeString(file, sb.toString());
            System.out.println("DOT écrit : " + file.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void buildDot(Node node, StringBuilder sb) {
        if (node == null) return;

        if (node.left == null && node.right == null) {
            // C'est une FEUILLE (classe)
            sb.append("  ").append(node.id)
              .append(" [label=\"").append(node.classes.get(0)).append("\"];\n");
        } else {
            // C'est un NŒUD INTERNE (fusion)
            sb.append("  ").append(node.id)
              .append(" [label=\"Fusion ID ").append(node.id)
              .append("\\n(sim: ").append(String.format(java.util.Locale.US, "%.4f", node.similarity)).append(")\"];\n");
            
            // Arêtes vers les enfants (-- pour graphe non orienté)
            sb.append("  ").append(node.id).append(" -- ").append(node.left.id).append(";\n");
            sb.append("  ").append(node.id).append(" -- ").append(node.right.id).append(";\n");
            
            // Appels récursifs
            buildDot(node.left, sb);
            buildDot(node.right, sb);
        }
    }
}