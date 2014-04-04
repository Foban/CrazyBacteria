package DNA;

import java.util.Random;

/**
 * Created by Foban on 02/04/14.
 */
public class Ferm {
    private class Cell{
        public int foodForPhotosynthesisBacterium;
        public int foodForBreathingBacterium;
        public Bacterium bacterium = null;
    }

    private Cell field[][];

    public void addToFoodForPhotosynthesisBacterium(int y, int x, int value){
        field[y][x].foodForPhotosynthesisBacterium += value;
    }

    public void addToFoodForBreathingBacterium(int y, int x, int value){
        field[y][x].foodForBreathingBacterium += value;
    }

    public int getFoodForPhotosynthesisBacterium(int y, int x){
        return field[y][x].foodForPhotosynthesisBacterium;
    }

    public int getFoodForBreathingBacterium(int y, int x){
        return field[y][x].foodForBreathingBacterium;
    }

    public  int getYSize(){
        return field.length;
    }

    public int getXSize(){
        return field[0].length;
    }

    public Bacterium getBacterium(int y, int x){
        return field[y][x].bacterium;
    }

    public void placeBacterium(int y, int x, Bacterium bacterium){
        field[y][x].bacterium = bacterium;
    }

    public void killBacterium(int y, int x){
        Random rand = new Random();
        if(field[y][x].bacterium.getType() == 'F'){
            int food = field[y][x].bacterium.getAge()*field[y][x].bacterium.getNeedNumberOfFood();
            int sep = Math.abs(rand.nextInt())%10;
            field[y][x].foodForPhotosynthesisBacterium += food/10*sep;
            field[y][x].foodForBreathingBacterium += food/10*(10-sep);
        }
        field[y][x].bacterium = null;
    }

    public void moveBacterium(int y_old, int x_old, int y_new, int x_new){
        if(field[y_old][x_old].bacterium != null){
            field[y_new][x_new].bacterium = field[y_old][x_old].bacterium;
            field[y_old][x_old].bacterium = null;
        }
    }

    public Ferm(int y_size, int x_size,int foodMax, boolean generate, boolean bacteriumGenerate){
        Random rand = new Random();
        field = new Cell[y_size][x_size];
        for (int i = 0; i < y_size; ++i){
            for(int n  = 0; n < x_size; ++n){
                field[i][n] = new Cell();
                field[i][n].foodForBreathingBacterium = Math.abs(rand.nextInt())%foodMax;
                field[i][n].foodForPhotosynthesisBacterium = Math.abs(rand.nextInt())%foodMax;
            }
        }

        if(generate){
            int number = y_size*x_size/5;
            for(int i = 0; i < number; ++i)
            {
                field[Math.abs(rand.nextInt())%y_size][Math.abs(rand.nextInt())%x_size].bacterium = new Bacterium(bacteriumGenerate);
            }
        }
    }

    public void activate(){
        for (int y = 0; y < field.length; ++y){
            for(int x  = 0; x < field[0].length; ++x){
                if(field[y][x].bacterium != null)
                    field[y][x].bacterium.move(y, x, this);
            }
        }
    }
}
