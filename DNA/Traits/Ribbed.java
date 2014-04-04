package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class Ribbed extends Trait {
    public Ribbed() {
        super("Ribbed");
    }

    @Override
    public void action(Bacterium parent) {
        parent.setNumberOfEatingFoodOnStep(parent.getNumberOfEatingFoodOnStep() + 10);
        parent.setNumberOfMovesOnStep(parent.getNumberOfMovesOnStep() - 1);
    }
}
