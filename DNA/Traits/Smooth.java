package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class Smooth extends Trait {
    public Smooth() {
        super("Smooth");
    }
    @Override
    public void action(Bacterium parent) {
        parent.setNumberOfEatingFoodOnStep(parent.getNumberOfEatingFoodOnStep() - 10);
        parent.setNumberOfMovesOnStep(parent.getNumberOfMovesOnStep() + 1);
    }
}
