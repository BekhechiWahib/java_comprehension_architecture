package hai913i.tp2.utils;

import java.io.File;

// Source - https://stackoverflow.com/q/23257369
// Posted by Colin Koenig
// Retrieved 2026-11-07, License - CC BY-SA 3.0

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import hai913i.tp2.models.CouplingGraph;
public class CSVGenerator {
    public static void generateCsvFile(String sFileName, List<CouplingGraph> couplingMap)
    { 
        File file = new File("C:\\Users\\DELL\\Desktop\\Master_2_Montpellier\\evolution_restructuration\\TP2\\java_comprehension_architecture\\starter\\src\\exports");
        try
        {
            FileWriter writer = new FileWriter(file.getAbsolutePath() + "\\" + sFileName+".csv");

            writer.append("Source");
            writer.append(',');
            writer.append("Target");
            writer.append(',');
            writer.append("Weight");
            writer.append('\n');
            for(CouplingGraph coupling : couplingMap) {
                writer.append(coupling.source);
                writer.append(',');
                writer.append(coupling.target != null ? coupling.target : "null");
                writer.append(',');
                writer.append(String.valueOf(coupling.weight));
                writer.append('\n');
            }
            
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        } 
    }

}


   
