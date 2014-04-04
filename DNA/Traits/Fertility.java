package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class Fertility extends Trait {
    public Fertility() {
        super("Fertility");
    }

    @Override
    public void action(Bacterium parent) {
        parent.setBreedingPeriod(parent.getBreedingPeriod()*2);
    }
}
