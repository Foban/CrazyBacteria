package DNA;

import DNA.Traits.*;

import java.util.Random;

/**
 * Created by Anton on 28.03.14.
 */
public class Gene {
    public char type;
    public Trait[] setOfTrait;
    public boolean recessivity;
    public Gene(char type, boolean recessivity, Trait[] setOfTrait){
        this.type = type;
        this.recessivity = recessivity;
        this.setOfTrait = setOfTrait;
    }
    public Gene(){}
    public static Gene generateGene(){

        Trait setOfTrait[] = new Trait[3];
        Random rand = new Random();
        for(int i = 0; i < setOfTrait.length; ++i)
            switch (Math.abs(rand.nextInt()%10))
            {
                case 0:
                    setOfTrait[i] = new Breathing();
                    break;
                case 1:
                    setOfTrait[i] = new Fertility();
                    break;
                case 2:
                    setOfTrait[i] = new Fimbria();
                    break;
                case 3:
                    setOfTrait[i] = new Flagellum();
                    break;
                case 4:
                    setOfTrait[i] = new LongLife();
                    break;
                case 5:
                    setOfTrait[i] = new Photosynthesis();
                    break;
                case 6:
                    setOfTrait[i] = new Ribbed();
                    break;
                case 7:
                    setOfTrait[i] = new ShortLife();
                    break;
                case 8:
                    setOfTrait[i] = new Smooth();
                    break;
                case 9:
                    setOfTrait[i] = new Sterility();
                    break;
            }
        Gene m = new Gene((char)('A'+ Math.abs(rand.nextInt()%5)),rand.nextBoolean(), setOfTrait);
        return m;
    }
}
