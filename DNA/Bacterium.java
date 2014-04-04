package DNA;

/**
 * Created by Anton on 28.03.14.
 */
import java.util.Random;

public class Bacterium {
    private Random rand = new Random();

    public class GenePair{
        public Gene firstParentGene;
        public Gene secondParentGene;
        public GenePair(){
            firstParentGene = new Gene();
            secondParentGene = new Gene();
        }
    }

    private int age = 0;
    private int lengthOfLIfe = 100;
    private int breedingPeriod = 10;
    private int nextBreeding;
    private int numberOfMovesOnStep = 0;
    private int needNumberOfFood = 10;
    private int numberOfEatingFoodOnStep = 30;
    private char type = 'N';

    private GenePair[] dna;

    public void setLengthOfLIfe(int lengthOfLIfe){
        this.lengthOfLIfe = lengthOfLIfe;
    }
    public void setBreedingPeriod(int breedingPeriod){
        this.breedingPeriod = breedingPeriod;
    }
    public void setNumberOfMovesOnStep(int numberOfMovesOnStep){
        this.numberOfMovesOnStep = numberOfMovesOnStep;
    }
    public void setNeedNumberOfFood(int needNumberOfFood){
        this.needNumberOfFood = needNumberOfFood;
    }
    public void setNumberOfEatingFoodOnStep(int numberOfEatingFoodOnStep){
        this.numberOfEatingFoodOnStep = numberOfEatingFoodOnStep;
    }
    public void setType(char type){
        this.type = type;
    }

    public int getLengthOfLIfe(){
        return lengthOfLIfe;
    }
    public int getBreedingPeriod( ){
        return breedingPeriod;
    }
    public int getNumberOfMovesOnStep( ){
        return numberOfMovesOnStep;
    }
    public int getNeedNumberOfFood( ){
        return needNumberOfFood;
    }
    public int getNumberOfEatingFoodOnStep( ){
        return numberOfEatingFoodOnStep;
    }
    public char getType(){
        return type;
    }
    public int getAge(){
        return age;
    }
    public GenePair[] getDNA(){
        return dna;
    }

    public Bacterium(GenePair[] firstParentDNA, GenePair[] secondParentDNA){
        if(firstParentDNA.length == secondParentDNA.length)
        {
            dna = new GenePair[firstParentDNA.length];
            for(int i = 0; i < dna.length; ++i)
            {
                dna[i] = new GenePair();
                if(rand.nextBoolean())
                    dna[i].firstParentGene = firstParentDNA[i].firstParentGene;
                else
                    dna[i].firstParentGene = firstParentDNA[i].secondParentGene;

                if(rand.nextBoolean())
                    dna[i].secondParentGene = firstParentDNA[i].firstParentGene;
                else
                    dna[i].secondParentGene = firstParentDNA[i].secondParentGene;
            }
            activateDNA();
            nextBreeding = breedingPeriod;
        }
    }

    public Bacterium(){
        dna = new GenePair[10];
        for(int i = 0; i < dna.length; ++i)
        {
            dna[i] = new GenePair();
            dna[i].firstParentGene = Gene.generateGene();
            dna[i].secondParentGene = Gene.generateGene();
        }
        activateDNA();
        nextBreeding = breedingPeriod;
    }

    public Bacterium(boolean flag){
        this();
        if(type == 'N' && flag){
            type = rand.nextBoolean()?'P':'B';
        }
    }

    private void activateDNA(){
        for(int i = 0; i < dna.length; ++i)
        {
            if(dna[i].firstParentGene.type == dna[i].secondParentGene.type)
            {
                if(!dna[i].firstParentGene.recessivity)
                    activateGene(dna[i].firstParentGene);
                if(!dna[i].secondParentGene.recessivity)
                    activateGene(dna[i].secondParentGene);
                if(dna[i].secondParentGene.recessivity && dna[i].firstParentGene.recessivity)
                    activateGene(rand.nextBoolean()?dna[i].firstParentGene:dna[i].secondParentGene);
            }
        }
        if(numberOfMovesOnStep < 0)
            numberOfMovesOnStep = 0;
    }

    private void activateGene(Gene gene){
        for(int i = 0; i < gene.setOfTrait.length; ++i)
        {
            gene.setOfTrait[i].action(this);
        }
    }

    private int foodCounter(int y, int x, Ferm parent){
        int work = 0;
        switch (type){
            case 'P':
                work =parent.getFoodForPhotosynthesisBacterium(y, x);
                break;
            case 'B':
                work =parent.getFoodForBreathingBacterium(y, x);
                break;
            case 'F':
                work =parent.getFoodForPhotosynthesisBacterium(y, x)+parent.getFoodForBreathingBacterium(y, x);
                break;
        }
        return work;
    }

    public void move(int y_current, int x_current, Ferm parent){
        if(type != 'N' && lengthOfLIfe > 0){
            age++;
            lengthOfLIfe--;
            nextBreeding--;

            // движение
            if(numberOfMovesOnStep > 0){
                boolean maxFood = false;
                int x_max = 0, y_max = 0;
                int work;
                int maxValue= 0;
                for(int i = 0; i < numberOfMovesOnStep && !maxFood; ++i){
                    for(int m = y_current>0?-1:0; m < (y_current<parent.getYSize()-1?2:1); ++m)
                        for(int k = x_current>0?-1:0; k < (x_current<parent.getXSize()-1?2:1); ++k){
                            if(maxValue < (work = foodCounter(y_current+m, x_current+k, parent)) && parent.getBacterium(y_current + m, x_current + k) == null) {
                                maxValue = work;
                                x_max = x_current+k;
                                y_max = y_current+m;
                            }
                        }

                    if(maxValue > foodCounter(0,0,parent)){
                        parent.moveBacterium(y_current,x_current,y_max,x_max);
                        y_current = y_max;
                        x_current = x_max;
                    }
                    else
                        maxFood = true;
                }
            }

            // питание
            if(foodCounter(y_current,x_current,parent)>=needNumberOfFood){
                switch (type){
                    case 'P':
                        parent.addToFoodForPhotosynthesisBacterium(y_current, x_current, -needNumberOfFood);
                        parent.addToFoodForBreathingBacterium(y_current, x_current, needNumberOfFood);
                        break;
                    case 'B':
                        parent.addToFoodForBreathingBacterium(y_current, x_current, -needNumberOfFood);
                        parent.addToFoodForPhotosynthesisBacterium(y_current, x_current, needNumberOfFood);
                        break;
                    case 'F':
                    {
                        int food = parent.getFoodForBreathingBacterium(y_current, x_current);
                        parent.addToFoodForBreathingBacterium(y_current, x_current, food < needNumberOfFood/2? -food: -needNumberOfFood/2);
                        food = parent.getFoodForPhotosynthesisBacterium(y_current, x_current);
                        parent.addToFoodForPhotosynthesisBacterium(y_current, x_current, food < needNumberOfFood/2? -food: -needNumberOfFood/2);
                    }
                        break;
                }
            }
            else
                parent.killBacterium(y_current, x_current);

            // размножение
            int x_child = 0, y_child = 0;
            boolean bornChild = false;
            Bacterium tmp = null;

            for(int m = y_current>0?-1:0; m < (y_current<parent.getYSize()-1?2:1) && !bornChild; m+=2)
                for(int k = x_current>0?-1:0; k < (x_current<parent.getXSize()-1?2:1) && !bornChild; k+=2){
                    if((tmp = parent.getBacterium(y_current+m, x_current+k)) != null && tmp.nextBreeding < 0){
                        if(parent.getBacterium(y_current+m, x_current) == null ){
                            x_child = x_current;
                            y_child = y_current + m;
                            bornChild = true;
                        }
                        else if(parent.getBacterium(y_current, x_current + k) == null ){
                            x_child = x_current + k;
                            y_child = y_current;
                            bornChild = true;
                        }
                    }
                }

            if(bornChild){
                parent.placeBacterium(y_child, x_child, new Bacterium());
                nextBreeding = breedingPeriod;
                tmp.nextBreeding = tmp.breedingPeriod;
            }

        }
        else parent.killBacterium(y_current, x_current);
    }
}
