package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class ShortLife extends Trait {
    public ShortLife() {
        super("Short life");
    }

    @Override
    public void action(Bacterium parent) {
        parent.setLengthOfLIfe(parent.getLengthOfLIfe()/2);
    }
}
