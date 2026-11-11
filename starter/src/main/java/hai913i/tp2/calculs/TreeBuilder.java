package hai913i.tp2.calculs;

import java.util.List;
import java.util.Map;

import hai913i.tp2.models.Couple;
import hai913i.tp2.models.Module;
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

    public static List<Module> cutModule(Node n, double cp, List<Module> modules, Map<Couple,Double> coupling) {
        if (n.left == null && n.right == null) {
            Module module = new Module();
            module.id = n.id;
            module.classeModule = n.classes;
            module.size = n.classes.size(); 
            module.internalCoupling = 0.0; 
            modules.add(module); 
            return modules;
        }
        double couplageInterne = calculerCouplageInterne(n.classes,coupling);
        if (couplageInterne > cp) {
            Module module = new Module();
            module.id = n.id;
            module.classeModule = n.classes;
            module.size = n.classes.size();
            module.internalCoupling = couplageInterne;
            modules.add(module); 
        } else {
            cutModule(n.left, cp, modules,coupling);  
            cutModule(n.right, cp, modules,coupling);
        }
        return modules;
    }

    public static double calculerCouplageInterne(List<String> classes, Map<Couple, Double> coupling) {
        int n = classes.size();
        if (n < 2) return 0.0; 
        double somme = 0.0;
        int nombreDePaires = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                String a = classes.get(i);
                String b = classes.get(j);
                double c = coupling.getOrDefault(new Couple(a, b), 0.0);
                somme += c;
                nombreDePaires++;
            }
        }
        double resultat = somme / nombreDePaires;
        return resultat; 
    }


  
}
