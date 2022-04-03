package src;

import static java.util.Objects.requireNonNull;

abstract class Player { 
    protected String name;
    public static enum COLOR{
        RED,
        YELLOW,
        GREEN,
        CYAN,
        PURPLE
    }

    private COLOR playerColor;

    public Player(String name){
        this.name = requireNonNull(name);
    } 

    public String getName(){ return this.name;}
    protected int getNumberColor(){ return COLOR.valueOf(this.playerColor.toString()).ordinal(); }

    public COLOR getTokenColor(){ return this.playerColor;}

    public void setANSIColor(int valuesANSIColor){ 
        this.playerColor = COLOR.values()[valuesANSIColor];
    }

    public void setColor(COLOR C){
        this.playerColor = C;
    }
    
    public String getANSIStringColor(){
        if(this.playerColor.equals(COLOR.RED))   { return Game.ANSI_RED;    }
        if(this.playerColor.equals(COLOR.YELLOW)){ return Game.ANSI_YELLOW; }
        if(this.playerColor.equals(COLOR.CYAN))  { return Game.ANSI_CYAN;   }
        if(this.playerColor.equals(COLOR.PURPLE)){ return Game.ANSI_PURPLE; }
        return Game.ANSI_GREEN;
    }

    public void chooseTokenColor(Player J){}
    public void placeTokenOnGrid(Grid grid){}
}

class Human extends Player{
    public Human(String name){
        super(name);
    }
    
    /* Choisir la couleur du joueur (et donc du jeton) en fonction de la couleur pris par l'adversaire */
    public void chooseTokenColor(Player J){
        boolean correctInput = false;
        int numberColorChoose = 0;
        int numberColorPlayer;
        if( this != J ){
            numberColorPlayer = J.getNumberColor() + 1;
        }else{ 
            numberColorPlayer = -1; 
        }

        while(!correctInput){
            if(numberColorPlayer != 1){
                System.out.print(Game.ANSI_RED    +  "> RED"     + Game.ANSI_WHITE + ": 1" + "\n");
            }
            if(numberColorPlayer != 2){
                System.out.print(Game.ANSI_YELLOW +  "> YELLOW"  + Game.ANSI_WHITE + ": 2" + "\n");
            }
            if(numberColorPlayer != 3){
                System.out.print(Game.ANSI_GREEN +   "> GREEN"   + Game.ANSI_WHITE + ": 3" + "\n");
            }
            if(numberColorPlayer != 4){
                System.out.print(Game.ANSI_CYAN   +  "> CYAN"    + Game.ANSI_WHITE + ": 4" + "\n");
            }
            if(numberColorPlayer != 5){
                System.out.print(Game.ANSI_PURPLE +  "> PURPLE"  + Game.ANSI_WHITE + ": 5" + "\n");
            }

            System.out.println("\n" + this.name +  " - Choose your color :");
            
            try{
                numberColorChoose = Integer.parseInt(System.console().readLine());
                System.out.println(numberColorChoose);
                if(numberColorChoose >= 1 && numberColorChoose <= 5){ 
                    correctInput = true; 
                }else{  
                    System.out.println("Enter an correct value (between 1 and 5)"); 
                    continue;
                }
            }catch (Exception e){
                System.out.println("\nWARNING : Enter an existing value.");
                continue;
            }
        }   this.setANSIColor(numberColorChoose - 1); 
    }

    /* Décider du coup à jouer et placer le jeton dans la grille */ 
    /* en fonction de la colonne choisi par le joueur */
    public void placeTokenOnGrid(Grid gameGrid){
        int column;
        boolean correctInput = false;        
            while(!correctInput){
                System.out.print("\n" + this.getANSIStringColor() + this.name + Game.ANSI_WHITE + " - play (choose your column): ");
                try{
                    try{
                        column = Integer.parseInt(System.console().readLine());
                    }catch (Exception e){
                        System.out.println("\nWARNING : Enter an existing value.");
                        continue;
                    }
                    gameGrid.placeToken(this, column, false);
                    correctInput = true;
                }catch (IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                    continue;
                }
            } 
    }
}
