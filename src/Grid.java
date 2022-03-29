package src;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;

import src.Player.COLOR;

public class Grid {

    public static final Token EMPTY = null;
    public static boolean FULL_GRID = false;

    private Token[][] grid = new Token[6][7];
    public enum DIRECTION{ LEFT, RIGHT, UP, DOWN, UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT }

    public Token[][] getGrid(){ return grid; }

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

        Token t = new Token(p.getTokenColor(), p.getANSIStringColor(), l, c);
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
                unload(Game.SAVE_FILE);
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
        }
        return str;
    }

    public void printNeighborsGrid(){
        for(int l = 0; l < this.grid.length; l++){
            for(int c = 0; c < this.grid[0].length; c++){
                if(this.grid[l][c] != EMPTY){ 
                    System.out.println(l + "/" + c + ": " + this.grid[l][c].getColor() + " ");
                }
            }
        }
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

    public void save(String path, Player old_J1, Player old_J2){
        try{
            RandomAccessFile fw = new RandomAccessFile(path, "rw");
            if(fw.length() != 0) fw.setLength(0);
            fw.writeChars(old_J1.getTokenColor().toString() + " " + old_J2.getTokenColor().toString() + "\n");
            fw.writeChars(this.findNeighborsGrid());
            fw.seek(0);
            fw.close();
        }catch(IOException ioe){
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public void load(String path, Player new_J1, Player new_J2){
        try{
            RandomAccessFile fw = new RandomAccessFile(path, "r");
            fw.seek(0);
            String line;
            line = fw.readLine().replace("\u0000", "");
            String[] line_sep = line.split(" ");
            List<String> line_sep_list = Arrays.asList(line_sep);

            Iterator<String> it = line_sep_list.iterator();
            if(it != null) new_J1.setColor(COLOR.valueOf(it.next()));
            if(it != null) new_J2.setColor(COLOR.valueOf(it.next()));
           
            if(fw.length() != 0){
                Grid loaded_grid = new Grid();
                while(fw.getFilePointer() != fw.length()){
                    line = fw.readLine().replace("\u0000", ""); 
                    line_sep = line.split(" ");
                    line_sep_list = Arrays.asList(line_sep);
                    it = line_sep_list.iterator();

                    int l = Integer.parseInt(it.next().trim());
                    int c = Integer.parseInt(it.next().trim());

                    Token t = new Token(COLOR.valueOf(it.next()), l, c);
 
                    while(it.hasNext()){
                        COLOR C = COLOR.valueOf(it.next()); 
                        DIRECTION D = DIRECTION.valueOf(it.next());           
                        if(D.equals(DIRECTION.LEFT)){
                            Token tok_nbhood = new Token(C, l, c - 1);
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.RIGHT, loaded_grid, l, c, 0, -1);
                        }else if(D.equals(DIRECTION.RIGHT)){
                            Token tok_nbhood = new Token(C, l, c + 1);
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.LEFT, loaded_grid, l, c, 0, 1);
                        }else if(D.equals(DIRECTION.UP)){
                            Token tok_nbhood = new Token(C, l - 1, c);
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.DOWN, loaded_grid, l, c, -1, 0);
                        }else if(D.equals(DIRECTION.DOWN)){
                            Token tok_nbhood = new Token(C, l + 1, c);
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.UP, loaded_grid, l, c, 1, 0);
                        }else if(D.equals(DIRECTION.UP_RIGHT)){
                            Token tok_nbhood = new Token(C, l - 1, c + 1);
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.DOWN_LEFT, loaded_grid, l, c, -1, 1);
                        }else if(D.equals(DIRECTION.UP_LEFT)){
                            Token tok_nbhood = new Token(C, l - 1, c - 1);
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.DOWN_RIGHT, loaded_grid, l, c, -1, -1);
                        }else if(D.equals(DIRECTION.DOWN_RIGHT)){
                            Token tok_nbhood = new Token(C, l + 1, c + 1);
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.UP_LEFT, loaded_grid, l, c, 1, 1);
                        }else if(D.equals(DIRECTION.DOWN_LEFT)){
                            Token tok_nbhood = new Token(C, l + 1, c - 1);
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.UP_RIGHT, loaded_grid, l, c, 1, -1);
                        }
                    }    
                }  
                    fw.close(); 
                    this.grid = loaded_grid.grid;
                    //this.printNeighborsGrid();
            }
        }catch(IOException ioe){
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    public void loadNeighbors(Token t, Token tok_nbhood, DIRECTION D, DIRECTION D_nbhood, Grid loaded_grid, int l, int c, int offx, int offy){
        if(loaded_grid.grid[l + offx][c + offy] == null){
            loaded_grid.grid[l][c] = t;
            loaded_grid.grid[l + offx][c + offy] = tok_nbhood;
            t.setNeighbors(D, tok_nbhood);
            tok_nbhood.setNeighbors(D_nbhood, loaded_grid.grid[l][c]);
        }else{
            t.setNeighbors(D, loaded_grid.grid[l + offx][c + offy]);
            loaded_grid.grid[l + offx][c + offy].setNeighbors(D_nbhood, t);
        }

    }  
    
    public void unload(String path){
        try{
            RandomAccessFile fw = new RandomAccessFile(path, "rw");
            fw.setLength(0);
            fw.close();
        }catch(IOException ioe){
            System.err.println("IOException: " + ioe.getMessage());
        }
    }
    
    /* Inner class Token */
    private class Token {
        public static final String CROSS = "X";

        private COLOR color;
        int l;
        int c;

        private EnumMap<DIRECTION, Token> neighbors = new EnumMap<>(DIRECTION.class);
    
        public Token(COLOR C, String ANSIcolor, int l, int c){
            this.color = C;
            this.l = l;
            this.c = c;
        }

        public Token(COLOR C, int l, int c){
            this.color = C;
            this.l = l;
            this.c = c;
        }

        public void setNeighbors(DIRECTION D, Token T){
            this.neighbors.put(D, T);
        }

        public COLOR getColor(){ return this.color; }

        @Override
        public String toString(){
            if(this.color.equals(COLOR.RED))   { return Game.ANSI_RED    + CROSS + Game.ANSI_WHITE; }
            if(this.color.equals(COLOR.YELLOW)){ return Game.ANSI_YELLOW + CROSS + Game.ANSI_WHITE; }
            if(this.color.equals(COLOR.CYAN))  { return Game.ANSI_CYAN   + CROSS + Game.ANSI_WHITE; }
            if(this.color.equals(COLOR.PURPLE)){ return Game.ANSI_PURPLE + CROSS + Game.ANSI_WHITE; }
            return Game.ANSI_GREEN + CROSS + Game.ANSI_WHITE;
        }

        public String printNeighbors(){
            String str = "";
            for( DIRECTION d : neighbors.keySet() ){
                str+= neighbors.get(d).getColor() + " " + d + " ";
            } 
            return str;
        }
        
        public void printNeighbors1(){ System.out.println(neighbors.toString());}

        public boolean check(){
            if(isAlign(this, DIRECTION.RIGHT, 0) + isAlign(this, DIRECTION.LEFT, 0) >= 3){
                return true;
            }else if(isAlign(this, DIRECTION.UP, 0) + isAlign(this, DIRECTION.DOWN, 0) >= 3){
                return true;
            }else if(isAlign(this, DIRECTION.UP_LEFT, 0) + isAlign(this, DIRECTION.DOWN_RIGHT, 0) >= 3){
                return true;
            }else if(isAlign(this, DIRECTION.UP_RIGHT, 0) + isAlign(this, DIRECTION.DOWN_LEFT, 0) >= 3){
                return true;
            }   return false;
        }   
        
        public int isAlign(Token t, DIRECTION d, int nb){
            System.out.println(d + " " + nb + " " + t.l + " " + t.c);
            t.printNeighbors1();
            if( t.neighbors.containsKey(d) ){
                if(t.neighbors.get(d) != null && t.getColor().equals(t.neighbors.get(d).getColor())){
                    return isAlign(t.neighbors.get(d), d, nb + 1);
                }
            }   return nb;
        }
    }  
}
