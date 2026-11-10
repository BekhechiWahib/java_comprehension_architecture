package hai913i.tp2.models;

import java.util.List;

public class Groupe {
    public int id;
    public String name;
    public int size;
    public int addOrder;
    public double similarity;
    public List<String> classes;
    public Node node; //le node dont le groupe fait partie
}
