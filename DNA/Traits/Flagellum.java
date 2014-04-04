package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class Flagellum extends Trait {
    public Flagellum() {
        super("Flagellum");
    }

    @Override
    public void action(Bacterium parent) {
        parent.setNumberOfMovesOnStep(parent.getNumberOfMovesOnStep() + 2);
        parent.setNeedNumberOfFood(parent.getNeedNumberOfFood() + 10);
    }
}
