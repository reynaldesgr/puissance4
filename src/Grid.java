package src;

public class Grid {

    public static final int MAX_COLUMN = 7;
    public static final int MAX_LINE   = 6;

    public static int last_token_pos_l = 0;
    public static int last_token_pos_c = 0;
    public static int last_line_play =   6;

    public static final Token EMPTY = null;
    private FileGridSaver fSaver = new FileGridSaver();

    private Token[][] grid = new Token[MAX_LINE][MAX_COLUMN];
    public enum DIRECTION{ LEFT, RIGHT, DOWN, UP, DOWN_RIGHT, DOWN_LEFT, UP_RIGHT, UP_LEFT}

    /* Retourne la grille du jeu */
    public Token[][] getGrid(){ return this.grid.clone(); } 

    /* Charge la grille du fichier de sauvegarde. Initialise la grille avec cette sauvegarde. */
    public void loadGrid(String path, Player J1, Player J2){ this.setGrid(fSaver.load(path, J1, J2)); }

    /* Sauvegarde la grille du jeu en cours */
    public void saveGrid(String path, Player J1, Player J2){ fSaver.save(path, this, J1, J2);}

    /* Setter privé qui sert au fichier de sauvegarde d'actualiser la grille du jeu en la grille de la sauvegarde.*/
    private void setGrid(Grid grid){ this.grid = grid.getGrid(); }

    /** Méthode permettant de construire la grille une fois le jeton placé par un joueur **/
    /* Cette méthode permet de:
    /* -> Placer correctement le jeton joué par le joueur dans la grille */
    /*    (ligne la + basse disponible avec sa colonne correspondante) */
    /* -> Initialiser les voisins du jeton placé en fonction de sa position dans la grille */
    public boolean placeToken(Player p, int column) throws IllegalArgumentException{
        if(column > grid[0].length) throw new IllegalArgumentException("Invalid column value.");

        int l = this.grid.length - 1; 
        int c = column - 1;
        boolean isLineFull = false;

        while(!isLineFull && (grid[l][c]) != EMPTY){
            if(l == 0){ 
                isLineFull = true; 
            }   --l;
        }
        
        Token t = new Token(p.getTokenColor(), p.getANSIStringColor());

        /* Référencement des voisins du jeton placé si la ligne où le jeton est placé n'est pas pleine. */
        if(!isLineFull){ 
            if(last_line_play > l){
                last_line_play = l;
            }

            this.grid[l][c] = t;
            last_token_pos_c = c; 
            last_token_pos_l = l;
            if(c != 0)                            referDirectionNeighbor(l, c, 0, -1, DIRECTION.LEFT,  DIRECTION.RIGHT, t); 
            if(c != MAX_COLUMN - 1)               referDirectionNeighbor(l, c, 0, 1,  DIRECTION.RIGHT, DIRECTION.LEFT,  t);
            if(l != 0)                            referDirectionNeighbor(l, c, -1, 0, DIRECTION.UP,    DIRECTION.DOWN,  t);
            if(l != MAX_LINE - 1)                 referDirectionNeighbor(l, c, 1, 0,  DIRECTION.DOWN,  DIRECTION.UP,    t);
            
            if(l != 0 && c != MAX_COLUMN -1)              referDirectionNeighbor(l, c, -1, 1, DIRECTION.UP_RIGHT,   DIRECTION.DOWN_LEFT,  t);
            if(l != 0 && c != 0)                          referDirectionNeighbor(l, c, -1, -1,DIRECTION.UP_LEFT,    DIRECTION.DOWN_RIGHT, t);
            if(l != MAX_LINE -1 && c != 0)                referDirectionNeighbor(l, c, 1, -1, DIRECTION.DOWN_LEFT,  DIRECTION.UP_RIGHT,   t);
            if(l != MAX_LINE - 1 && c != MAX_COLUMN - 1)  referDirectionNeighbor(l, c, 1, 1, DIRECTION.DOWN_RIGHT, DIRECTION.UP_LEFT,    t);

            /* Si le jeton joué satisfait un alignement de + de 4 jetons, la partie se termine. */
            if( t.check() ){
                System.out.println( Game.ANSI_YELLOW + "!! " + p.getName() + " - WIN " + " !!" + Game.ANSI_WHITE);
                this.printGrid();
                fSaver.unload(Game.SAVE_FILE);
                System.exit(0);
            }   return true;
        }else if (this.isLineFullGrid()){
            System.out.println( Game.ANSI_BLUE + "La grille est pleine. Aucun gagnant." + Game.ANSI_WHITE);
            this.printGrid();
            fSaver.unload(Game.SAVE_FILE);
            System.exit(0);
        }else{
            System.out.println( Game.ANSI_BLUE + "Ligne pleine." + Game.ANSI_WHITE);
        }
        return false;
    }

    /** Méthode de référencement des voisins **/
    /* Les jetons (cellules) de notre grille sont en 8-connexité (à cause des diagonales) */
    /* Cette méthode permet d'ajouter un voisin au jeton joué ET d'indiquer la direction de ce voisin au jeton joué et vice-versa. 
    /* Le jeton voisin au jeton joué -- s'il existe -- doit lui aussi avoir une référence et connaître la direction de son voisin 
    /* (qui est le jeton joué) */
    public void referDirectionNeighbor(int l,  int c, int offset_line, int offset_column, DIRECTION D, DIRECTION D_neighbor, Token t){
        if(this.grid[l + offset_line][c + offset_column] != EMPTY){ 
            t.setNeighbors(D, this.grid[l + offset_line][c + offset_column]); 
            this.grid[l + offset_line][c + offset_column].setNeighbors(D_neighbor, this.grid[l][c]);
        }
    }

    public boolean isLineFullGrid(){
        for(int c = 0; c < MAX_COLUMN; c++){
            if(this.grid[0][c] != null){
                return false;
            }
        } return true;
    }

    /* Affichage de la grille en jeu */
    public void printGrid(){
        System.out.println(this.convertGridToString());
    }

    /* Trouve et renvoie une chaîne de caractère des voisins de chaque jeton présent dans la grille */
    public String findNeighborsGrid(){
        String str="";
        for(int l = 0; l < this.grid.length; l++){
            for(int c = 0; c < this.grid[0].length; c++){
                if(this.grid[l][c] != EMPTY){ 
                    str+=(l + " " + c + " " + this.grid[l][c].getColor() + " ");
                    str+=  this.grid[l][c].toStringNeighbors() + "\n";
                }
            }
        }   return str;
    }

    /* Convertit la grille en une chaîne de caractère */
    public String convertGridToString(){
        String strGrid = "";
        for(int l = 0; l < this.grid.length; l++){
            for(int c = 0; c < this.grid[0].length; c++){
                if(this.grid[l][c] == EMPTY){ 
                    strGrid+=" 0 ";
                }else{ strGrid+= " " + this.grid[l][c] + " "; } 
            }
            strGrid+="\n";
        }
        strGrid+=("----------------------\n");
        /* Lignes */
        for(int i = 0; i < this.grid[0].length; i++){
            strGrid+=" " + (i+1) + " ";
        }
        return strGrid;
    }
}