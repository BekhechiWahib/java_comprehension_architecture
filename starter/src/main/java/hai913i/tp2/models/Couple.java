package hai913i.tp2.models;

import java.util.Objects;

public class Couple {
    public final String source;
    public final String target;
    public Couple(String src, String target) {
         if (src.compareTo(target) <= 0) {
            this.source = src;
            this.target = target;
        } else {
            this.source = target;
            this.target = src;
        }
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Couple)) return false;
        Couple c = (Couple) o;
        return source.equals(c.source) && target.equals(c.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
}
