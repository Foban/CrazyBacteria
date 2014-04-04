package DNA.Traits;

import DNA.Bacterium;

/**
 * Created by Anton on 30.03.14.
 */
public class LongLife extends Trait {
    public LongLife() {
        super("Long life");
    }

    @Override
    public void action(Bacterium parent) {
        parent.setLengthOfLIfe(parent.getLengthOfLIfe()*2);
    }
}
