package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class Breathing extends Trait {
    public Breathing() {
        super("Breathing");
    }

    @Override
    public void action(Bacterium parent) {
        if(parent.getType() == 'N')
            parent.setType('B');
        else if(parent.getType() == 'P')
            parent.setType('F');
    }
}
