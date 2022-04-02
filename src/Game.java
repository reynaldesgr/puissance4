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

    public static Player J1;
    public static Player J2;
    public static Player current_player;

    public static Player nextPlayer(){
        return current_player == J1 ? J1 : J2;
    }

    /* Initialiser les joueurs J1 & J2 */
    public static void initPlayer(){
        System.out.println(ANSI_PURPLE + " === Bienvenue ! === " + ANSI_WHITE + "\n");
        try (Scanner input = new Scanner(System.in)) {
            boolean isThereIA = false;
            
            System.out.println(ANSI_CYAN + "Choississez votre mode de jeu." + ANSI_WHITE);
            System.out.println("Joueur vs. Joueur : 1 | Joueur vs. IA : 2");
            
            isThereIA = System.console().readLine().equals("2") ? true : false;

            if(!isThereIA){
                System.out.println(" J1 - Enter your name: ");
                J1 = new Human(input.nextLine());
                System.out.println(" J2 - Entrez your name: ");
                J2 = new Human(input.nextLine()); 
            }else{
                System.out.println(" J1 - Enter your name: ");
                J1 = new Human(input.nextLine());
                J2 = new IA(); 
            }
            playGame(J1, J2);
        }
    }

    /* Joue le jeu jusqu'à une victoire ou une défaite (au tour par tour: J1 puis J2) */
    public static void playGame(Player J1, Player J2){
        boolean isGameFinished = false;
        boolean saveload = false;
        String resp_load = "";
        current_player = J1;
        Grid gameGrid = new Grid();
        File f = new File(SAVE_FILE);
        System.console().flush();
        if(f.exists()){
            if(f.length() != 0){
                while(!resp_load.equals("O") && !resp_load.equals("N")){
                    System.out.println("Une sauvegarde existe. Voulez-vous la charger ? (O: oui / N: non)");
                    resp_load = System.console().readLine();
                }
                saveload = resp_load.equals("O") ? true : false;
            }
            if(saveload){
                System.out.println(" --- Chargement de la sauvegarde ... ---");
                gameGrid.loadGrid(SAVE_FILE, J1, J2);
            }else{
                J1.chooseTokenColor(J1);
                J2.chooseTokenColor(J1);    
            }
        }
        
        while(!isGameFinished){
            gameGrid.printGrid();
            if(current_player == J1){
                current_player = J2;
                try{
                    J1.placeTokenOnGrid(gameGrid);
                }catch(IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                    current_player = J1;
                    continue;
                }
            }else{ 
                current_player = J1;
                try{
                    J2.placeTokenOnGrid(gameGrid);
                }catch(IllegalArgumentException ex){
                    System.out.println(ex.getMessage());
                    current_player = J2;
                    continue;
                }
            } 
            gameGrid.saveGrid(SAVE_FILE, J1, J2);
        }  
    }
    public static void main(String args[]){
        System.out.println(" === PUISSANCE 4 === ");
        initPlayer();
    }
}
