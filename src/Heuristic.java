package src;


/** Note une grille : potentiel de gain pour un joueur donné **/

public abstract class Heuristic
{
    public static final int MAX_NOTE = Integer.MAX_VALUE;
    public static final int MIN_NOTE = Integer.MIN_VALUE;

    public abstract int noteGrid(Grid grid, Player p);
}