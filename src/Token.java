package src;
import java.util.EnumMap;

import src.Grid.DIRECTION;
import src.Player.COLOR;

public class Token {
    public static final String CROSS = "X";

    private COLOR color;

    private EnumMap<DIRECTION, Token> neighbors = new EnumMap<>(DIRECTION.class);

    public Token(COLOR C, String ANSIcolor){
        this.color = C;
    }

    public Token(COLOR C){
        this.color = C;
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
        t.printNeighbors1();
        if( t.neighbors.containsKey(d) ){
            if(t.neighbors.get(d) != null && t.getColor().equals(t.neighbors.get(d).getColor())){
                return isAlign(t.neighbors.get(d), d, nb + 1);
            }
        }   return nb;
    }
}  