package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class Sterility extends Trait{
    public Sterility() {
        super("Sterility");
    }

    @Override
    public void action(Bacterium parent) {
        parent.setBreedingPeriod(parent.getBreedingPeriod()/2);
    }
}
