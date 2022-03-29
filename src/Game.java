package src;

import java.io.File;

import java.util.Scanner;


public class Game {
    /** Some colors **/
    public static final String ANSI_BLACK   = "\u001B[30m";
    public static final String ANSI_CYAN    = "\u001B[36m";
    public static final String ANSI_RED     = "\u001B[31m";
    public static final String ANSI_GREEN   = "\u001B[32m";
    public static final String ANSI_YELLOW  = "\u001B[33m";
    public static final String ANSI_BLUE    = "\u001B[34m";
    public static final String ANSI_PURPLE  = "\u001B[35m";
    public static final String ANSI_WHITE   = "\u001B[37m";

    /** Default path to save the current game grid **/
    public static final String SAVE_FILE = "./save/savefile.txt";


    public static void initPlayer(){
        System.out.println(ANSI_PURPLE + " === Bienvenue ! === " + ANSI_WHITE + "\n");
        try (Scanner input = new Scanner(System.in)) {
            Player J1;
            Player J2;

            System.out.println(" J1 - Enter your name: ");
            J1 = new Player(input.nextLine());

            System.out.println(" J2 - Entrez your name: ");
            J2 = new Player(input.nextLine());

            playGame(J1, J2);
        }
    }

    public static void placeTokenOnGrid(Player j, Grid gameGrid){
        int column;
        boolean correctInput = false;        
        String name = j.getName();
            while(!correctInput){
                System.out.print("\n" + j.getANSIStringColor() + name + ANSI_WHITE + " - play (choose your column): ");
                try{
                    try{
                        column = Integer.parseInt(System.console().readLine());
                    }catch (Exception e){
                        System.out.println("\nWARNING : Enter an existing value.");
                        continue;
                    }
                    gameGrid.placeToken(j, column);
                    correctInput = true;
                }catch (IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                    continue;
                }
            } 
    }

    public static void playGame(Player J1, Player J2){
        boolean isGameFinished = false;
        boolean isTurnJ1 = true;
        boolean saveload = true;

        Grid gameGrid = new Grid();
        File f = new File(SAVE_FILE);
        if(f.exists()){
            if(saveload && f.length() != 0){
                gameGrid.loadGrid(SAVE_FILE, J1, J2);
            }else{
                Grid.FULL_GRID = false;
                J1.chooseTokenColor();
                J2.chooseTokenColor(J1);    
            }
        }
        
        while(!isGameFinished){
            gameGrid.printGrid();
            if(isTurnJ1){
                isTurnJ1 = !isTurnJ1;
                placeTokenOnGrid(J1, gameGrid);
            }else{ 
                isTurnJ1 = !isTurnJ1;
                placeTokenOnGrid(J2, gameGrid);
            } 
            gameGrid.saveGrid(SAVE_FILE, J1, J2);
        }  
    }

    public static void main(String args[]){
        System.out.println(" === PUISSANCE 4 === ");
        initPlayer();
    }
}
