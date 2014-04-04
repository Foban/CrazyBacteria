package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 28.03.14.
 */
public abstract class Trait {
    private String name;

    public Trait(String name){
        this.name = name;
    }
    public abstract void action(Bacterium parent);
    public String getName(){
        return name;
    }
}
