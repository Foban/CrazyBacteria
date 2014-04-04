package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class Fimbria extends Trait {
    public Fimbria() {
        super("Fimbria");
    }

    @Override
    public void action(Bacterium parent) {
        parent.setNumberOfMovesOnStep(parent.getNumberOfMovesOnStep() + 1);
    }
}
