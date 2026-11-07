package hai913i.tp2.calculs;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hai913i.tp2.Facts;
import hai913i.tp2.Facts.CallArc;
import hai913i.tp2.Facts.ClassInfo;
import hai913i.tp2.models.Couple;

public class CouplingAnalyzer {

    public static Set<String> couplesUniques = new HashSet<>();
    public static int arcsInterClasse = 0; // cette variable compte les arcs inter-classes representés par |E_inter|
    public static int arcsIntraClasse = 0;

    /**
    * Analyse le couplage entre les classes.
    * 
    * <b>Partie A1</b> : compte le nombre d'arcs inter-classes et intra-classe,
    *  ainsi que le nombre de couples de classes uniques.
    *
    * @param facts les faits à analyser 
    */
    public static int analyzeCoupling(Facts facts) {
       
        for (CallArc call : facts.calls()) {
            String caller = call.callerClass();
            String callee = call.calleeClass();

            if (caller.equals(callee)) {
                arcsIntraClasse++;
            } else {
                arcsInterClasse++;
                // clé non ordonnée : A→B et B→A donnent la même clé
                String cle = (caller.compareTo(callee) < 0)
                        ? caller + "|" + callee
                        : callee + "|" + caller;
                couplesUniques.add(cle);
            }
        }

        System.out.println("\nNombre d'arcs inter-classes (|E_inter|) : " + arcsInterClasse);
        System.out.println("Nombre de couples de classes uniques   : " + couplesUniques.size());
        System.out.println("Nombre d'arcs intra-classe             : " + arcsIntraClasse +"\n");
    return arcsInterClasse;
    }


    /**
     * Calcule le nombre d'arcs inter-classes pour chaque couple de classes distinctes et retourne une map.<br>
     * <b>r(A,B)</b> est le nombre d'arcs inter-classes entre les classes A et B.<br>
     * <b>|E_inter|</b> est le nombre total d'arcs inter-classes.<br>
     * @implNote la méthode <b>computeCoupling()</b> est appelée pour calculer le couplage entre les classes et afficher les résultats.<br>
     * @param facts les faits à analyser
     */
   

    public static Map<String, Integer> computeR(Facts facts) {
        Map<String, Integer> R = new HashMap<>();

        for (CallArc call : facts.calls()) {
            String caller = call.callerClass();
            String callee = call.calleeClass();

            if (caller.equals(callee)) continue;   // écarter intra-classe

            String cle = (caller.compareTo(callee) < 0)
                    ? caller + "|" + callee
                    : callee + "|" + caller;

            R.merge(cle, 1, Integer::sum);
        }
        return R;
    }
    
    /**
    * Calcule le couplage entre les classes. C(A,B) = r(A,B) / |E_inter| et affiche les résultats.<br>
    * 
    * @param R la map contenant le nombre d'arcs entre chaque couple de classes distinctes
    * @param eInter le nombre total d'arcs inter-classes (|E_inter|)
    *    
    */
    public static Map<Couple, Double> computeCoupling(Map<String, Integer> R, int eInter) {
        Map<Couple, Double> couplingValues = new HashMap<>();
        
        for (Map.Entry<String, Integer> entry : R.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            Couple couple = new Couple(parts[0], parts[1]);
            double c = (double) entry.getValue() / eInter;
            couplingValues.put(couple, c);
        }
        return couplingValues;
    }

    public static void printCouplingSorted(Map<Couple, Double> coupling) {
        List<Map.Entry<Couple, Double>> list = new ArrayList<>(coupling.entrySet());
        list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));  // décroissant
        double cTotal = 0.0;
        System.out.println("\n=== Couplage entre les classes distinctes ===\n");
        for (Map.Entry<Couple, Double> e : list) {
            cTotal += e.getValue();
            System.out.printf("Couplage entre %s --> %s : %.4f%n",
                    e.getKey().source, e.getKey().target, e.getValue());
        }
        System.out.printf("Couplage total : %.4f%n", cTotal);
    }

    public static List<String> computeIsolatedClasses(Map<Couple, Double> coupling,Facts facts) {

        List<String> isolatedClasses = new ArrayList<>();
        for (ClassInfo fact : facts.classes()) {
            String className = fact.name();
            if(!coupling.keySet().stream().anyMatch(c -> c.source.equals(className) || c.target.equals(className))) {
                isolatedClasses.add(className);

            }
        }
        return isolatedClasses;
    }
    public static void printCouplingGraph(Map<Couple, Double> coupling, Facts facts) {

        System.out.println("=== Graphe de couplage pondéré ===");
        for (CallArc call : facts.calls()) {
            String caller = call.callerClass();
            String callee = call.calleeClass();

            if (caller.equals(callee)) {
                arcsIntraClasse++;
            } else {
                arcsInterClasse++;
                // clé non ordonnée : A→B et B→A donnent la même clé
                String cle = (caller.compareTo(callee) < 0)
                        ? caller + "|" + callee
                        : callee + "|" + caller;
                couplesUniques.add(cle);
            }
        }
    }

    public static void printTop5(Map<Couple, Double> coupling) {
        List<Map.Entry<Couple, Double>> list = new ArrayList<>(coupling.entrySet());
        list.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        System.out.println("\n=== TOP 5 ===\n");
        int n = Math.min(5, list.size());
        for (int i = 0; i < n; i++) {
            Map.Entry<Couple, Double> e = list.get(i);
            System.out.printf("%d. %s --> %s \t| C = %.4f%n", i + 1, e.getKey().source , e.getKey().target, e.getValue());
        }
    }

}
