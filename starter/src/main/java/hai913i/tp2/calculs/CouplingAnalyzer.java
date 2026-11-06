package hai913i.tp2.calculs;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hai913i.tp2.Facts;
import hai913i.tp2.Facts.CallArc;

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
    public static void analyzeCoupling(Facts facts) {
       
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

        System.out.println("Nombre d'arcs inter-classes (|E_inter|) : " + arcsInterClasse);
        System.out.println("Nombre de couples de classes uniques   : " + couplesUniques.size());
        System.out.println("Nombre d'arcs intra-classe             : " + arcsIntraClasse);
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
        int eInter=0;

       for(CallArc call : facts.calls()) {
            String caller = call.callerClass();
            String callee = call.calleeClass();

            if (!caller.equals(callee)) {
                // clé non ordonnée : A→B et B→A donnent la même clé
                String cle = (caller.compareTo(callee) < 0)
                        ? caller + " | " + callee
                        : callee + " | " + caller;

                R.put(cle, R.getOrDefault(cle, 0) + 1);
                eInter++;
            }
        }
              
        //On calcule le couplage entre les classes et on affiche les résultats
        computeCoupling(R, eInter);
        return R;
    }
    
    /**
    * Calcule le couplage entre les classes. C(A,B) = r(A,B) / |E_inter| et affiche les résultats.<br>
    * 
    * @param R la map contenant le nombre d'arcs entre chaque couple de classes distinctes
    * @param eInter le nombre total d'arcs inter-classes (|E_inter|)
    *    
    */
    public static void computeCoupling(Map<String, Integer> R, int eInter) {
    List<Map.Entry<String, Integer>> entries = new ArrayList<>(R.entrySet());
    entries.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));
    double cTotal = 0.0;
    for (Map.Entry<String, Integer> entry : entries) {
        String cle = entry.getKey();
        int r = entry.getValue();
        double c = (double) r / eInter;
        cTotal += c;
        System.out.printf("Couple de classes : %s \t| r(A,B) = %d \t| C(A,B) = %.4f%n",cle, r, c);
    }

    System.out.printf("Couplage total des %d classes : %.4f%n", entries.size(), cTotal);

    }



}
