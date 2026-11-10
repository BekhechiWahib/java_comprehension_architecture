package hai913i.tp2.calculs;

import hai913i.tp2.models.Node;

public class TreeBuilder { 

    public static void printTree(Node node, String prefix, boolean isLast) {
        if (node == null) return;

        if (node.left == null && node.right == null) {
            System.out.println(prefix + (isLast ? "└── " : "├── ") + node.classes.get(0));
        } else {
                System.out.printf("%s%s[Fusion ID %d] (sim: %.4f)%n", 
                prefix, 
                (isLast ? "└── " : "├── "), 
                node.id, 
                node.similarity);
                String childPrefix = prefix + (isLast ? "    " : "│   ");
            
            printTree(node.left, childPrefix, false);
            printTree(node.right, childPrefix, true);
        }

        
    }

  
}
