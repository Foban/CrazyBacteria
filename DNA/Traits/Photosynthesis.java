package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class Photosynthesis extends Trait{
    public Photosynthesis() {
        super("Photosynthesis");
    }

    @Override
    public void action(Bacterium parent) {
        if(parent.getType() == 'N')
            parent.setType('P');
        else if(parent.getType() == 'B')
            parent.setType('F');
    }
}
