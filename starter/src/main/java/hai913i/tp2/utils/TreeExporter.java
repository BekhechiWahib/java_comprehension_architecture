package hai913i.tp2.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import hai913i.tp2.models.Node;

public class TreeExporter {

    public static void exportToJson(Node root, String filename) {
        StringBuilder sb = new StringBuilder();
        buildJson(root, sb, 0); // On commence à l'indentation 0
        
        // Écriture dans le dossier export
        try {
            Path dir = Path.of("src/exports");
            Files.createDirectories(dir);
            Path file = dir.resolve(filename);
            Files.writeString(file, sb.toString());
            System.out.println("JSON écrit : " + file.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void buildJson(Node node, StringBuilder sb, int indentLevel) {
        String indent = "  ".repeat(indentLevel); // 2 espaces par niveau

        sb.append(indent).append("{\n");
        
        // 1. L'ID du nœud
        sb.append(indent).append("  \"id\": ").append(node.id).append(",\n");
        
        // 2. La similarité
        sb.append(indent).append("  \"similarity\": ").append(String.format(java.util.Locale.US,"%.4f", node.similarity)).append(",\n");
        
        // 3. Les classes (formatées en tableau JSON)
        sb.append(indent).append("  \"classes\": [");
        for (int i = 0; i < node.classes.size(); i++) {
            sb.append("\"").append(node.classes.get(i)).append("\"");
            if (i < node.classes.size() - 1) sb.append(", ");
        }
        sb.append("]");

        // 4. Les enfants (si ce n'est pas une feuille)
        if (node.left != null && node.right != null) {
            sb.append(",\n");
            sb.append(indent).append("  \"left\": ");
            buildJson(node.left, sb, indentLevel + 1); // Appel récursif (fils gauche)
            sb.append(",\n");
            sb.append(indent).append("  \"right\": ");
            buildJson(node.right, sb, indentLevel + 1); // Appel récursif (fils droit)
        }
        
        sb.append("\n").append(indent).append("}");
    }
}