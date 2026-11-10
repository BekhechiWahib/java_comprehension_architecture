package hai913i.tp2.models;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public int id;                 // L'ID (0 à 14 pour les feuilles, 15+ pour les fusions)
    public List<String> classes;   // Les classes contenues dans ce nœud (pour l'affichage/export)
    public double similarity;      // La similarité AU MOMENT de la fusion (0.0 pour une feuille)
    
    // Les deux fils (null si c'est une feuille)
    public Node left;  
    public Node right; 
 // Constructeur pour une FEUILLE (une classe seule)
    public Node(int id, String className) {
        this.id = id;
        this.classes = new ArrayList<>();
        this.classes.add(className);
        this.similarity = 0.0;
        this.left = null;
        this.right = null;
    }

    // Constructeur pour un NŒUD INTERNE (une fusion)
    public Node(int id, Node left, Node right, double similarity) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.similarity = similarity;
        
        // Les classes de ce nœud sont l'union des classes de ses deux fils
        this.classes = new ArrayList<>();
        this.classes.addAll(left.classes);
        this.classes.addAll(right.classes);
    }
}