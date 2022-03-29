package src;

public class Grid {

    public static final Token EMPTY = null;
    public static boolean FULL_GRID = false;

    private FileGridSaver fSaver = new FileGridSaver();

    private Token[][] grid = new Token[6][7];
    public enum DIRECTION{ LEFT, RIGHT, UP, DOWN, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT }

    public Token[][] getGrid(){ return this.grid; }

    public void loadGrid(String path, Player J1, Player J2){ this.setGrid(fSaver.load(path, J1, J2)); }
    public void saveGrid(String path, Player J1, Player J2){ fSaver.save(path, this, J1, J2);}

    private void setGrid(Grid grid){ this.grid = grid.getGrid(); }
    /* Placer le jeton t sur la colonne c */
    public boolean placeToken(Player p, int column) throws IllegalArgumentException{

        if(column > grid[0].length) throw new IllegalArgumentException("Invalid column value.");


        int l = this.grid.length - 1; // Ligne la + basse
        int c = column - 1;
        boolean isFull = false;

        while(!isFull && (grid[l][c]) != EMPTY){
            if(l == 0){ 
                isFull = true; 
            }   --l;
        }

        Token t = new Token(p.getTokenColor(), p.getANSIStringColor());
        // SI la grille n'est pas pleine, on place le jeton.
        if(!isFull){ 
            this.grid[l][c] = t;
            referLeftNeighbor(c, l, t);
            referRightNeighbor(c, l, t);

            referUpNeighbor(c, l, t);
            referDownNeighbor(c, l, t);

            referUpLeftNeighbor(c, l, t);
            referUpRightNeighbor(c, l, t);

            referDownRightNeighbor(c, l, t);
            referDownLeftNeighbor(c, l, t);

            //t.printNeighbors1();
            if( t.check() ){
                System.out.println(p.getName() + " - WIN ");
                this.printGrid();
                fSaver.unload(Game.SAVE_FILE);
                System.exit(0);
            }   return true;
        }
        return false;
    }

    public void referLeftNeighbor(int c, int l, Token t){
        /** LEFT **/
        if(c != 0){
            if(this.grid[l][c - 1] != EMPTY){ 
                t.setNeighbors(DIRECTION.LEFT, this.grid[l][c - 1]); 
                this.grid[l][c - 1].setNeighbors(DIRECTION.RIGHT, this.grid[l][c]);
            }
        }
    }

    public void referUpNeighbor(int c, int l, Token t){
        /** UP **/
        if(l != 0){
            if(this.grid[l - 1][c] != EMPTY){ 
                t.setNeighbors(DIRECTION.UP, this.grid[l - 1][c]);
                this.grid[l - 1][c].setNeighbors(DIRECTION.DOWN, this.grid[l][c]);
            }
        }
    }

    public void referRightNeighbor(int c, int l, Token t){
        /** RIGHT **/
        if(c != this.grid[0].length - 1){
            if(this.grid[l][c + 1] != EMPTY){
                t.setNeighbors(DIRECTION.RIGHT, this.grid[l][c + 1]);
                this.grid[l][c + 1].setNeighbors(DIRECTION.LEFT, this.grid[l][c]);
            }
        }
    }

    public void referDownNeighbor(int c, int l, Token t){
        /** DOWN  **/
        if(l != this.grid.length - 1){
            if(this.grid[l + 1][c] != EMPTY){
                t.setNeighbors(DIRECTION.DOWN, this.grid[l + 1][c]);
                this.grid[l + 1][c].setNeighbors(DIRECTION.UP, this.grid[l][c]);
            }
        }
    }

    public void referUpRightNeighbor(int c, int l, Token t){
        /** UP RIGHT **/
        if(l != 0 && c != this.grid[0].length - 1){
            if(this.grid[l - 1][c + 1] != EMPTY){
                t.setNeighbors(DIRECTION.UP_RIGHT, this.grid[l - 1][c + 1]);
                this.grid[l - 1][c + 1].setNeighbors(DIRECTION.DOWN_LEFT, this.grid[l][c]);
            }
        }
    }

    public void referUpLeftNeighbor(int c, int l, Token t){
        /** UP LEFT **/
        if(l != 0 && c != 0){
            if(this.grid[l - 1][c - 1] != EMPTY){
                t.setNeighbors(DIRECTION.UP_LEFT, this.grid[l - 1][c - 1]);
                this.grid[l - 1][c - 1].setNeighbors(DIRECTION.DOWN_RIGHT, this.grid[l][c]);
            }
        }
    }

    public void referDownLeftNeighbor(int c, int l, Token t){
        /** DOWN LEFT **/
        if(l != this.grid.length - 1 && c != 0){
            if(this.grid[l + 1][c - 1] != EMPTY){
                t.setNeighbors(DIRECTION.DOWN_LEFT, this.grid[l + 1][c - 1]);
                this.grid[l + 1][c - 1].setNeighbors(DIRECTION.UP_RIGHT, this.grid[l][c]);
            }
        }
    }

    public void referDownRightNeighbor(int c, int l, Token t){
        /** DOWN RIGHT **/
        if(l != this.grid.length - 1 && c != this.grid[0].length - 1){
            if(this.grid[l + 1][c + 1] != EMPTY){
                t.setNeighbors(DIRECTION.DOWN_RIGHT, this.grid[l + 1][c + 1]);
                this.grid[l + 1][c + 1].setNeighbors(DIRECTION.UP_LEFT, this.grid[l][c]);
            }
        }
    }

    public void printGrid(){
        System.out.println(this.convertGridToString());
    }

    public String findNeighborsGrid(){
        String str="";
        for(int l = 0; l < this.grid.length; l++){
            for(int c = 0; c < this.grid[0].length; c++){
                if(this.grid[l][c] != EMPTY){ 
                    str+=(l + " " + c + " " + this.grid[l][c].getColor() + " ");
                    str+=  this.grid[l][c].printNeighbors() + "\n";
                }
            }
        }   return str;
    }

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

        /* Print lines */
        for(int i = 0; i < this.grid[0].length; i++){
            strGrid+=" " + (i+1) + " ";
        }
        return strGrid;
    }
}
