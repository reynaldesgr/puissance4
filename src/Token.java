package src;

import java.util.EnumMap;

import src.Grid.DIRECTION;
import src.Player.COLOR;

/* Classe Token (jeton) */
public class Token {
    public static final String CROSS = "X";
    private COLOR color;

    /* EnumMap contenant: 
    le référencement vers les voisins du jeton de l'instance courante 
    + la direction de ses voisins dans la grille par rapport à la position de ce jeton*/
    private EnumMap<DIRECTION, Token> neighbors = new EnumMap<>(DIRECTION.class); 
    
    public Token(COLOR C){
        this.color = C;
    }

    /* Setter : rajoute un voisin au Token T dans la direction D) */
    public void setNeighbors(DIRECTION d, Token t){
        this.neighbors.put(d, t);
    }

    public void setNeighbors(EnumMap<DIRECTION, Token> map){
        this.neighbors = map;
    }

    public EnumMap<DIRECTION, Token> getNeighbors(){
        return neighbors.clone();
    }

    /* Retourne la couleur (COLOR) du jeton */
    public COLOR getColor(){ return this.color; }

    @Override
    public String toString(){
        if(this.color.equals(COLOR.RED))   { return Game.ANSI_RED    + CROSS + Game.ANSI_WHITE; }
        if(this.color.equals(COLOR.YELLOW)){ return Game.ANSI_YELLOW + CROSS + Game.ANSI_WHITE; }
        if(this.color.equals(COLOR.CYAN))  { return Game.ANSI_CYAN   + CROSS + Game.ANSI_WHITE; }
        if(this.color.equals(COLOR.PURPLE)){ return Game.ANSI_PURPLE + CROSS + Game.ANSI_WHITE; }
        return Game.ANSI_GREEN + CROSS + Game.ANSI_WHITE;
    }

    /* Retourne une chaîne de caractère contenant les voisins du jeton de l'instance courante 
    /* ainsi que leur direction. */
    public String toStringNeighbors(){
        String str = "";
        for( DIRECTION d : neighbors.keySet() ){
            str+= neighbors.get(d).getColor() + " " + d + " ";
        } 
        return str;
    }

    /**  Méthode de check **/
    /* Cette méthode vérifie si le jeton de l'instance courante est dans un alignement de 3 autres jetons
    /* dans une direction permettant un alignement */
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
    
    /** Méthode isAlign (récursive) -> fonctionne en "paire" avec check() **/
    /* Méthode récursive permettant de compter le nombre de voisins du Token t
    /* qui sont aligné à ce Token t dans une direction d */
    public int isAlign(Token t, DIRECTION d, int nb){
        if( t.neighbors.containsKey(d) ){
            if(t.neighbors.get(d) != null && t.getColor().equals(t.neighbors.get(d).getColor())){
                return isAlign(t.neighbors.get(d), d, nb + 1);
            }
        }   return nb;
    }
}  